package com.mcac.ariacashless.ui.ctoc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mcac.ariacashless.R
import com.mcac.ariacashless.databinding.FragmentCtocL1TransferBinding
import com.mcac.common.utils.CreditCardTextWatcher
import com.mcac.common.utils.NumberTextWatcherForThousand
import org.koin.androidx.viewmodel.ext.android.viewModel


class CtoCTransferL1Fragment: Fragment() {

    val viewModel: CtoCTransferViewModel by viewModel()
    private var _binding: FragmentCtocL1TransferBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
//    private lateinit var citizenInfo: CitizenInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.getSerializable("citizenInfo")?.let {
//            citizenInfo = it as CitizenInfo
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCtocL1TransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            navController = view.findNavController()
        _binding?.etAmount?.addTextChangedListener(NumberTextWatcherForThousand(_binding?.etAmount))
//        _binding?.etSourceCardNumber?.addTextChangedListener(CreditCardTextWatcher(_binding?.etSourceCardNumber))
//        _binding?.etTargetCardNumber?.addTextChangedListener(CreditCardTextWatcher(_binding?.etTargetCardNumber))
            binding.btnNext.setOnClickListener {
                navController.navigate(R.id.nav_ctoc_transfer_l2_fragment)
            }
            binding.btnCancel.setOnClickListener {
                navController.popBackStack(R.id.nav_main_fragment, false)
            }

        }

}