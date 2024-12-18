package com.labs.recipe.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.labs.recipe.data.database.entities.FavoritesEntity
import com.labs.recipe.data.database.entities.FoodJokeEntity
import com.labs.recipe.data.database.entities.RecipesEntity

@Database(entities = [RecipesEntity::class, FavoritesEntity::class, FoodJokeEntity::class], version = 2, exportSchema = false)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase:RoomDatabase() {

    abstract fun recipesDao(): RecipeDao
}