package com.mcac.ariacashless.ui.barcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mcac.ariacashless.R
import com.mcac.ariacashless.databinding.FragmentBarcodeBinding
import com.mcac.common.utils.Event
import org.koin.androidx.viewmodel.ext.android.viewModel

class BarcodeFragment: Fragment() {

    private val viewModel: BarcodeViewModel by viewModel()
    private var _binding: FragmentBarcodeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarcodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

            navController = view.findNavController()
            binding.btnScan.setOnClickListener {
                viewModel.scanBarcode()
            }
            binding.btnCancel.setOnClickListener {
                navController.popBackStack(R.id.nav_main_fragment, false)
            }

        viewModel.onScan.observe(viewLifecycleOwner,onScanObserver)
    }

    private val onScanObserver:Observer<Event<String>> = Observer {
        if (!it.hasBeenHandled){
            navController.navigate(R.id.nav_epay_fragment,Bundle().apply {
                this.putString("url",it.getContentIfNotHandled())
            })
        }
    }
}