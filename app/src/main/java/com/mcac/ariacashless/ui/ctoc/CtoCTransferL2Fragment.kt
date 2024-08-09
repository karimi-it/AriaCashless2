package com.mcac.ariacashless.ui.ctoc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.text.InputType
import com.mcac.ariacashless.R
import com.mcac.ariacashless.databinding.FragmentCtocL2TransferBinding
import com.mcac.common.models.Action
import com.mcac.common.models.CardToCardResponse

import com.mcac.common.utils.Event
import com.mcac.common.utils.printFormatCardToCard
import com.mcac.common.utils.printFormatGetBalance


class CtoCTransferL2Fragment: Fragment() {

    val viewModel: CtoCTransferViewModel by viewModel()
    private var _binding: FragmentCtocL2TransferBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCtocL2TransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            navController = view.findNavController()

        _binding?.btnConfirm?.setOnClickListener {
            val action = Action().apply {
                message = "تراکنش موفق"
                delay = 3000
                distId = R.id.nav_main_fragment
            }
            navController.navigate(R.id.action_to_success_fragment,Bundle().apply { putSerializable("action",action) })
        }
        _binding?.btnCancel?.setOnClickListener {
            navController.popBackStack(R.id.nav_main_fragment, false)
        }
        _binding?.etSecondPassword?.inputType = InputType.TYPE_NULL
        viewModel.onKeyEvent.observe(viewLifecycleOwner,onKeyEventObserver)
    }

    private val onKeyEventObserver:Observer<Event<String>> = Observer {
        if (!it.hasBeenHandled){
            val lText = binding.etSecondPassword.text.toString()+ it.getContentIfNotHandled()
            _binding?.etSecondPassword?.setText("*".repeat(lText.length))
        }
    }


}