package cz.johrusk.myapp.Fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import cz.johrusk.myapp.Activity.MainActivity
import cz.johrusk.myapp.Adapter.RecipeAdapter
import cz.johrusk.myapp.R
import cz.johrusk.myapp.Realm.Recipe
import es.dmoral.prefs.Prefs
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import kotlinx.android.synthetic.main.main_content.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.warn
import org.json.JSONObject
import java.util.*


/**
 * Fragment which shows list with downloaded items
 *
 * @author Josef Hruška (pepa.hruska@gmail.com)
 */
class MainFragment : Fragment(), AnkoLogger {
    private var recipeList = ArrayList<Recipe>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: RecipeAdapter? = null

    companion object {
        var isFetching: Boolean = false
        var realmConfig: RealmConfiguration? = null
        var realm: Realm? = null
        var isDBComplete: Boolean? = null
        var showLoading = false
        var dbSize: Int? = null
        var mRequestQueue: RequestQueue? = null
        val TAG = "TAG" // Tag is best choice to safely remove all pending Volley request (onStop)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        warn("onOptionsItemSelected")
        when (item.itemId) {
            R.id.action_delete -> {
                deleteDB() // Delete database
                return true
            }
            R.id.action_reload -> {
                reloadDB() // Load Database manually
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.main_content, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realmConfig = RealmConfiguration.Builder(ctx).deleteRealmIfMigrationNeeded().build()
        realm = Realm.getInstance(realmConfig)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)



        recyclerView = find<RecyclerView>(R.id.recycler_view)
        mAdapter = RecipeAdapter(recipeList)

        recyclerView!!.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(ctx)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter

        recyclerView!!.addOnItemTouchListener(RecyclerTouchListener(ctx, recyclerView as RecyclerView, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                val recipe = recipeList[position]

                val iTitle = recipe.title!!
                val iBody = recipe.body!!
                toast(iTitle + " is selected!")
                EventBus.getDefault().post(MessageEvent(iTitle, iBody))
                EventBus.getDefault().postSticky(MessageEvent(iTitle, iBody))

                val args = Bundle()
                args.putString("title", iTitle)
                args.putString("body", iBody)
                val frag = DetailFragment()
                frag.arguments = args
                if (!MainActivity.isTablet) activity.supportFragmentManager.beginTransaction().replace(R.id.cont, frag).addToBackStack(null).commit()
            }

            override fun onLongClick(view: View, position: Int) {
            }
        }))

        work()
    }

    fun work() {
        showLoading = Prefs.with(ctx).readBoolean("SHOULD_SHOW_LOADING", true)
        if (!Prefs.with(ctx).readBoolean(getString(R.string.enough_items), false)) {
            (activity as MainActivity).showProgressCircle(true)
        }
        isDBComplete = Prefs.with(ctx).readBoolean("COMPLETELY_BACKED_UP", false)

        val numberPagesL = Prefs.with(ctx).readInt("NUMBER_OF_PAGES_IN_DB", 0) // Last page which was downloaded
        dbSize = realm!!.where(Recipe::class.java).findAll().size.toInt()
        when (isDBComplete) {
            false -> fetchJSON(numberPagesL)
            true -> {
                warn("Database is complete")
                populateFromDB()
            }
        }
    }

    fun reloadDB() {
        warn("Reloading DB...")
        mAdapter = RecipeAdapter(recipeList)
        work()
    }

    fun fetchJSON(URL_PAGE: Int) {

        if (showLoading) loading_view.visibility = View.VISIBLE
        warn("Method: fetchJSON")
        isFetching = true
        val BASE_URL: String = "https://api.stackexchange.com/2.2/questions?filter=withbody&fromdate=1459468800&todate=1461974400&order=desc&sort=creation&site=cooking&pagesize=5&page="
        warn("Loading page no. : " + (URL_PAGE + 1).toString() + " no. of items in DB - " + dbSize)

        val COMPLETE_URL: String = BASE_URL + (URL_PAGE + 1).toString()
        val buildUri: Uri = Uri.parse(COMPLETE_URL).buildUpon().build()
        val url: String = buildUri.toString()
        mRequestQueue = Volley.newRequestQueue(ctx)
        val obj: JSONObject
        val stringRequest: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener<JSONObject> { response ->  // calls when is request succesfull

            val isWriteOK = writeToDB(response)
            isFetching = false


            if (!isWriteOK) {
                Prefs.with(ctx).writeBoolean("COMPLETELY_BACKED_UP", true)
                isDBComplete = true
                return@Listener
            } else {
                fetchJSON(URL_PAGE + 1)
            }
        }, Response.ErrorListener { // calls when there is a connection problem 404, 502 etc.
            warn("There is a problem with connection")
            populateFromDB()
            isFetching = false
        })
        stringRequest.tag = TAG
        mRequestQueue!!.add(stringRequest)

    }

    override fun onStop() {
        super.onStop()
        warn { "Onstop" }
        Prefs.with(ctx).writeBoolean("SHOULD_SHOW_LOADING",false)
        mRequestQueue?.cancelAll(TAG) ?: warn("mRequestQueue == null")
    }


    fun writeToDB(jsonObj: JSONObject): Boolean {

            val gson: Gson = GsonBuilder().create()
            val jsonStr = jsonObj.getJSONArray("items").toString()
            val items: List<Recipe> = gson.fromJson(jsonStr, object : TypeToken<List<Recipe>>() {}.type)
            realm!!.beginTransaction()
            val realmItems: Collection<Recipe> = realm!!.copyToRealm(items) // Saves all items to DB
            realm!!.commitTransaction()
            dbSize = realm!!.where(Recipe::class.java).findAll().size.toInt() // Amount of items in DB
            warn("Size of db: " + dbSize.toString())
            savePageInDB(dbSize as Int)

                val viewLength = mAdapter?.itemCount ?: throw NullPointerException("mAdapter null")
                if ((dbSize!! - viewLength) >= 20) {
                    warn("adapter updated")
                    updateList(realmItems)

        }
        if (!jsonObj.getBoolean("has_more")) {
            warn("It was last page")
            if (loading_view.visibility == View.VISIBLE) {loading_view.visibility = View.GONE}
            populateFromDB()
            return false
        } else {
            warn("More pages available")
        }
        return true
    }

    fun deleteDB() {

        Prefs.with(ctx).writeBoolean("COMPLETELY_BACKED_UP", false)
        isDBComplete = false

        realm?.beginTransaction()
        mAdapter = null
        mAdapter?.notifyDataSetChanged()
        val result: RealmResults<Recipe> = realm?.where(Recipe::class.java)!!.findAll()
        result.deleteAllFromRealm()
        Prefs.with(ctx).writeInt(getString(R.string.page_number), 0)
        realm?.commitTransaction()

        warn("Deleting DB...")

    }

    fun updateList(newItems: Collection<Recipe>) {
        recipeList.addAll(newItems)
        mAdapter?.notifyItemRangeChanged(mAdapter!!.itemCount, newItems.size)
        Prefs.with(ctx).writeBoolean(getString(R.string.enough_items), true)
        (activity as MainActivity).showProgressCircle(false)
    }

    fun populateFromDB() {
        if(showLoading) {
            if (loading_view?.visibility == View.VISIBLE) {
                loading_view?.visibility = View.GONE
            }
        }
        var list = realm?.where(Recipe::class.java)?.findAll()?.toList()
        recipeList = ArrayList<Recipe>(list)
        mAdapter = RecipeAdapter(recipeList)
        recyclerView!!.adapter = mAdapter
        mAdapter!!.notifyDataSetChanged()
        warn("mADAPTER has size: " + mAdapter!!.itemCount)
    }

    fun savePageInDB(itemsNo: Int) {
        val no = (itemsNo / 5)
        Prefs.with(ctx).writeInt(getString(R.string.page_number), no)
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View, position: Int)
    }

    class RecyclerTouchListener(context: Context, recyclerView: RecyclerView, private val clickListener: ClickListener?) : RecyclerView.OnItemTouchListener {

        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                    }
                }
            })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }
    }

}

class MessageEvent(itemTitle: String, itemBody: String) {
    val title: String = itemTitle
    val body: String = itemBody
}




