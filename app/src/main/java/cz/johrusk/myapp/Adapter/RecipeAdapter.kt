package cz.johrusk.myapp.Adapter


import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cz.johrusk.myapp.R
import cz.johrusk.myapp.Realm.Recipe

/**
 * RecyclerView adapter
 *
 * @author Josef Hruška (pepa.hruska@gmail.com)
 */

class RecipeAdapter(private val recipesList: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView

        init {
            title = view.findViewById(R.id.title) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = recipesList[position]
        holder.title.text = (Html.fromHtml(recipe.title))
    }
    override fun getItemCount(): Int {
        return recipesList.size
    }
}
