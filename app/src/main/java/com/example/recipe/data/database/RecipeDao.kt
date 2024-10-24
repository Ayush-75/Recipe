package com.example.recipe.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipe.data.database.entities.FavoritesEntity
import com.example.recipe.data.database.entities.FoodJokeEntity
import com.example.recipe.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    // network response
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavRecipes(favoritesEntity: FavoritesEntity)

    // favorites recipes
    @Query("SELECT * FROM favorite_recipes_table ORDER BY id DESC")
    fun readFavRecipes():Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavRecipe(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavRecipes()

    // food joke
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM food_joke_table ORDER BY id ASC")
    fun readFoodJoke():Flow<List<FoodJokeEntity>>
}