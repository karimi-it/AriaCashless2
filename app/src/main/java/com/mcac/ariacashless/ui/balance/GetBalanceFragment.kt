package com.mcac.ariacashless.ui.balance

import android.animation.AnimatorSet
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mcac.ariacashless.R
import com.mcac.ariacashless.databinding.FragmentGetBalanceBinding
import com.mcac.ariacashless.ui.dialog.ProgressDialog
import com.mcac.common.models.Action
import com.mcac.common.models.BalanceRequest
import com.mcac.common.utils.AnimationUtil
import com.mcac.common.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class GetBalanceFragment : Fragment() {

    val viewModel: GetBalanceViewModel by viewModel()
    var progressDialog: ProgressDialog?= null
    private var _binding: FragmentGetBalanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    var timer: CountDownTimer? = null
    private val Number0Char = 48
    private val Number9Char = 57
    private val AcceptChar = 13
    private val BackSpaceChar = 18
    private val CancelChar = 12
    private val etBilder :StringBuilder = StringBuilder()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGetBalanceBinding.inflate(inflater, container, false)
        progressDialog = ProgressDialog(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        viewModel.startMagnetReader()
        _binding?.btnCancel?.setOnClickListener {
            navController.popBackStack(R.id.nav_main_fragment,false)
        }
        timer = object: CountDownTimer(30000, 1000){

            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000

                AnimatorSet().apply {
                    playTogether(
                        AnimationUtil.scaleX(0.4f,1f,binding.llTimer,
                        0,
                        1000,LinearInterpolator()),
                        AnimationUtil.scaleY(0.4f,1f,binding.llTimer,
                            0,
                            1000,LinearInterpolator()),
                        AnimationUtil.fadeIn(
                            0.3f,0.7f,
                            binding.llTimer,
                            0,
                            1000,
                            LinearInterpolator(),
                            object : com.mcac.common.utils.AnimationUtil.Listener {
                                override fun onStart() {
                                    _binding?.tvTimer?.text = "${String.format("%02d", time)} "
                                }

                            })
                    )
                    start()
                }


            }

            override fun onFinish() {
               val failAction =  Action().apply {
                    message = "عدم دریافت پاسخ از دستگاه"
                    delay = 3000
                    distId = R.id.nav_main_fragment
                }
                navController.navigate(R.id.action_to_failure_fragment,Bundle().apply { putSerializable("action",failAction) })
            }
        }
        viewModel.onShowMessage.observe(viewLifecycleOwner, showMessageObserver)
        viewModel.showProgressView.observe(viewLifecycleOwner, showProgressViewObserver)
        viewModel.onReadMagnetSuccess.observe(viewLifecycleOwner, onReadMagnetSuccessObserver)
        viewModel.onPinKey.observe(viewLifecycleOwner, onPinKeyObserver)
        viewModel.onActionFail.observe(viewLifecycleOwner, onActionFailObserver)
        viewModel.onActionSuccess.observe(viewLifecycleOwner, onActionSuccessObserver)

    }
    private val onActionFailObserver: Observer<Event<Action>> = Observer {
        if (!it.hasBeenHandled) {
            navController.navigate(R.id.action_to_failure_fragment,Bundle().apply { putSerializable("action",it.peekContent()) })
        }
    }
    private val onActionSuccessObserver: Observer<Event<Action>> = Observer {
        if (!it.hasBeenHandled) {
            navController.navigate(R.id.action_to_success_fragment,Bundle().apply { putSerializable("action",it.peekContent()) })
        }
    }
    private val showMessageObserver: Observer<Event<String>> = Observer {
        if (!it.hasBeenHandled) {

        }
    }
    private val showProgressViewObserver: Observer<Event<Boolean>> = Observer {
        if (!it.hasBeenHandled) {
            try{
                if(it.peekContent()){
                    progressDialog?.show()
                }
                else {
                    progressDialog?.hide()
                }
            }
            catch (ex:Exception){}

        }
    }
    private val onReadMagnetSuccessObserver: Observer<Event<BalanceRequest>> = Observer {
        if (!it.hasBeenHandled) {
            _binding?.llSwipe?.visibility = View.GONE
            _binding?.llPin?.visibility = View.VISIBLE
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.getCardPin()
            }
            timer?.start()
        }
    }
    private val onPinKeyObserver: Observer<Event<Char>> = Observer {
        var pin = _binding?.etPin?.text.toString()
        if (!it.hasBeenHandled) {
            if (etBilder.toString().length < 4 && it.peekContent().code in Number0Char..Number9Char) {
                etBilder.append(it.peekContent())
                _binding?.etPin?.text?.append("*")
            } else if (etBilder?.toString()?.length == 4 && it.peekContent().code == AcceptChar) {
                timer?.cancel()
                timer = null
                viewModel.pinAccept(etBilder.toString())
            } else if (etBilder?.toString()?.length!! > 0 && it.peekContent().code == BackSpaceChar) {
                _binding?.etPin?.text?.delete(_binding?.etPin?.text?.length!! - 1, _binding?.etPin?.text?.length!!)
                etBilder?.delete(etBilder?.toString()?.length!! - 1, etBilder?.toString()?.length!!)
            } else if (it.peekContent().code == CancelChar) {
                if (etBilder?.toString()?.length!! > 0){
                    _binding?.etPin?.text?.clear()
                    etBilder?.clear()
                }
                else
                    navController.popBackStack()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        progressDialog?.dismiss()
        _binding = null
        timer?.cancel()
        timer = null
        viewModel.onClear()
    }
}