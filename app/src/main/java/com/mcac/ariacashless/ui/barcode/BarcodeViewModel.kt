package com.mcac.ariacashless.ui.barcode

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcac.common.utils.Event
import com.mcac.devices.DeviceManager
import com.mcac.devices.hardwares.IBarcodeScanner
import com.mcac.devices.models.BarcodeCommand
import com.mcac.devices.models.BarcodeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets


class BarcodeViewModel(private val deviceManager: DeviceManager):ViewModel(),
    IBarcodeScanner.BarcodeListener {

    private val mOnScan = MutableLiveData<Event<String>>()
    var onScan:LiveData<Event<String>> = mOnScan

    init{
        viewModelScope.launch {
            deviceManager.getBarcodeScanner().open()
        }
    }
    fun scanBarcode(){
        viewModelScope.launch {
            val result = deviceManager.getBarcodeScanner().scan(BarcodeCommand(15000))
           // val response = GsonBuilder().create().fromJson(result.obj.toString(),BarcodeResponse::class.java)
            val response = result.obj as BarcodeResponse
            mOnScan.value = Event(response.data)
        }
    }

    override fun onScanSuccess(data: ByteArray) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = String(data, StandardCharsets.UTF_8)
            mOnScan.value = Event(result)
        }

    }

    override fun onFail(code: Int, message: String) {
        TODO("Not yet implemented")
    }
}