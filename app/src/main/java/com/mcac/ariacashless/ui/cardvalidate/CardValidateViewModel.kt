package com.mcac.ariacashless.ui.cardvalidate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mcac.ariacashless.R
import com.mcac.ariacashless.ui.base.BasePaymentViewModel
import com.mcac.common.models.*
import com.mcac.common.utils.*
import com.mcac.data.repository.Repository
import com.mcac.devices.DeviceManager
import com.mcac.devices.models.MagnetCardResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CardValidateViewModel constructor(
    private val repository: Repository,
    private val deviceManager: DeviceManager,
    private val encryptionUtil: EncryptionUtil
) : BasePaymentViewModel(repository, deviceManager, encryptionUtil) {

    private val mOnReadMagnetSuccess = MutableLiveData<Event<CardValidateRequest>>()
    var onReadMagnetSuccess: LiveData<Event<CardValidateRequest>> = mOnReadMagnetSuccess
    private val mOnValidateSuccess = MutableLiveData<Event<CardValidateResponse>>()
    var onValidateSuccess: LiveData<Event<CardValidateResponse>> = mOnValidateSuccess
//    private val mOnCardToCardSuccess = MutableLiveData<Event<CardToCardResponse>>()
//    var onCardToCardSuccess: LiveData<Event<CardToCardResponse>> = mOnCardToCardSuccess

    var cardValidateRequest = CardValidateRequest().apply {
        lang = Constants.LANGUAGE
        trackingNumber = UUID.randomUUID().toString()
        terminalId = Constants.TRMINAL_ID
       terminalType = Constants.TERMINAL_TYPE
    }
    var cardToCardRequest = CardToCardRequest().apply {
        lang = Constants.LANGUAGE
        trackingNumber = UUID.randomUUID().toString()
        terminalId = Constants.TRMINAL_ID
        terminalType = Constants.TERMINAL_TYPE
    }
    override suspend fun readTracks(magnetCardResponse: MagnetCardResponse) {
        if (magnetCardResponse.track2.isNotEmpty()) {
            mOnReadMagnetSuccess.postValue(Event(cardValidateRequest.apply {
                cardNumber =
                    encryptionUtil.encByPublicKey(magnetCardResponse.track2.split("=")[0].drop(1))
                track2 =
                    encryptionUtil.encByPublicKey(magnetCardResponse.track2.drop(1).dropLast(1))
            }))
        } else {
            mOnActionFail.postValue(Event(Action().apply {
                message = "خواندن کارت با خطا مواجه شده است."
                delay = 3000
                distId = R.id.nav_main_fragment
            }))
        }
    }

    override fun pinAccept(pin: String) {
        destroyPinpad()
        cardValidateRequest.apply {
            pin1 = encryptionUtil.encByPublicKey(pin)
        }

    }

    fun checkCardValidate(amount: String, distCard: String) {
        cardValidateRequest.apply {
            this.amount = amount
            this.toCardNumber = encryptionUtil.encByPublicKey(distCard)
        }
        mShowProgressView.postValue(Event(true))
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.cardValidate(cardValidateRequest)
            if (result.isSuccess) {
                handelCardValidateViewState(result.getOrNull())
            } else {
                handelException(result.exceptionOrNull())
            }
        }
    }
    private fun handelCardValidateViewState(response: CardValidateResponse?) {
        mShowProgressView.postValue(Event(false))
        if(response?.response == "00"){
            mOnValidateSuccess.postValue(Event(response))
        }
        else{
            mOnActionFail.postValue(Event(Action().apply {
                message = response?.message.toString()
                delay = 3000
                distId = R.id.nav_main_fragment
            }))
        }
    }
    fun acceptCardToCard() {
        val cardToCardRequest = CardToCardRequest().apply {
            mShowProgressView.postValue(Event(true))
            lang = Constants.LANGUAGE
            trackingNumber = UUID.randomUUID().toString()
            terminalId = Constants.TRMINAL_ID
            terminalType = Constants.TERMINAL_TYPE
            amount = cardValidateRequest.amount
            cardNumber = cardValidateRequest.cardNumber
            toCardNumber = cardValidateRequest.toCardNumber
            pin1 = cardValidateRequest.pin1
            track2 = cardValidateRequest.track2
        }

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.cardToCard(cardToCardRequest)
            if (result.isSuccess) {
                handelCardToCardViewState(result.getOrNull())
            } else {
                handelException(result.exceptionOrNull())
            }
        }
    }

    private fun handelCardToCardViewState(response: CardToCardResponse?) {
        mShowProgressView.postValue(Event(false))
        if(response?.response == "00"){
            mOnActionSuccess.postValue(Event(Action().apply {
                message = response.message
                messagePrint = printFormatCardToCard(response)
                delay = 10000
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