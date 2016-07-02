package cz.johrusk.myapp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast
import java.util.*

class MainFragment : Fragment() {
    private val recipeList = ArrayList<Recipe>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: RecipeAdapter? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.main_content, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = find<RecyclerView>(R.id.recycler_view)
        mAdapter = RecipeAdapter(recipeList)

        recyclerView!!.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(ctx)
        recyclerView!!.layoutManager = mLayoutManager
        // recyclerView!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter

        recyclerView!!.addOnItemTouchListener(RecyclerTouchListener(ctx, recyclerView as RecyclerView, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                val recipe = recipeList[position]

                val iTitle =  recipe.title
                val iBody =  recipe.body
                toast(iTitle + " is selected!")
                EventBus.getDefault().post(MessageEvent(iTitle, iBody))
                EventBus.getDefault().postSticky(MessageEvent(iTitle, iBody))

                val args = Bundle();
                args.putString("title",iTitle)
                args.putString("body",iBody)
                val frag = DetailFragment()
                frag.arguments = args
                if (!MainActivity.isTablet) activity.supportFragmentManager.beginTransaction().replace(R.id.cont, frag).addToBackStack(null).commit()
            }

            override fun onLongClick(view: View, position: Int) {

            }
        }))

        prepareRecipeData()
    }

    private fun prepareRecipeData() {
        var recipe = Recipe("Mad Max: Fury Road", "Action & Adventure")
        recipeList.add(recipe)

        recipe = Recipe("Inside Out", "Animation, Kids & Family")
        recipeList.add(recipe)

        recipe = Recipe("Star Wars: Episode VII - The Force Awakens", "Action")
        recipeList.add(recipe)

        recipe = Recipe("Shaun the Sheep", "Animation")
        recipeList.add(recipe)

        recipe = Recipe("The Martian", "Science Fiction & Fantasy")
        recipeList.add(recipe)

        recipe = Recipe("Mission: Impossible Rogue Nation", "Action")
        recipeList.add(recipe)

        recipe = Recipe("Up", "Animation")
        recipeList.add(recipe)

        recipe = Recipe("Star Trek", "Science Fiction")
        recipeList.add(recipe)

        recipe = Recipe("The LEGO Recipe", "Animation")
        recipeList.add(recipe)

        recipe = Recipe("Iron Man", "Action & Adventure")
        recipeList.add(recipe)

        recipe = Recipe("Aliens", "Science Fiction")
        recipeList.add(recipe)

        recipe = Recipe("Chicken Run", "Animation")
        recipeList.add(recipe)

        recipe = Recipe("Back to the Future", "Science Fiction")
        recipeList.add(recipe)

        recipe = Recipe("Raiders of the Lost Ark", "Action & Adventure")
        recipeList.add(recipe)

        recipe = Recipe("Inside Out", "Animation, Kids & Family")
        recipeList.add(recipe)

        recipe = Recipe("Star Wars: Episode VII - The Force Awakens", "Action")
        recipeList.add(recipe)

        recipe = Recipe("Shaun the Sheep", "Animation")
        recipeList.add(recipe)

        recipe = Recipe("The Martian", "Science Fiction & Fantasy")
        recipeList.add(recipe)

        recipe = Recipe("Mission: Impossible Rogue Nation", "Action")
        recipeList.add(recipe)

        recipe = Recipe("Up", "Animation")
        recipeList.add(recipe)

        recipe = Recipe("Star Trek", "Science Fiction")
        recipeList.add(recipe)

        recipe = Recipe("The LEGO Recipe", "Animation")
        recipeList.add(recipe)

        recipe = Recipe("Iron Man", "Action & Adventure")
        recipeList.add(recipe)

        recipe = Recipe("Aliens", "Science Fiction")
        recipeList.add(recipe)

        recipe = Recipe("Chicken Run", "Animation")
        recipeList.add(recipe)

        recipe = Recipe("Back to the Future", "Science Fiction")
        recipeList.add(recipe)

        recipe = Recipe("Raiders of the Lost Ark", "Action & Adventure")
        recipeList.add(recipe)

        recipe = Recipe("Goldfinger", "Action & Adventure")
        recipeList.add(recipe)

        recipe = Recipe("Guardians of the Galaxy", "Science Fiction & Fantasy")
        recipeList.add(recipe)

        mAdapter!!.notifyDataSetChanged()
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View, position: Int)
    }

    class RecyclerTouchListener(context: Context, recyclerView: RecyclerView, private val clickListener: MainFragment.ClickListener?) : RecyclerView.OnItemTouchListener {

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




