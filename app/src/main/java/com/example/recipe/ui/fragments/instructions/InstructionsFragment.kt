package com.example.recipe.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.recipe.R
import com.example.recipe.databinding.FragmentInstructionsBinding
import com.example.recipe.models.Result
import com.example.recipe.utils.Constant.Companion.RECIPE_RESULT_KEY


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
        val myBundle:Result? = args?.getParcelable(RECIPE_RESULT_KEY)

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