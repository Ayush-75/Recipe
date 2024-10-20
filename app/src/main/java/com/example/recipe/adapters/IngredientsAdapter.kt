package com.example.recipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.recipe.R
import com.example.recipe.databinding.IngredientsRowLayoutBinding
import com.example.recipe.models.ExtendedIngredient
import com.example.recipe.utils.Constant.Companion.BASE_IMAGE_URL
import com.example.recipe.utils.RecipesDiffutils
import java.util.Locale

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var ingredientList = emptyList<ExtendedIngredient>()

    class ViewHolder(private val binding: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExtendedIngredient) {
            val res = itemView.context.resources
            with(binding) {
                ingredientImageView.load(BASE_IMAGE_URL + item.image) {
                    crossfade(600)
                    error(R.drawable.ic_error_placeholder)
                }
                ingredientName.text = item.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                    else it.toString()
                }
                ingredientAmount.text = item.amount.toString()
                ingredientUnit.text = item.unit
                ingredientConsistency.text = item.consistency
                ingredientOriginal.text = item.original
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = IngredientsRowLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ingredientList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    fun setData(newIngredientList: List<ExtendedIngredient>) {
        val ingredientsDiffUtil = RecipesDiffutils(ingredientList, newIngredientList)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientList = newIngredientList
        diffUtilResult.dispatchUpdatesTo(this)

    }
}