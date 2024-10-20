package com.example.recipe.binding_adapters

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.recipe.data.database.entities.RecipesEntity
import com.example.recipe.models.FoodRecipe
import com.example.recipe.utils.NetworkResult


@BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
fun handleError(
    view: View,
    apiResponse: NetworkResult<FoodRecipe>?,
    database: List<RecipesEntity>?
) {
    when(view){
        is AppCompatImageView -> {
            view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
        }
        is AppCompatTextView -> {
            view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
            view.text = apiResponse?.message.toString()
        }
    }

}
