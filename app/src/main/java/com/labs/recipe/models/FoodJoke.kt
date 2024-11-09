package com.labs.recipe.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FoodJoke(
    @Json(name = "text")
    val text: String
)