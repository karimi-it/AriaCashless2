package com.mcac.ariacashless.ui.success

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mcac.ariacashless.R
import com.mcac.ariacashless.databinding.FragmentSuccessBinding
import com.mcac.common.models.Action
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SuccessFragment : Fragment() {

    val viewModel: SuccessViewModel by viewModel()
    private var _binding: FragmentSuccessBinding? = null
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
        _binding = FragmentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
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
        val icon: Bitmap = BitmapFactory.decodeResource(
            requireContext().resources,
            R.drawable.recipt_logo
        )
        _binding?.btnPrint?.setOnClickListener {

            viewModel.printMsg(action?.messagePrint,icon)
        }

    }

    private fun setState(action: Action) {
        if (action.actionIconId != 0)
            _binding?.imgSuccess?.setImageDrawable(resources.getDrawable(action.actionIconId, null))
        if (action.message.isNotEmpty())
            _binding?.tvSuccessMsg?.text = action.message
        if (action.messagePrint.isNotEmpty()) {
            _binding?.llPrint?.visibility = View.VISIBLE
            _binding?.tvPrintMsg?.text = action.messagePrint
            _binding?.btnPrint?.visibility = View.VISIBLE

        } else {
            _binding?.tvPrintMsg?.visibility = View.GONE
        }
    }

}