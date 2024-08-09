package com.mcac.devices.hardwares

import android.view.Surface
import com.mcac.devices.models.Container

interface ICamera: IHardwareSupported {
    suspend fun open(listener : CameraListener): Container
    suspend fun setting(): Container
    suspend fun status(): Container
    suspend fun startCamera(surface: Surface): Container
    suspend fun reset(): Container
    suspend fun destroy(): Container

    interface CameraListener {
        fun onResult(i:Int,result:ByteArray)
        fun onFrame(data:ByteArray)
    }

}