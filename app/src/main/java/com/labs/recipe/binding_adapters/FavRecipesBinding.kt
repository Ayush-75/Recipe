package com.labs.recipe.binding_adapters

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.labs.recipe.adapters.FavoritesRecipeAdapter
import com.labs.recipe.data.database.entities.FavoritesEntity


@BindingAdapter("setVisibility", "setData", requireAll = false)
fun setViewVisibility(
    view: View,
    favoritesEntity: List<FavoritesEntity>?,
    mAdapter: FavoritesRecipeAdapter?
) {
        when (view) {
            is RecyclerView -> {
                val dataCheck = favoritesEntity.isNullOrEmpty()
                view.isInvisible = dataCheck
                if (!dataCheck){
                    favoritesEntity?.let { mAdapter?.setData(it) }
            }
        }
            else -> view.isVisible = favoritesEntity.isNullOrEmpty()
    }
}