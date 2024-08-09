package com.mcac.arianow.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcac.arianow.data.CitizenInfo
import com.mcac.arianow.data.Repository
import com.mcac.arianow.data.models.Container
import com.mcac.arianow.device.DeviceManager
import com.mcac.arianow.device.hardwares.ISmartCardReader
import com.mcac.arianow.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(private val repository: Repository, val deviceManager: DeviceManager) :
    ViewModel(){
    var iCitizenInfo: CitizenInfo? = null
    var isRegistered = false
    private val mCitizenInfo = MutableLiveData<CitizenInfo?>()
    var citizenInfo: MutableLiveData<CitizenInfo?> = mCitizenInfo

    private val mCardStatus = MutableLiveData<Event<ISmartCardReader.IcCardStatus>>()
    var lCardReaderStatus: LiveData<Event<ISmartCardReader.IcCardStatus>> = mCardStatus

    private val mContainer = MutableLiveData<Event<Container>>()
    var container: LiveData<Event<Container>> = mContainer

    fun setCitizenInfo(citizenInfo: CitizenInfo?) {
        if (!citizenInfo?.name.isNullOrEmpty()) {
            isRegistered = true
            this.iCitizenInfo = citizenInfo
        } else {
            isRegistered = false
        }
        mCitizenInfo.postValue(citizenInfo)
    }

    fun send(container: Container) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.send(container)
        }
    }
    fun onReceive(container: Container) {
        viewModelScope.launch(Dispatchers.Main) {
            mContainer.value = Event(container)
        }
    }
}