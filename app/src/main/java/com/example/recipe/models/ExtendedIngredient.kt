package com.example.recipe.models


data class ExtendedIngredient(
    val amount: Int,
    val consistency: String,
    val image: String,
    val name: String,
    val original: String,
    val unit: String
)