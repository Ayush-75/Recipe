package com.example.recipe.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe.viewmodels.MainViewModel
import com.example.recipe.adapters.RecipesAdapter
import com.example.recipe.databinding.FragmentRecipeBinding
import com.example.recipe.utils.Constant.Companion.API_KEY
import com.example.recipe.utils.NetworkResult
import com.example.recipe.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private val recipesViewModel: RecipesViewModel by viewModels()
    private val mAdapter by lazy { RecipesAdapter() }
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)

        setUpRecyclerView()
        requireApiData()

        return binding.root
    }

    private fun requireApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQuery())
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
}