package com.mcac.devices.ks8123

import android.util.Log
import android.view.Surface
import com.mcac.devices.enums.CommandType
import com.mcac.devices.enums.DeviceType
import com.mcac.devices.enums.Status
import com.mcac.devices.hardwares.ICamera
import com.mcac.devices.models.Container
import com.szzt.sdk.device.UsbCamera.UsbCamera

class CameraImpl(private val camera: UsbCamera) : ICamera {

    private var lastSurface: Surface? = null
    private var timeOut: Long = 0

    companion object {
        var Instance: CameraImpl? = null

        fun getInstance(camera: UsbCamera): CameraImpl {
            if (Instance == null) {
                synchronized(this) {
                    Instance = CameraImpl(camera)
                }
            }
            return Instance!!
        }
    }

    override suspend fun open(listener: ICamera.CameraListener): Container {
        camera.close()
        val result = camera.open(0, object : UsbCamera.UsbCameraListener {
            override fun onResult(p0: Int, p1: ByteArray) {
                listener.onResult(p0, p1)
            }
            override fun onFrame(p0: ByteArray) {
                listener.onFrame(p0)
            }
        })
        if (result >= 0) {
            return Container(DeviceType.CAMERA, CommandType.OPEN, Status.SUCCESS)
        }
        return Container(DeviceType.CAMERA, CommandType.OPEN, Status.FAIL)
    }

    override suspend fun setting(): Container {
        TODO("Not yet implemented")
    }

    override suspend fun status(): Container {
        return Container(DeviceType.CAMERA, CommandType.STATUS, obj = camera.status)
    }

    override suspend fun startCamera(surface: Surface): Container {
        try {
            lastSurface = surface
            val result = camera.startPreview(surface)
            if (result >= 0) {
                return Container(DeviceType.CAMERA, CommandType.CAMERA_START_PREVIEW, Status.SUCCESS)
            }
        }
        catch (ex:Exception){
        }
        return Container(DeviceType.CAMERA, CommandType.CAMERA_START_PREVIEW, Status.FAIL)
    }

    override suspend fun reset(): Container {
        lastSurface?.let {
            destroy()
            return startCamera(lastSurface!!).apply {
                commandType = CommandType.RESET
            }
        }
        return Container(DeviceType.CAMERA, CommandType.RESET, Status.FAIL)
    }

    override suspend fun destroy(): Container {
        lastSurface?.release()
        camera.stopPreview()
        camera.close()
        return Container(DeviceType.CAMERA, CommandType.CLOSE, Status.SUCCESS)
    }

    override fun isSupported(): Boolean {
        return true
    }


}