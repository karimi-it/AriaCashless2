package com.mcac.ariacashless.ui.splash

import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateInterpolator
import android.view.animation.BounceInterpolator
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mcac.ariacashless.R
import com.mcac.ariacashless.databinding.FragmentSplashBinding
import com.mcac.common.utils.AnimationUtil

class SplashFragment :Fragment() {

    private var _binding : FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        navController = view.findNavController()
        AnimatorSet().apply {
            playSequentially(
                AnimationUtil.fadeIn(0f,1f,
                    _binding?.imgLogo!!,
                    0,
                    500,
                    AnticipateInterpolator()
                ),
                AnimationUtil.sideBottom(
                    _binding?.tvMabna!!,
                    0,
                    1000,
                    FastOutSlowInInterpolator()
                ),
                AnimationUtil.sideBottom(
                    _binding?.imgMabna!!,
                    0,
                    1000,
                    BounceInterpolator(),
                    object : AnimationUtil.Listener {
                        override fun onEnd() {
                            super.onEnd()
                            navController.navigate(R.id.action_splash_fragment_to_main_fragment)
//                            navController.navigate(
//                                R.id.nav_main_fragment/*, null,
//                                NavOptions.Builder().setPopUpTo(R.id.nav_host_fragment, true).build()*/
//                            )
                        }
                    })
            )
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}