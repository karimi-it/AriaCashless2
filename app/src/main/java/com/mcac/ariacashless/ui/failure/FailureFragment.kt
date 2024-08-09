package com.mcac.ariacashless.ui.failure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mcac.ariacashless.R
import com.mcac.ariacashless.databinding.FragmentFailureBinding
import com.mcac.common.models.Action
import com.mcac.common.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FailureFragment : Fragment() {

    val viewModel: FailureViewModel by viewModel()
    private var _binding: FragmentFailureBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private var action: Action? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        action = arguments?.getSerializable("action") as Action?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFailureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        action?.let {
            setState(action!!)
            lifecycleScope.launch(Dispatchers.Main) {
                delay(action!!.delay)
                navController.popBackStack(action!!.distId, false)
            }
        }
    }

    private fun setState(action: Action) {
        if (action.actionIconId != 0)
            _binding?.imgFailure?.setImageDrawable(resources.getDrawable(action.actionIconId,null))
        if (action.message.isNotEmpty())
            _binding?.tvFailMsg?.text = action.message
        if (action.messagePrint.isNotEmpty())
            viewModel.printMessage(action.messagePrint)
    }

}