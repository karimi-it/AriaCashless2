package com.mcac.devices

import com.mcac.devices.hardwares.*


class DeviceManagerImpl constructor(private val device: IDevice): DeviceManager {

    override suspend fun getIcCard(): ISmartCardReader {
        return device.getIcCardReader()
    }

    override suspend fun getPrinter(): IPrinter {
        return device.getPrinter()
    }

    override suspend fun getBarcodeScanner(): IBarcodeScanner {
        return device.getBarcodeScanner()
    }

    override suspend fun getPinpad(): IPinpad {
        return device.getPinpad()
    }

    override suspend fun getCamera(): ICamera {
        return device.getCamera()
    }

    override suspend fun getFingerPrint(): IFingerPrint {
        return device.getFingerPrint()
    }

    override suspend fun getMagneticCardReader(): IMagneticCardReader {
        return device.getMagneticCardReader()
    }

    override suspend fun getContactLessReader(): IContactLessReader {
        return device.getContactLessReader()
    }
}