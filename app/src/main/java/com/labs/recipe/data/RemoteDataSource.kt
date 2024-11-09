package com.labs.recipe.data

import com.labs.recipe.data.network.FoodRecipeApi
import com.labs.recipe.models.FoodJoke
import com.labs.recipe.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: FoodRecipeApi
) {

    suspend fun getRecipes(queryMap:Map<String,String>):Response<FoodRecipe>{
        return foodRecipeApi.getRecipes(queryMap)
    }

    suspend fun searchRecipes(searchQuery:Map<String,String>):Response<FoodRecipe>{
        return foodRecipeApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey:String):Response<FoodJoke>{
        return foodRecipeApi.getFoodJoke(apiKey)
    }
}
