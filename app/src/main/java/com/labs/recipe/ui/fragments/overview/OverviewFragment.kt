package com.labs.recipe.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.labs.recipe.R
import com.labs.recipe.binding_adapters.parseHtml
import com.labs.recipe.databinding.FragmentOverviewBinding
import com.labs.recipe.models.Result
import com.labs.recipe.utils.Constant.Companion.RECIPE_RESULT_KEY
import com.labs.recipe.utils.retrieveParcelable

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle:Result? = args!!.retrieveParcelable(RECIPE_RESULT_KEY) as Result?

        if (myBundle != null) {

            with(binding) {
                mainImageView.load(myBundle.image)
                titleTextView.text = myBundle.title
                likesTextView.text = myBundle.aggregateLikes.toString()
                timeTextView.text = myBundle.readyInMinutes.toString()
//            myBundle?.summary?.let {
//                val summary = Jsoup.parse(it).text()
//                summaryTextView.text = summary
//            }

                parseHtml(summaryTextView, myBundle.summary)

                updateColors(myBundle.vegetarian, vegetarianTextView, vegetarianImageView)
                updateColors(myBundle.vegan, veganTextView, veganImageView)
                updateColors(myBundle.cheap, cheapTextView, cheapImageView)
                updateColors(myBundle.dairyFree, dairyFreeTextView, dairyFreeImageView)
                updateColors(myBundle.glutenFree, glutenFreeTextView, glutenFreeImageView)
                updateColors(myBundle.veryHealthy, healthyTextView, healthyImageView)

            }
        }

        return binding.root

    }

    private fun updateColors(stateIsOn:Boolean,textView: TextView,imageView: ImageView){
        if (stateIsOn){
            imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}