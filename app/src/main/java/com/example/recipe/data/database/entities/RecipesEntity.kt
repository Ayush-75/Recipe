package com.example.recipe.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipe.models.FoodRecipe
import com.example.recipe.utils.Constant.Companion.RECIPE_TABLE

@Entity(tableName = RECIPE_TABLE)
class RecipesEntity(var foodRecipe: FoodRecipe) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}