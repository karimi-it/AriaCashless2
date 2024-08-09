package com.mcac.devices

import com.mcac.devices.hardwares.*

interface DeviceManager {
    suspend fun getIcCard(): ISmartCardReader
    suspend fun getPrinter(): IPrinter
    suspend fun getBarcodeScanner():IBarcodeScanner
    suspend fun getPinpad():IPinpad
    suspend fun getCamera():ICamera
    suspend fun getFingerPrint():IFingerPrint
    suspend fun getMagneticCardReader():IMagneticCardReader
    suspend fun getContactLessReader():IContactLessReader
}