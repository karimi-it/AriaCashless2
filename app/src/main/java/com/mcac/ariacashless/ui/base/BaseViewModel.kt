package com.mcac.ariacashless.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcac.common.utils.Event

open class BaseViewModel:ViewModel() {

    protected val mShowMessage = MutableLiveData<Event<String>>()
    var onShowMessage: LiveData<Event<String>> = mShowMessage
    protected val mShowProgressView = MutableLiveData<Event<Boolean>>()
    var showProgressView: LiveData<Event<Boolean>> = mShowProgressView
}