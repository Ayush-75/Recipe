package com.example.recipe.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.util.recursiveFetchLongSparseArray
import com.example.recipe.data.Repository
import com.example.recipe.data.database.entities.FavoritesEntity
import com.example.recipe.data.database.entities.FoodJokeEntity
import com.example.recipe.data.database.entities.RecipesEntity
import com.example.recipe.models.FoodJoke
import com.example.recipe.models.FoodRecipe
import com.example.recipe.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application, val repository: Repository) :
    AndroidViewModel(application) {

        var recyclerViewState:Parcelable? = null

    /**
     * Room Database
     **/

    val readRecipe: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    // food joke
    val readFoodJoke:LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    private fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }

    // Fav
    val readFavRecipes: LiveData<List<FavoritesEntity>> =
        repository.local.readFavRecipes().asLiveData()

    fun insertFavRecipes(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavRecipes(favoritesEntity)
        }

    fun deleteFavRecipes(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavRecipe(favoritesEntity)
        }

    fun deleteALlFavRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavRecipes()
        }

    /**
     * Retrofit Response
     **/


    val recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    val searchResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    val foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeCall(apiKey)
    }


    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {

        searchResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchResponse.value = NetworkResult.Error("Recipes Not Found")
                Log.d("TAG", "getRecipesSafeCall: ${e.message}")
            }
        } else {
            searchResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }


    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if (foodRecipe != null) {
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

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }

    private suspend fun getFoodJokeCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)

                val foodJoke = foodJokeResponse.value!!.data
                if (foodJoke !=null){
                    offlineCacheFoodJoke(foodJoke)
                }
            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error("Recipes Not Found")
                Log.d("TAG", "getRecipesSafeCall: ${e.message}")
            }
        } else {
            foodJokeResponse.value = NetworkResult.Error("No Internet Connection")
        }
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

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                NetworkResult.Error("API key Limited")
            }

            response.isSuccessful -> {
                val foodJoke = response.body()
                Log.d("MainViewModel", "foodRecipe: ${foodJoke.toString()}")
                NetworkResult.Success(foodJoke)
            }

            else -> {
                NetworkResult.Error(response.message())
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