package com.labs.recipe.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.labs.recipe.R
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.repeatOnLifecycle
import com.labs.recipe.viewmodels.MainViewModel
import com.labs.recipe.adapters.RecipesAdapter
import com.labs.recipe.databinding.FragmentRecipeBinding
import com.labs.recipe.utils.NetworkListener
import com.labs.recipe.utils.NetworkResult
import com.labs.recipe.utils.observeOnce
import com.labs.recipe.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var dataRequested = false
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    private val mAdapter by lazy { RecipesAdapter() }

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RecipeFragmentArgs>()

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        if (mainViewModel.recyclerViewState !=null){
            binding.recyclerView.layoutManager?.onRestoreInstanceState(mainViewModel.recyclerViewState)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)

//        requireApiData()
//        readDatabase()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.recipes_menu, menu)

                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@RecipeFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setUpRecyclerView()

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                networkListener = NetworkListener()
                networkListener.checkNetworkAvailability(requireContext())
                    .collect { status ->
                        Log.d("NetworkListener", status.toString())
                        recipesViewModel.networkStatus = status
                        recipesViewModel.showNetworkStatus()
                        readDatabase()
                    }
            }
        }

        binding.recipeFab.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipeFragment_to_recipeBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    private fun readDatabase() {
        mainViewModel.readRecipe.observeOnce(viewLifecycleOwner) { database ->
            if (database.isNotEmpty() && !args.backFromBottomSheet || database.isNotEmpty() && dataRequested) {
                Log.d("Recipe Fragment", "readDatabase: localdatabase Called ")
                mAdapter.setData(database[0].foodRecipe)
                stopShimmerEffect()
            } else {
                if (!dataRequested) {
                    requireApiData()
                    dataRequested = true
                }
            }
        }

    }

    private fun requireApiData() {
        Log.d("Recipe Fragment", "requireApiData: api request Called ")
            mainViewModel.getRecipes(recipesViewModel.applyQueries())
            mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        stopShimmerEffect()
                        response.data?.let { mAdapter.setData(it) }
                        recipesViewModel.saveMealAndDietType()
                    }

                    is NetworkResult.Error -> {
                        stopShimmerEffect()
                        loadDataFromCache()
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


    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    stopShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    stopShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(context, response.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun loadDataFromCache() {
        mainViewModel.readRecipe.observe(viewLifecycleOwner) { database ->
            if (database.isNotEmpty()) {
                mAdapter.setData(database.first().foodRecipe)
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
        mainViewModel.recyclerViewState = binding.recyclerView.layoutManager?.onSaveInstanceState()
        super.onDestroyView()
        _binding = null
    }
}