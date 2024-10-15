package com.example.recipe.data

import com.example.recipe.data.database.RecipeDao
import com.example.recipe.data.database.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipeDao: RecipeDao
) {

    fun readDatabase(): Flow<List<RecipesEntity>> {
        return recipeDao.recipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipeDao.insertRecipes(recipesEntity)
    }
}