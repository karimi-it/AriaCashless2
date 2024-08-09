package com.mcac.ariacashless.ui.ctoc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcac.common.utils.Event
import com.mcac.devices.DeviceManager
import com.mcac.devices.hardwares.IPinpad
import kotlinx.coroutines.launch

class CtoCTransferViewModel(private val deviceManager: DeviceManager):ViewModel(),
    IPinpad.PinpadListener {

    private val mOnKeyEvent = MutableLiveData<Event<String>>()
    var onKeyEvent:LiveData<Event<String>> = mOnKeyEvent


    init {
        viewModelScope.launch {
            deviceManager.getPinpad().open(this@CtoCTransferViewModel);
        }
    }
    override fun onKey(key: Int) {
        val char:Char = key.toChar()
        mOnKeyEvent.postValue(Event(char.toString()))
    }

    override fun onFail(code: Int, msg: String) {
        TODO("Not yet implemented")
    }
}