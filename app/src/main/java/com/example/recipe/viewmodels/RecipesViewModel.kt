package com.example.recipe.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipe.utils.Constant.Companion.API_KEY
import com.example.recipe.utils.Constant.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.recipe.utils.Constant.Companion.QUERY_API_KEY
import com.example.recipe.utils.Constant.Companion.QUERY_DIET
import com.example.recipe.utils.Constant.Companion.QUERY_FILL_INGREDIENTS
import com.example.recipe.utils.Constant.Companion.QUERY_NUMBER
import com.example.recipe.utils.Constant.Companion.QUERY_TYPE


class RecipesViewModel(application: Application):AndroidViewModel(application) {

     fun applyQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = "snack"
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
}