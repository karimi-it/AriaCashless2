package com.mcac.devices.ks8123

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.usb.UsbDevice
import com.eyecool.fp.client.Callback
import com.eyecool.fp.client.TcFingerClient
import com.eyecool.fp.entity.FingerResult
import com.eyecool.fp.util.*
import com.mcac.devices.enums.CommandType
import com.mcac.devices.enums.DeviceType
import com.mcac.devices.enums.Status
import com.mcac.devices.hardwares.IFingerPrint
import com.mcac.devices.ks8123.util.ImageUtils
import com.mcac.devices.models.Container
import com.mcac.devices.models.FingerPrintCommand
import com.mcac.devices.models.FingerPrintResponse
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FingerPrintImp private constructor(private val usbUtil: USBUtil) :
    IFingerPrint {

    var fingerPrint :TcFingerClient? = null
    companion object {
        var Instance: FingerPrintImp? = null

        fun getInstance(fingerPrint: USBUtil): FingerPrintImp {
            if (Instance == null) {
                synchronized(this) {
                    Instance = FingerPrintImp(fingerPrint)
                }
            }
            return Instance!!
        }
    }

    override suspend fun open(): Container = suspendCoroutine {
        usbUtil.setProtocol(USBUtil.Protocol.SIMPLE)
        usbUtil.registerMonitor()
        usbUtil.setConnectListener(object : OnUsbConnectListener() {
            override fun onConnected(client: TcFingerClient) {
                fingerPrint = client
                it.resume(Container(DeviceType.FINGER_PRINT, CommandType.OPEN, Status.SUCCESS))
            }

            override fun onConnectFailed(code: Int, msg: String) {
                it.resume(Container(DeviceType.FINGER_PRINT, CommandType.OPEN, Status.FAIL))
            }

            override fun onDisconnected() {
                super.onDisconnected()
            }

            override fun onAttach(usbDevice: UsbDevice) {
                // TODO Auto-generated method stub
                super.onAttach(usbDevice)
            }

            override fun onDettach(usbDevice: UsbDevice) {
                // TODO Auto-generated method stub
                super.onDettach(usbDevice)
            }
        })
        usbUtil.connectDevice()

    }
        override suspend fun status(): Container {
        return if (fingerPrint != null)
            Container(DeviceType.FINGER_PRINT, CommandType.STATUS, Status.SUCCESS)
        else
            Container(DeviceType.FINGER_PRINT, CommandType.STATUS, Status.FAIL)
    }

    override suspend fun captureFingerImageBig(command: FingerPrintCommand): Container  = suspendCoroutine{
        FpConfig.setFeatureType(FpConfig.FEATURE_INTERNATIONAL_TC_ISO_19794_2_2011)
        FpConfig.setImageType(FpConfig.IMAGE_BIG)
        FpConfig.setQualityScore(30)
        var bitmap:Bitmap? = null
        fingerPrint?.tcGetImage(command.timeOut.toInt(), object : Callback() {
            override fun onSuccess(result: FingerResult) {
                val feature = result.imgBytes
                if (feature != null) {
                    val options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ALPHA_8
                    bitmap = BitmapFactory.decodeByteArray(
                        feature, 0,
                        feature.size, options
                    )
                    it.resume( Container(DeviceType.FINGER_PRINT, CommandType.FINGER_PRINT_CAPTURE_IMAGE_BIG,Status.SUCCESS,
                        FingerPrintResponse().apply {
                        width = bitmap!!.width
                        height = bitmap!!.height
                        captureData = ImageUtils.bitmapToBase64(bitmap!!)
                    }))
                }
                else{
                    it.resume(Container(DeviceType.FINGER_PRINT, CommandType.FINGER_PRINT_CAPTURE_IMAGE_BIG, Status.FAIL))
                }

        }
            override fun onError(errorCode: Int) {
               it.resume(Container(DeviceType.FINGER_PRINT, CommandType.FINGER_PRINT_CAPTURE_IMAGE_BIG, Status.FAIL))
            }
        })
    }

    override suspend fun captureFingerImage152(command: FingerPrintCommand): Container  = suspendCoroutine{
        FpConfig.setFeatureType(FpConfig.FEATURE_INTERNATIONAL_TC_ISO_19794_2_2011)
        FpConfig.setImageType(FpConfig.IMAGE_SMALL)
        FpConfig.setQualityScore(30)
        var bitmap:Bitmap? = null
        fingerPrint?.tcGetImage(30000, object : Callback() {
            override fun onSuccess(result: FingerResult) {
                val feature = result.imgBytes
                if (feature != null) {
                    val options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ALPHA_8
                    bitmap = BitmapFactory.decodeByteArray(
                        feature, 0,
                        feature.size, options
                    )
                    it.resume( Container(DeviceType.FINGER_PRINT, CommandType.FINGER_PRINT_CAPTURE_IMAGE_152,Status.SUCCESS,FingerPrintResponse().apply {
                        width = bitmap!!.width
                        height = bitmap!!.height
                        captureData = ImageUtils.bitmapToBase64(bitmap!!)
                    }))
                }
                else{
                    it.resume(Container(DeviceType.FINGER_PRINT, CommandType.FINGER_PRINT_CAPTURE_IMAGE_152, Status.FAIL))
                }

            }
            override fun onError(errorCode: Int) {
                it.resume(Container(DeviceType.FINGER_PRINT, CommandType.FINGER_PRINT_CAPTURE_IMAGE_152, Status.FAIL))
            }
        })
    }

    override suspend fun reset(): Container {
        return if (destroy().status == Status.SUCCESS && open().status == Status.SUCCESS)
            Container(DeviceType.FINGER_PRINT, CommandType.RESET, Status.SUCCESS)
        else
            Container(DeviceType.FINGER_PRINT, CommandType.RESET, Status.FAIL)
    }

    override suspend fun destroy(): Container {
        usbUtil.unRegisterMonitor()
        usbUtil.asynDisconnectDevice()
        return Container(DeviceType.FINGER_PRINT, CommandType.CLOSE, Status.SUCCESS)
    }
}