package com.mcac.ariacashless.ui.success

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcac.devices.DeviceManager
import com.mcac.devices.enums.Status
import com.mcac.devices.ks8123.util.ImageUtils
import com.mcac.devices.models.PrinterCommand
import com.mcac.devices.models.SZZTPrinterSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuccessViewModel(private val deviceManager: DeviceManager):ViewModel() {

    fun getDevice(): DeviceManager {
        return deviceManager
    }

    fun printMsg(messagePrint: String?,bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO){
            if(deviceManager.getPrinter().open().status == Status.SUCCESS){
                val s1 = PrinterCommand().apply {
                    image = ImageUtils.bitmapToBase64(bitmap)
                    setting = SZZTPrinterSetting(23, 1)
                }
                if(deviceManager.getPrinter().printImage(s1).status == Status.SUCCESS){
                    deviceManager.getPrinter().print(PrinterCommand().apply {
                        text = "$messagePrint-----------------------------------\n"
                        setting = SZZTPrinterSetting(23, 1)
                    })
                }

            }
            deviceManager.getPrinter().destroy()
        }
    }

    fun printImage(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO){
            if(deviceManager.getPrinter().open().status == Status.SUCCESS){
                deviceManager.getPrinter().printImage(PrinterCommand().apply {
                    image = ImageUtils.bitmapToBase64(bitmap)
                    setting = SZZTPrinterSetting(23, 1)
                })
            }
            deviceManager.getPrinter().destroy()
        }
    }

}