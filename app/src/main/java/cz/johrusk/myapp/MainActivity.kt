package cz.johrusk.myapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import java.util.*

class MainActivity : AppCompatActivity() {
    private val recipeList = ArrayList<Recipe>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: RecipeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        supportFragmentManager.beginTransaction().replace(R.id.cont, MainFragment()).commit()
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)


        fun screenInches() {
            val dm: DisplayMetrics = DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            val width = dm.widthPixels.toDouble()
            val height = dm.heightPixels.toDouble();
            val dens = dm.densityDpi.toDouble()
            val wi: Double = (width / dens)
            val hi: Double = height / dens
            val x: Double = Math.pow(wi, 2.0)
            val y = Math.pow(hi, 2.0)
            var screenInches = Math.sqrt(x + y)
        }
    }
}