package cz.johrusk.myapp

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {
    private val recipeList = ArrayList<Recipe>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: RecipeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView

        mAdapter = RecipeAdapter(recipeList)

        recyclerView!!.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
       // recyclerView!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter

        recyclerView!!.addOnItemTouchListener(RecyclerTouchListener(applicationContext, recyclerView as RecyclerView, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                val recipe = recipeList[position]
                Toast.makeText(applicationContext, recipe.title + " is selected!", Toast.LENGTH_SHORT).show()
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

    class RecyclerTouchListener(context: Context, recyclerView: RecyclerView, private val clickListener: MainActivity.ClickListener?) : RecyclerView.OnItemTouchListener {

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
