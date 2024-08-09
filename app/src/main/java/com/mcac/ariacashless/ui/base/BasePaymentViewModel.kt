package com.mcac.ariacashless.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mcac.ariacashless.R
import com.mcac.common.models.Action
import com.mcac.common.models.BalanceRequest
import com.mcac.common.utils.EncryptionUtil
import com.mcac.common.utils.Event
import com.mcac.data.repository.Repository
import com.mcac.devices.DeviceManager
import com.mcac.devices.enums.Status
import com.mcac.devices.hardwares.IPinpad
import com.mcac.devices.models.Container
import com.mcac.devices.models.ErrorResponse
import com.mcac.devices.models.MagnetCardCommand
import com.mcac.devices.models.MagnetCardResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

open class BasePaymentViewModel constructor(
    private val repository: Repository,
    private val deviceManager: DeviceManager,
    private val encryptionUtil: EncryptionUtil
) : BaseViewModel(), IPinpad.PinpadListener {

    val TIME_OUT = 15000
    val ERROR_CODE_TIME_OUT = -106

    protected val mOnReadMagnetFail = MutableLiveData<Event<Unit>>()
    var onReadMagnetFail: LiveData<Event<Unit>> = mOnReadMagnetFail
    protected val mOnPinKey = MutableLiveData<Event<Char>>()
    var onPinKey: LiveData<Event<Char>> = mOnPinKey
    protected val mOnActionFail = MutableLiveData<Event<Action>>()
    var onActionFail: LiveData<Event<Action>> = mOnActionFail
    protected val mOnActionSuccess = MutableLiveData<Event<Action>>()
    var onActionSuccess: LiveData<Event<Action>> = mOnActionSuccess

    fun startMagnetReader() {
        viewModelScope.launch(Dispatchers.IO) {
            openMagnetReader(deviceManager.getMagneticCardReader().open())
        }
    }

    open fun showMessage(message: String) {
        mShowMessage.postValue(Event(message))
    }

    open suspend fun openMagnetReader(container: Container) {
        if (container.status == Status.SUCCESS) {
            listenForCard(
                deviceManager.getMagneticCardReader().listenerForCard(MagnetCardCommand(TIME_OUT))
            )
        } else {
            mOnActionFail.postValue(Event(Action().apply {
                message = "ارتباط با دستگاه برقرار نشد."
                delay = 3000
                distId = R.id.nav_main_fragment
            }))
        }
    }

    open suspend fun listenForCard(container: Container) {
        if (container.status == Status.SUCCESS)
            readTracks(container.obj as MagnetCardResponse)
        else if (container.status == Status.FAIL)
            readFailHandler(container.obj as ErrorResponse)
        else
            readException(container.obj as ErrorResponse)

        destroyMagnetReader()
    }

    open suspend fun readException(error: ErrorResponse) {
        mOnActionFail.postValue(Event(Action().apply {
            message = "خطای عمومی."
            delay = 3000
            distId = R.id.nav_main_fragment
        }))
    }

    open suspend fun readFailHandler(error: ErrorResponse) {
        if (error.code == ERROR_CODE_TIME_OUT) {
            mOnActionFail.postValue(Event(Action().apply {
                message = "عدم دریافت پاسخ از دستگاه"
                delay = 3000
                distId = R.id.nav_main_fragment
            }))
        } else {
            mOnActionFail.postValue(Event(Action().apply {
                message = "کارت نا معتبر است یا خواندن کارت با خطا مواجه شده است."
                delay = 3000
                distId = R.id.nav_main_fragment
            }))
        }
    }


    open fun destroyMagnetReader() {
        viewModelScope.launch(Dispatchers.IO) {
            deviceManager.getMagneticCardReader().destroy()
        }
    }

    open suspend fun readTracks(magnetCardResponse: MagnetCardResponse) {

    }

    open suspend fun getCardPin() {
        viewModelScope.launch(Dispatchers.IO) {
            openPinpad(deviceManager.getPinpad().open(this@BasePaymentViewModel))
        }
    }

    open fun openPinpad(container: Container) {
        if (container.status == Status.SUCCESS) {

        }
    }


    open fun pinAccept(pin: String) {

    }

    open fun handelException(exception: Throwable?) {
        mShowProgressView.postValue(Event(false))
        if (exception is SocketTimeoutException) {
            mOnActionFail.postValue(Event(Action().apply {
                message = "هیچ پاسخی از صادر کننده دریافت نشد."
                delay = 3000
                distId = R.id.nav_main_fragment
            }))
        }
    }

    open fun destroyPinpad() {
        viewModelScope.launch(Dispatchers.IO) {
            deviceManager.getPinpad().destroy()
        }
    }

    open fun onClear() {
        super.onCleared()
        destroyMagnetReader()
        destroyPinpad()
    }

    override fun onKey(key: Int) {
        mOnPinKey.postValue(Event(Char(key)))
    }

    override fun onFail(code: Int, msg: String) {

    }
}