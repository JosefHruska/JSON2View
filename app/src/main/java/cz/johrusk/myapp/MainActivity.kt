package cz.johrusk.myapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_content.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.warn
import java.util.*

class MainActivity : AppCompatActivity(), AnkoLogger {
    private val recipeList = ArrayList<Recipe>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: RecipeAdapter? = null

    companion object{
        var isTablet = false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (isTablet and (detail_frame != null)) {
            warn("Device is considered as Tablet (7''+)")
            supportFragmentManager.beginTransaction().replace(R.id.detail_frame, DetailFragment()).commit()
            val dParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            dParam.weight = 3.0f
            val cParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            cParam.weight = 2.0f
            detail_frame.layoutParams = dParam
            cont.layoutParams = cParam
//            val detail_param  = detail_frame as LinearLayout.LayoutParams?: throw NullPointerException("params is null?")
//            detail_param.weight = 3.0f
//            val cont_param  = detail_frame as LinearLayout.LayoutParams?: throw NullPointerException("params is null?")
//            cont_param.weight = 2.0f
        } else warn("Device is considered as Phone (7''+)")

        supportFragmentManager.beginTransaction().replace(R.id.cont, MainFragment()).commit()
        //   val toolbar = findViewById(R.id.toolbar) as Toolbar //TODO Is it OK?
        setSupportActionBar(toolbar)


    }


    fun isTablet() {

        fun screenInches(): Double {
            val dm: DisplayMetrics = DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            val width = dm.widthPixels.toDouble()
            val height = dm.heightPixels.toDouble();
            val dens = dm.densityDpi.toDouble()
            val wi: Double = (width / dens)
            val hi: Double = height / dens
            val x: Double = Math.pow(wi, 2.0)
            val y = Math.pow(hi, 2.0)
            return Math.sqrt(x + y)

        }
        warn("Screen size is " + screenInches().toString())
        if (screenInches() >= 7) {
            isTablet = true
        } else {
            isTablet = false
        }
    }

    fun showProgressCircle(isVisible: Boolean) {
        if (loading_view != null) {
            if (isVisible) {
                loading_view?.visibility = View.VISIBLE
                debug("progress visibility = true")
            } else {
                loading_view?.visibility = View.GONE
                debug("progress visibility = false")
            }
        }
    }

}