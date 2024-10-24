package com.example.recipe.data.network

import com.example.recipe.models.FoodJoke
import com.example.recipe.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipeApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queryMap:Map<String,String>
    ):Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery:Map<String,String>
    ):Response<FoodRecipe>

    @GET("/food/jokes/random")
    suspend fun getFoodJoke(
        @Query("apiKey") apiKey: String
    ):Response<FoodJoke>
}