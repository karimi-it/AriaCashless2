package com.mcac.ariacashless.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mcac.ariacashless.R
import com.mcac.ariacashless.databinding.FragmentMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {
    val viewModel: MainViewModel by viewModel()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        _binding?.btnGetBalance?.setOnClickListener {
            navController.navigate(R.id.action_main_fragment_to_get_balance_fragment)
        }
        _binding?.btnCardToCard?.setOnClickListener {
            navController.navigate(R.id.action_main_fragment_to_card_validate_fragment)
        }
        _binding?.btnWithdraw?.setOnClickListener {
            navController.navigate(R.id.action_main_fragment_to_card_ctoc_fragment)
        }
        _binding?.btnBillPaid?.setOnClickListener {
            navController.navigate(R.id.nav_barcode_fragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}