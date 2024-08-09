package com.mcac.common.utils

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.*
import androidx.core.animation.addListener

object AnimationUtil {

    fun fadeIn(
        startAlpha:Float = 0f,
        endAlpha:Float = 1f,
        view: View,
        delay: Long = 0,
        duration: Long = 500,
        interpolator: Interpolator = LinearInterpolator(),
        listener: Listener = object : Listener {}
    ):ObjectAnimator {
        view.visibility = View.INVISIBLE
        val fadeIn = ObjectAnimator.ofFloat(view, View.ALPHA, startAlpha, endAlpha)
        fadeIn.startDelay = delay
        fadeIn.duration = duration
        fadeIn.addListener(onStart = {
            view.visibility = View.VISIBLE
            listener.onStart()
        }, onEnd = {
            listener.onEnd()
        }, onCancel = {
            listener.onCancel()
        }, onRepeat = {
            listener.onRepeat()
        })
        return fadeIn
    }

    fun fadeOut(
        startAlpha:Float = 0f,
        endAlpha:Float = 1f,
        view: View,
        delay: Long = 0,
        duration: Long = 500,
        interpolator: Interpolator = LinearInterpolator(),
        listener: Listener = object : Listener {}
    ):ObjectAnimator {
        val fadeOut = ObjectAnimator.ofFloat(view, View.ALPHA, startAlpha, endAlpha)
        fadeOut.startDelay = delay
        fadeOut.duration = duration
        fadeOut.addListener(onStart = {
            listener.onStart()
        }, onEnd = {
            view.visibility = View.GONE
            listener.onEnd()
        }, onCancel = {
            listener.onCancel()
        }, onRepeat = {
            listener.onRepeat()
        })
        return fadeOut
    }

    fun sideBottom(
        view: View,
        delay: Long = 0,
        duration: Long = 500,
        interpolator: Interpolator = LinearInterpolator(),
        listener: Listener = object : Listener {}
    ):ObjectAnimator {
        view.visibility = View.INVISIBLE
        val sideBottom = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 300f, 0f)
        sideBottom.startDelay = delay
        sideBottom.duration = duration
        sideBottom.interpolator = interpolator
        sideBottom.addListener(onStart = {
            view.visibility = View.VISIBLE
            listener.onStart()
        }, onEnd = {
            listener.onEnd()
        }, onCancel = {
            listener.onCancel()
        }, onRepeat = {
            listener.onRepeat()
        })
        return sideBottom
    }

    interface Listener {
        fun onStart() {

        }

        fun onEnd() {

        }

        fun onCancel() {

        }

        fun onRepeat() {

        }
    }

    fun scaleX(
        start:Float = 0f,
        end:Float = 1f,
        view: View,
        delay: Long = 0,
        duration: Long = 500,
        interpolator: Interpolator = LinearInterpolator(),
        listener: Listener = object : Listener {}
    ):ObjectAnimator {
        view.visibility = View.INVISIBLE
        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, start, end)
        scaleX.startDelay = delay
        scaleX.addListener(onStart = {
            view.visibility = View.VISIBLE
            listener.onStart()
        }, onEnd = {
            listener.onEnd()
        }, onCancel = {
            listener.onCancel()
        }, onRepeat = {
            listener.onRepeat()
        })
        return scaleX
    }

    fun scaleY(
        start:Float = 0f,
        end:Float = 1f,
        view: View,
        delay: Long = 0,
        duration: Long = 500,
        interpolator: Interpolator = LinearInterpolator(),
        listener: Listener = object : Listener {}
    ):ObjectAnimator {
        view.visibility = View.INVISIBLE
        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, start, end)
        scaleY.startDelay = delay
        scaleY.addListener(onStart = {
            view.visibility = View.VISIBLE
            listener.onStart()
        }, onEnd = {
            listener.onEnd()
        }, onCancel = {
            listener.onCancel()
        }, onRepeat = {
            listener.onRepeat()
        })
        return scaleY
    }
}
