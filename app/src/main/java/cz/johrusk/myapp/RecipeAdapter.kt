package cz.johrusk.myapp


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class RecipeAdapter(private val recipesList: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var body: TextView

        init {
            title = view.findViewById(R.id.title) as TextView
            body = view.findViewById(R.id.body) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = recipesList[position]
        holder.title.text = recipe.title
        holder.body.text = recipe.body

    }

    override fun getItemCount(): Int {
        return recipesList.size
    }
}
