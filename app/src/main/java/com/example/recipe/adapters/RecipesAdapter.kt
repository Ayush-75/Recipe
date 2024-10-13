package com.example.recipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe.databinding.RecipeRowLayoutBinding
import com.example.recipe.models.FoodRecipe
import com.example.recipe.models.Result
import com.example.recipe.utils.RecipesDiffutils

class RecipesAdapter:RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    private var recipes = emptyList<Result>()

    class ViewHolder(private val binding: RecipeRowLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result){
            binding.result = result
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup):ViewHolder{
                val binding = RecipeRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(newData: FoodRecipe){
        val recipesDiffUtil = RecipesDiffutils(recipes, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)





    }
}