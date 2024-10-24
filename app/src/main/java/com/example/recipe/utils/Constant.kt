package com.example.recipe.utils

import com.example.recipe.BuildConfig


class Constant {
    companion object{
        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://img.spoonacular.com/ingredients_100x100/"
        const val API_KEY = BuildConfig.API_KEY

        const val RECIPE_RESULT_KEY = "recipeBundle"

        //Api Query keys
        const val QUERY_SEARCH = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        //Room
        const val RECIPE_TABLE = "recipes_table"
        const val DATABASE_NAME = "recipes_database"
        const val FAVORITE_RECIPES_TABLE = "favorite_recipes_table"
        const val FOOD_JOKE_TABLE = "food_joke_table"

        //Bottom Sheet and preferences
        const val DEFAULT_RECIPE_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "vegetarian"

        const val PREFERENCE_NAME = "recipe_preferences"
        const val PREFERENCE_MEAL_TYPE = "mealType"
        const val PREFERENCE_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCE_DIET_TYPE = "dietType"
        const val PREFERENCE_DIET_TYPE_ID = "dietTypeId"

        const val PREFERENCE_BACK_ONLINE = "backOnline"
    }
}