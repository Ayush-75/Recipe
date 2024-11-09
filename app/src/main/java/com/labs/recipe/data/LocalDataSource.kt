package com.labs.recipe.data

import com.labs.recipe.data.database.RecipeDao
import com.labs.recipe.data.database.entities.FavoritesEntity
import com.labs.recipe.data.database.entities.FoodJokeEntity
import com.labs.recipe.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipeDao: RecipeDao
) {

    //recipes

    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipeDao.readRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipeDao.insertRecipes(recipesEntity)
    }

    // favorites

    fun readFavRecipes():Flow<List<FavoritesEntity>>{
        return recipeDao.readFavRecipes()
    }

    suspend fun insertFavRecipes(favoritesEntity: FavoritesEntity){
        recipeDao.insertFavRecipes(favoritesEntity)
    }

    suspend fun deleteFavRecipe(favoritesEntity: FavoritesEntity){
        recipeDao.deleteFavRecipe(favoritesEntity)
    }

    suspend fun deleteAllFavRecipes(){
        recipeDao.deleteAllFavRecipes()
    }

    // food joke

    fun readFoodJoke():Flow<List<FoodJokeEntity>>{
        return recipeDao.readFoodJoke()
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
         recipeDao.insertFoodJoke(foodJokeEntity)
    }
}