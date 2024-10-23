package com.example.recipe.ui.fragments.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipe.R
import com.example.recipe.adapters.FavoritesRecipeAdapter
import com.example.recipe.databinding.FragmentFavRecipeBinding
import com.example.recipe.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavRecipeFragment : Fragment() {

    private var _binding: FragmentFavRecipeBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter:FavoritesRecipeAdapter by lazy { FavoritesRecipeAdapter(requireActivity(),mainViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavRecipeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter



//        mainViewModel.readFavRecipes.observe(viewLifecycleOwner){favoritesEntity ->
//            mAdapter.setData(favoritesEntity)
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fav_recipes_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.deleteAll_fav_recipes_menu) {
                    mainViewModel.deleteALlFavRecipes()
                    showSnackBar()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showSnackBar() {
        Snackbar.make(binding.root, "All recipes removed.", Snackbar.LENGTH_SHORT)
            .setAction("Okay") {}.show()
    }

    private fun setUpRecyclerView(){
        binding.favoritesRecipeRecycler.adapter = mAdapter
        binding.favoritesRecipeRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter.clearContextualActionMode()
    }
}
