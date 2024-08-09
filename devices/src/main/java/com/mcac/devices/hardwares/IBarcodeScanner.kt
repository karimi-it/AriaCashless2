package com.mcac.devices.hardwares

import com.mcac.devices.models.BarcodeCommand
import com.mcac.devices.models.Container

interface IBarcodeScanner : IHardwareSupported {

    suspend fun open(): Container
    suspend fun setting(command: BarcodeCommand): Container
    suspend fun scan(command: BarcodeCommand): Container
    suspend fun status(): Container
    suspend fun reset(): Container
    suspend fun destroy(): Container

    interface BarcodeListener {
        fun onScanSuccess(data: ByteArray)
        fun onFail(code: Int, message: String)
    }


}