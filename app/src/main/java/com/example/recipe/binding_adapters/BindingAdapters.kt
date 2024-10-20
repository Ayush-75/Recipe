package com.example.recipe.binding_adapters

import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.recipe.R
import com.example.recipe.models.Result
import com.example.recipe.ui.fragments.recipes.RecipeFragmentDirections
import org.jsoup.Jsoup

@BindingAdapter("loadImageFromUrl")
fun loadImageFromUrl(imageView: AppCompatImageView, imageUrl: String){
    imageView.load(imageUrl){
        crossfade(600)
        error(R.drawable.ic_error_placeholder)
    }
}

@BindingAdapter("applyVeganColor")
fun applyVeganColor(view:View,vegan: Boolean) {
     val greenColor by lazy { ContextCompat.getColor(view.context, R.color.green) }
    if (vegan){
        when(view){
            is AppCompatTextView -> {
                view.setTextColor(greenColor)
            }
            is AppCompatImageView -> {
                view.setColorFilter(greenColor)
            }
        }
    }
}

@BindingAdapter("onRecipeClickListener")
fun onRecipeClickListener(recipeRowLayout:ConstraintLayout,result: Result){
    Log.d("onRecipeClickListener", result.toString())
    recipeRowLayout.setOnClickListener {
        try {
            val action =
                RecipeFragmentDirections.actionRecipeFragmentToDetailsActivity(result)
            recipeRowLayout.findNavController().navigate(action)

        }catch (e:Exception){
            Log.d("onRecipeClickListener",  e.toString())
        }
    }
}

@BindingAdapter("parseHtml")
fun parseHtml(textView: AppCompatTextView, description: String?){
    if (description!=null){
        val desc = Jsoup.parse(description).text()
        textView.text = desc
    }
}