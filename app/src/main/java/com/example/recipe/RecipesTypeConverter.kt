package com.example.recipe

import androidx.room.TypeConverter
import com.example.recipe.models.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import javax.inject.Inject

class RecipesTypeConverter @Inject constructor(private val moshi: Moshi) {

    val gson = Gson()
    private val foodRecipeAdapter = moshi.adapter(FoodRecipe::class.java)

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe):String{
        return foodRecipeAdapter.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data:String):FoodRecipe{
        return foodRecipeAdapter.fromJson(data)!!
    }

}