package com.example.recipe.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipe.data.DataStoreRepository
import com.example.recipe.utils.Constant.Companion.API_KEY
import com.example.recipe.utils.Constant.Companion.DEFAULT_DIET_TYPE
import com.example.recipe.utils.Constant.Companion.DEFAULT_MEAL_TYPE
import com.example.recipe.utils.Constant.Companion.DEFAULT_RECIPE_NUMBER
import com.example.recipe.utils.Constant.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.recipe.utils.Constant.Companion.QUERY_API_KEY
import com.example.recipe.utils.Constant.Companion.QUERY_DIET
import com.example.recipe.utils.Constant.Companion.QUERY_FILL_INGREDIENTS
import com.example.recipe.utils.Constant.Companion.QUERY_NUMBER
import com.example.recipe.utils.Constant.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(application: Application,private val dataStoreRepository: DataStoreRepository):AndroidViewModel(application) {

     fun applyQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_NUMBER] = DEFAULT_RECIPE_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = DEFAULT_MEAL_TYPE
        queries[QUERY_DIET] = DEFAULT_DIET_TYPE
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
}