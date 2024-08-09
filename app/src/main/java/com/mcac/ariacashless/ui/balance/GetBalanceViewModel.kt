package com.mcac.ariacashless.ui.balance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mcac.ariacashless.R
import com.mcac.ariacashless.ui.base.BasePaymentViewModel
import com.mcac.common.models.Action
import com.mcac.common.models.BalanceRequest
import com.mcac.common.models.BalanceResponse
import com.mcac.common.utils.Constants
import com.mcac.common.utils.EncryptionUtil
import com.mcac.common.utils.Event
import com.mcac.data.repository.Repository
import com.mcac.devices.DeviceManager
import com.mcac.common.utils.printFormatGetBalance
import com.mcac.devices.models.MagnetCardResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class GetBalanceViewModel constructor(private val repository: Repository,
    private val deviceManager: DeviceManager,
  private val encryptionUtil: EncryptionUtil) : BasePaymentViewModel(repository, deviceManager, encryptionUtil) {

    protected val mOnReadMagnetSuccess = MutableLiveData<Event<BalanceRequest>>()
    var onReadMagnetSuccess: LiveData<Event<BalanceRequest>> = mOnReadMagnetSuccess

    var balanceRequest = BalanceRequest().apply {
        trackingNumber = UUID.randomUUID().toString()
        terminalId = Constants.TRMINAL_ID
        lang = Constants.LANGUAGE
        terminalType = Constants.TERMINAL_TYPE
    }
     override suspend fun readTracks(magnetCardResponse: MagnetCardResponse) {
        if (magnetCardResponse.track2.isNotEmpty()) {
            mOnReadMagnetSuccess.postValue(Event(balanceRequest.apply {
                cardNumber = encryptionUtil.encByPublicKey(magnetCardResponse.track2.split("=")[0].drop(1))
                track2 = encryptionUtil.encByPublicKey(magnetCardResponse.track2.drop(1).dropLast(1))
            }))
        }
        else{
            mOnActionFail.postValue(Event(Action().apply {
                message = "خواندن کارت با خطا مواجه شده است."
                delay = 3000
                distId = R.id.nav_main_fragment
            }))
        }
    }

    override fun pinAccept(pin: String) {
        destroyPinpad()
        balanceRequest.apply {
            pin1 = encryptionUtil.encByPublicKey(pin)
        }
        mShowProgressView.postValue(Event(true))
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getBalance(balanceRequest)
            if (result.isSuccess){
                handelCardValidateViewState(result.getOrNull())
            }else{
                handelException(result.exceptionOrNull())
            }
        }

    }
    private fun handelCardValidateViewState(response: BalanceResponse?) {
        mShowProgressView.postValue(Event(false))
        if(response?.response == "00"){
            mOnActionSuccess.postValue(Event(Action().apply {
                message = response.message
                messagePrint = printFormatGetBalance(response)
                delay = 5000
                distId = R.id.nav_main_fragment
            }))
        }
        else{
            mOnActionFail.postValue(Event(Action().apply {
                message = response?.message.toString()
                delay = 3000
                distId = R.id.nav_main_fragment
            }))
        }
    }


}