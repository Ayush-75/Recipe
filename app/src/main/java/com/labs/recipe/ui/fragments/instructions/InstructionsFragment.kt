package com.labs.recipe.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.labs.recipe.databinding.FragmentInstructionsBinding
import com.labs.recipe.models.Result
import com.labs.recipe.utils.Constant.Companion.RECIPE_RESULT_KEY
import com.labs.recipe.utils.retrieveParcelable


class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle:Result? = args?.retrieveParcelable(RECIPE_RESULT_KEY)

        if (myBundle!=null){
            binding.instructionsWebView.webViewClient = object : WebViewClient(){}
            val websiteUrl: String = myBundle.sourceUrl
            binding.instructionsWebView.loadUrl(websiteUrl)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}