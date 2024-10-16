package com.example.recipe.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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
import com.example.recipe.utils.Constant.Companion.QUERY_SEARCH
import com.example.recipe.utils.Constant.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

//    fun applyQueries(): HashMap<String, String> {
//        val queries: HashMap<String, String> = HashMap()
//
//        viewModelScope.launch {
//            readMealAndDietType.collect { value ->
//                mealType = value.selectedMealType
//                dietType = value.selectedDietType
//            }
//        }
//
//        queries[QUERY_NUMBER] = DEFAULT_RECIPE_NUMBER
//        queries[QUERY_API_KEY] = API_KEY
//        queries[QUERY_TYPE] = mealType
//        queries[QUERY_DIET] = dietType
//        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
//        queries[QUERY_FILL_INGREDIENTS] = "true"
//
//        return queries
//    }

    suspend fun applyQueries(): HashMap<String, String> {
        val mealAndDietType = readMealAndDietType.first() // This will suspend until the first value is emitted

        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_NUMBER] = DEFAULT_RECIPE_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealAndDietType.selectedMealType
        queries[QUERY_DIET] = mealAndDietType.selectedDietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun applySearchQuery(searchQuery:String):HashMap<String,String>{
        val queries:HashMap<String,String> = HashMap()

        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPE_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

}

//        else if (networkStatus){
//            Toast.makeText(getApplication(),"Internet Connection Available",Toast.LENGTH_SHORT).show()
//
//        }

