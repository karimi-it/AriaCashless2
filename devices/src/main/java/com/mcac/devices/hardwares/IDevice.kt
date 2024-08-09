package com.mcac.devices.hardwares

interface IDevice {
    suspend fun getIcCardReader(): ISmartCardReader
    suspend fun getPrinter(): IPrinter
    suspend fun getBarcodeScanner(): IBarcodeScanner
    suspend fun getPinpad(): IPinpad
    suspend fun getFingerPrint(): IFingerPrint
    suspend fun getCamera(): ICamera
    suspend fun getMagneticCardReader(): IMagneticCardReader
    suspend fun getContactLessReader(): IContactLessReader

}