package com.example.recipe.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.recipe.models.FoodRecipe
import com.squareup.moshi.Moshi
import javax.inject.Inject

@ProvidedTypeConverter
class RecipesTypeConverter @Inject constructor(val moshi: Moshi) {


    private val foodRecipeAdapter = moshi.adapter(FoodRecipe::class.java)

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe):String{
        return foodRecipeAdapter.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data:String):FoodRecipe?{
        return foodRecipeAdapter.fromJson(data)
    }

}