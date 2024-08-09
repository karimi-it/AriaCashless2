package com.mcac.devices.ks8123

import android.content.Context
import com.eyecool.fp.util.USBUtil
import com.mcac.devices.hardwares.IDevice
import com.mcac.devices.hardwares.*
import com.szzt.sdk.device.Device
import com.szzt.sdk.device.DeviceManager
import com.szzt.sdk.device.UsbCamera.UsbCamera
import com.szzt.sdk.device.barcode.CameraScan
import com.szzt.sdk.device.card.ContactlessCardReader
import com.szzt.sdk.device.card.MagneticStripeCardReader
import com.szzt.sdk.device.card.SmartCardReader
import com.szzt.sdk.device.pinpad.PinPad
import com.szzt.sdk.device.printer.Printer

class SZZTKS8123 constructor(val context:Context): IDevice{

    var deviceManager: DeviceManager = DeviceManager.createInstance(context)


    companion object{
        const val DEVICE_MODEL = "KS8123"
    }

    init {
        deviceManager.start(object:DeviceManager.DeviceManagerListener{
            override fun deviceEventNotify(p0: Device?, p1: Int): Int {
                return p1
            }

            override fun serviceEventNotify(p0: Int): Int {
                return p0
            }

        } )
        deviceManager.systemManager
    }

    override suspend fun getIcCardReader(): ISmartCardReader {
//        return IcCardImpl.getInstance(deviceManager.getDeviceByType(Device.TYPE_SMARTCARDREADER)[0] as SmartCardReader)
        return SmartCardReaderImpl.getInstance(deviceManager.getDeviceByType(Device.TYPE_SMARTCARDREADER)[0] as SmartCardReader)
    }

    override suspend fun getPrinter(): IPrinter {
        return PrinterImpl.getInstance(deviceManager.getDeviceByType(Device.TYPE_PRINTER)[0] as Printer)
    }

    override suspend fun getBarcodeScanner(): IBarcodeScanner {
       return BarcodeScannerImpl.getInstance(deviceManager.getDeviceByType(Device.TYPE_CAMERA_SCAN)[0] as CameraScan)
    }

    override suspend fun getPinpad(): IPinpad {
        return PinpadImpl.getInstance(deviceManager.getDeviceByType(Device.TYPE_PINPAD)[0] as PinPad)
    }

    override suspend fun getFingerPrint(): IFingerPrint {
        //val mxMscBigFingerApi = fingerFactory.api
//        val mxFingerAlg = fingerFactory.alg
//        val i: Int = FingerLiveApi.initAlg("")
//        Log.d(TAG,"ver=========${deviceManager.sdkVersion}")
        return FingerPrintImp.getInstance(USBUtil.getInstance(context))
    }

    override suspend fun getCamera(): ICamera {
        val idCard: Array<Device> = deviceManager.getDeviceByType(Device.TYPE_USBCAMERA)
        if (idCard != null) {
             return CameraImpl.getInstance(idCard[0] as UsbCamera)
        }
        throw Exception()
//        (deviceManager.getDeviceByType(Device.TYPE_SERIALPORT)[0] as UsbDeviceConnection).
    }

    override suspend fun getMagneticCardReader(): IMagneticCardReader {
        return MagneticCardReaderImpl.getInstance(deviceManager.getDeviceByType(Device.TYPE_MAGSTRIPECARDREADER)[0] as MagneticStripeCardReader)
    }

    override suspend fun getContactLessReader(): IContactLessReader {
        return ContactLessReaderImpl.getInstance(deviceManager.getDeviceByType(Device.TYPE_CONTACTLESSCARDREADER)[0] as ContactlessCardReader)
    }

}
