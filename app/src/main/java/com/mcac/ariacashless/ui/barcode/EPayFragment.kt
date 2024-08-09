package com.mcac.ariacashless.ui.barcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mcac.ariacashless.R
import com.mcac.ariacashless.databinding.FragmentEpayBinding

class EPayFragment: Fragment() {

    private var _binding: FragmentEpayBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        binding.wvEpay.settings.javaScriptEnabled = true
        binding.wvEpay.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.wvEpay.settings.domStorageEnabled = true
        binding.wvEpay.webViewClient = WebViewClient()

        arguments?.getString("url")?.let {
            if (URLUtil.isValidUrl(it)){
                binding.wvEpay.loadUrl(it)
            }
            else{
                navController.navigate(R.id.nav_failure_fragment)
            }
        }

    }
}