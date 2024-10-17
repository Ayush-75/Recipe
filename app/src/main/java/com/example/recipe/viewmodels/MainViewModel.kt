package com.example.recipe.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipe.data.Repository
import com.example.recipe.data.database.RecipesEntity
import com.example.recipe.models.FoodRecipe
import com.example.recipe.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application, val repository: Repository) :
    AndroidViewModel(application) {


    /**
     * Room Database
     **/

    val readRecipe:LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch {
            repository.local.insertRecipes(recipesEntity)
        }


    /**
     * Retrofit Response
     **/


    val recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if (foodRecipe != null){
                    offlineCacheRecipes(foodRecipe)
                }

            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes Not Found")
                Log.d("TAG", "getRecipesSafeCall: ${e.message}")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("API key Limited")
            }

            response.body()!!.results.isEmpty() -> {
                return NetworkResult.Error("Recipes not Found")
            }

            response.isSuccessful -> {
                val foodRecipe = response.body()
                Log.d("MainViewModel", "foodRecipe: ${foodRecipe.toString()}")
                return NetworkResult.Success(foodRecipe)
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }

    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }
}