package com.example.recipe.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe.R
import com.example.recipe.viewmodels.MainViewModel
import com.example.recipe.adapters.RecipesAdapter
import com.example.recipe.databinding.FragmentRecipeBinding
import com.example.recipe.utils.Constant.Companion.API_KEY
import com.example.recipe.utils.NetworkListener
import com.example.recipe.utils.NetworkResult
import com.example.recipe.utils.observeOnce
import com.example.recipe.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private val recipesViewModel: RecipesViewModel by viewModels()

    private val mAdapter by lazy { RecipesAdapter() }

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RecipeFragmentArgs>()

    private lateinit var networkListener: NetworkListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        setUpRecyclerView()
//        requireApiData()
//        readDatabase()

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner){
            recipesViewModel.backOnline = it
        }
//
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        binding.recipeFab.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipeFragment_to_recipeBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }

        return binding.root
    }

    private fun readDatabase() {
        mainViewModel.readRecipe.observeOnce(viewLifecycleOwner) { database ->
            if (database.isNotEmpty() && !args.backFromBottomSheet) {
                Log.d("Recipe Fragment", "readDatabase: localdatabase Called ")
                mAdapter.setData(database[0].foodRecipe)
                stopShimmerEffect()
            } else {
                requireApiData()
            }
        }

    }

    private fun requireApiData() {
        Log.d("Recipe Fragment", "requireApiData: api request Called ")
        lifecycleScope.launch {
            mainViewModel.getRecipes(recipesViewModel.applyQueries())
            mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        stopShimmerEffect()
                        response.data?.let { mAdapter.setData(it) }
                    }

                    is NetworkResult.Error -> {
                        stopShimmerEffect()
                        Toast.makeText(
                            requireContext(),
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is NetworkResult.Loading -> {
                        showShimmerEffect()
                    }
                }
            }
        }
    }


    private fun setUpRecyclerView() {
        with(binding) {
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            showShimmerEffect()
        }
    }

    private fun showShimmerEffect() {
        with(binding) {
            shimmerLayout.startShimmer()
            shimmerLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE

        }
    }

    private fun stopShimmerEffect() {
        with(binding) {
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}