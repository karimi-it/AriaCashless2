package com.mcac.devices.ks8123

import android.os.Bundle
import com.mcac.devices.enums.CommandType
import com.mcac.devices.enums.DeviceType
import com.mcac.devices.enums.Status
import com.mcac.devices.hardwares.IBarcodeScanner
import com.mcac.devices.ks8123.util.ErrorList
import com.mcac.devices.ks8123.util.StatusList
import com.mcac.devices.models.BarcodeCommand
import com.mcac.devices.models.BarcodeResponse
import com.mcac.devices.models.Container
import com.szzt.sdk.device.Constants
import com.szzt.sdk.device.barcode.CameraScan
import java.nio.charset.StandardCharsets
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BarcodeScannerImpl(private val cameraScan: CameraScan) : IBarcodeScanner {

    var listener: IBarcodeScanner.BarcodeListener? = null

    companion object {
        var Instance: BarcodeScannerImpl? = null
        fun getInstance(cameraScan: CameraScan): IBarcodeScanner {
            if (Instance == null) {
                synchronized(this) {
                    Instance = BarcodeScannerImpl(cameraScan)
                }
            }
            return Instance!!
        }
    }

    override suspend fun open(): Container {
        return try {
            cameraScan.setConfig(Bundle().apply {
                putInt("type", CameraScan.CarmeraType.TYPE_FRONT_FACING)
            })
            Container(DeviceType.BARCODE_SCANNER, CommandType.OPEN, Status.SUCCESS)
        } catch (e: Exception) {
            Container(DeviceType.BARCODE_SCANNER, CommandType.OPEN, Status.FAIL, ErrorList.getError(-1))
        }
    }

    override suspend fun setting(command: BarcodeCommand): Container {
        return Container(DeviceType.BARCODE_SCANNER, CommandType.SETTING, Status.SUCCESS)
    }

    override suspend fun scan(command: BarcodeCommand): Container = suspendCoroutine {
        cameraScan.scan(command.timeOut,
            CameraScan.CameraListener { resultCode, rowData ->
                if (resultCode >= Constants.Error.OK && rowData != null) {
                    it.resume(
                        Container(
                            DeviceType.BARCODE_SCANNER,
                            CommandType.BARCODE_SCANNER_SCAN,
                            Status.SUCCESS,
                            BarcodeResponse().apply {
                               this.data = String(rowData, StandardCharsets.UTF_8)
                            }
                        )
                    )
                } else {
                    it.resume(
                        Container(
                            DeviceType.BARCODE_SCANNER,
                            CommandType.BARCODE_SCANNER_SCAN,
                            Status.FAIL,
                            ErrorList.getError(resultCode)
                        )
                    )
                }
            })
    }

    override suspend fun status(): Container {
        val result = cameraScan.status
        return Container(
            DeviceType.BARCODE_SCANNER,
            CommandType.STATUS,
            Status.SUCCESS,
            StatusList.getPublicStatus(result)
        )
    }

    override suspend fun reset(): Container {
        if (destroy().status == Status.SUCCESS && open().status == Status.SUCCESS) {
            return Container(DeviceType.BARCODE_SCANNER, CommandType.RESET, Status.SUCCESS)
        }
        return Container(DeviceType.BARCODE_SCANNER, CommandType.RESET, Status.FAIL)
    }

    override suspend fun destroy(): Container {
        return try{
            cameraScan.cancle()
            cameraScan.close()
            Container(DeviceType.BARCODE_SCANNER, CommandType.CLOSE, Status.SUCCESS)
        } catch (e:Exception){
            Container(
                DeviceType.BARCODE_SCANNER,
                CommandType.CLOSE,
                Status.FAIL,
                ErrorList.getError(-1)
            )
        }
    }

    override fun isSupported(): Boolean {
        return true
    }
}