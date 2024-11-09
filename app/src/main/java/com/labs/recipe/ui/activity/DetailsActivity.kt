package com.labs.recipe.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.labs.recipe.R
import com.labs.recipe.adapters.PagerAdapter
import com.labs.recipe.data.database.entities.FavoritesEntity
import com.labs.recipe.databinding.ActivityDetailsBinding
import com.labs.recipe.ui.fragments.ingredients.IngredientsFragment
import com.labs.recipe.ui.fragments.instructions.InstructionsFragment
import com.labs.recipe.ui.fragments.overview.OverviewFragment
import com.labs.recipe.utils.Constant.Companion.RECIPE_RESULT_KEY
import com.labs.recipe.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private lateinit var menuItem: MenuItem

    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0

    private val args by navArgs<DetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpViewPager()

    }

    private fun setupFavoriteObserver(menuItem: MenuItem) {
        mainViewModel.readFavRecipes.observe(this) { favoritesEntity ->
            checkSavedRecipes(favoritesEntity, menuItem)
        }
    }

    private fun setUpViewPager() {
        val fragments =  listOf(OverviewFragment(),IngredientsFragment(),InstructionsFragment())
        val titles = listOf("Overview","Ingredients","Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val pagerAdapter = PagerAdapter(resultBundle, fragments, this@DetailsActivity)
        binding.viewPager.apply {
            isUserInputEnabled = false
                adapter = pagerAdapter
            }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_fav_recipe_menu)
        setupFavoriteObserver(menuItem)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        else if (item.itemId == R.id.save_fav_recipe_menu) {
            item.isChecked = !item.isChecked
            if (!recipeSaved) {
                saveToFavorites(item)
            } else {
                removeFromFav(item)
            }
//            recipeSaved = !recipeSaved

            }
        return super.onOptionsItemSelected(item)
    }

    private fun changeMenuItemIcon(menuItem: MenuItem, redFavorite: Int) {
        menuItem.setIcon(redFavorite)
    }

    private fun checkSavedRecipes(favoritesEntity: List<FavoritesEntity>,menuItem: MenuItem) {
        val foundRecipe = favoritesEntity.find { it.result.id == args.result.id }
                if (foundRecipe != null){
                    changeMenuItemIcon(menuItem, R.drawable.red_favorite)
                    menuItem.isChecked = true
                    savedRecipeId = foundRecipe.id
                    recipeSaved = true
                }else{
                    changeMenuItemIcon(menuItem, R.drawable.white_favorite)
                    menuItem.isChecked = false
                    recipeSaved = false
            }
        }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(
            0,
            args.result
        )
        mainViewModel.insertFavRecipes(favoritesEntity)
//        changeMenuItemIcon(item, R.color.red)
        showSnackbar("Recipe Saved")
        recipeSaved = true
    }

    private fun removeFromFav(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(
            savedRecipeId,
            args.result
        )
        mainViewModel.deleteFavRecipes(favoritesEntity)
        showSnackbar("Removed from Favorites")
        recipeSaved = false
        changeMenuItemIcon(item, R.drawable.white_favorite)
    }

    private fun showSnackbar(message: String) {
            Snackbar.make(
                binding.detailsLayout,message,Snackbar.LENGTH_SHORT
            ).setAction("Okay"){}.show()
    }
//
//    private fun changeMenuItemColor(item: MenuItem, color: Int) {
//        item.icon?.setTint(ContextCompat.getColor(this, color))
//    }
}