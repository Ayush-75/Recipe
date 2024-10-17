package com.example.recipe.ui.fragments.recipes.bottom_sheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.recipe.R
import com.example.recipe.databinding.RecipeBottomSheetBinding
import com.example.recipe.utils.Constant.Companion.DEFAULT_MEAL_TYPE
import com.example.recipe.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class RecipeBottomSheet : BottomSheetDialogFragment() {

    private val recipesViewModel: RecipesViewModel by viewModels()
    private var _binding: RecipeBottomSheetBinding? = null
    private val binding get() = _binding!!

    // default values
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_MEAL_TYPE
    private var dietTypeChipId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =  RecipeBottomSheetBinding.inflate(inflater, container, false)

        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner){value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId,binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId,binding.dietTypeChipGroup)
        }

        binding.mealTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId.first())
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId.first()
        }

        binding.dietTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId.first())
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId.first()
        }

        binding.applyBtn.setOnClickListener{
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )

            val action = RecipeBottomSheetDirections.actionRecipeBottomSheetToRecipeFragment(true)
            findNavController().navigate(action)
        }


        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup:ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipeBottomSheet", e.message.toString())
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
