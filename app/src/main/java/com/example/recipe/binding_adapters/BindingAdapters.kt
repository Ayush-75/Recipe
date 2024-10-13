package com.example.recipe.binding_adapters

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.example.recipe.R

@BindingAdapter("loadImageFromUrl")
fun loadImageFromUrl(imageView: AppCompatImageView, imageUrl: String){
    imageView.load(imageUrl){
        crossfade(600)
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