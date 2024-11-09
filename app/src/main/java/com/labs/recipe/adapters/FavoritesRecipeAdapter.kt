package com.labs.recipe.adapters

import android.view.LayoutInflater
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.labs.recipe.R
import com.labs.recipe.databinding.FavoritesRecipesRowLayoutBinding
import com.labs.recipe.data.database.entities.FavoritesEntity
import com.labs.recipe.ui.fragments.favorites.FavRecipeFragmentDirections
import com.labs.recipe.utils.RecipesDiffutils
import com.labs.recipe.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesRecipeAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavoritesRecipeAdapter.ViewHolder>(), ActionMode.Callback {

    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var viewHolder = arrayListOf<ViewHolder>()
    private var favoritesRecipes = emptyList<FavoritesEntity>()
    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    class ViewHolder(val binding: FavoritesRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = FavoritesRecipesRowLayoutBinding.inflate(
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
        viewHolder.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favoritesRecipes[position]
        holder.bind(currentRecipe)

        saveItemStateOnScroll(currentRecipe, holder)

        // single click
        holder.binding.favRecipesRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                holder.itemView.findNavController()
                    .navigate(
                        FavRecipeFragmentDirections.actionFavRecipeFragmentToDetailsActivity(
                            currentRecipe.result
                        )
                    )
            }
        }

        //long click
        holder.binding.favRecipesRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                applySelection(holder, currentRecipe)
                true
            }
        }
    }

    private fun saveItemStateOnScroll(currentRecipe: FavoritesEntity, holder: ViewHolder) {
        if (selectedRecipes.contains(currentRecipe)) {
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
        } else {
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
    }

    private fun applySelection(holder: ViewHolder, currentRecipe: FavoritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: ViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.favRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.favRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }

            1 -> {
                mActionMode.title = "${selectedRecipes.size} item selected"
            }

            else -> {
                mActionMode.title = "${selectedRecipes.size} items selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favoritesRecipes.size
    }

    fun setData(newFavoriteRecipes: List<FavoritesEntity>) {
        val favoriteRecipesDiffUtil = RecipesDiffutils(favoritesRecipes, newFavoriteRecipes)
        val diffUtilResult =
            androidx.recyclerview.widget.DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoritesRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.fav_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favorite_menu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavRecipes(it)
            }
            showSnackBar("${selectedRecipes.size} Recipes removed")
            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        viewHolder.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).setAction("Okay") {}.show()
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

}