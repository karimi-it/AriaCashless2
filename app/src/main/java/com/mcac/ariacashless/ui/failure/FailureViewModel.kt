package com.mcac.ariacashless.ui.failure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcac.common.models.Action
import com.mcac.common.utils.Event

class FailureViewModel:ViewModel() {

    private val mOnNavControllerEvent = MutableLiveData<Event<Action>>()
    var onNavControllerEvent : LiveData<Event<Action>> = mOnNavControllerEvent
    private val mOnStateEvent = MutableLiveData<Event<Action>>()
    var onStateEvent : LiveData<Event<Action>> = mOnStateEvent
    fun handelAction(action: Action) {

    }

    fun printMessage(messagePrint: String) {
        TODO("Not yet implemented")
    }
}