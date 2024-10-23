package com.example.recipe.binding_adapters

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe.adapters.FavoritesRecipeAdapter
import com.example.recipe.data.database.entities.FavoritesEntity


@BindingAdapter("viewVisibility", "setData", requireAll = false)
fun setViewVisibility(
    view: View,
    favoritesEntity: List<FavoritesEntity>?,
    mAdapter: FavoritesRecipeAdapter?
) {
    if (favoritesEntity.isNullOrEmpty()) {
        when (view) {
            is AppCompatImageView -> {
                view.visibility = View.VISIBLE
            }

            is AppCompatTextView -> {
                view.visibility = View.VISIBLE
            }

            is RecyclerView -> {
                view.visibility = View.INVISIBLE
            }
        }
    } else {
        when (view) {
            is AppCompatImageView -> {
                view.visibility = View.INVISIBLE
            }

            is AppCompatTextView -> {
                view.visibility = View.INVISIBLE
            }

            is RecyclerView -> {
                view.visibility = View.VISIBLE
                mAdapter?.setData(favoritesEntity)
            }
        }
    }
}