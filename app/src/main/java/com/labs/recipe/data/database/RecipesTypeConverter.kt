package com.labs.recipe.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.labs.recipe.models.FoodRecipe
import com.labs.recipe.models.Result
import com.squareup.moshi.Moshi
import javax.inject.Inject

@ProvidedTypeConverter
class RecipesTypeConverter @Inject constructor(val moshi: Moshi) {


    private val foodRecipeAdapter = moshi.adapter(FoodRecipe::class.java)
    private val resultAdapter = moshi.adapter(Result::class.java)

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe):String{
        return foodRecipeAdapter.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data:String):FoodRecipe?{
        return foodRecipeAdapter.fromJson(data)
    }

    @TypeConverter
    fun resultToString(result: Result):String{
        return resultAdapter.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data:String):Result?{
        return resultAdapter.fromJson(data)
    }

}