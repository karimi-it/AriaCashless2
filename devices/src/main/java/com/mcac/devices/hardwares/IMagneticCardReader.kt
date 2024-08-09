package com.mcac.devices.hardwares

import com.mcac.devices.models.Container
import com.mcac.devices.models.MagnetCardCommand

interface IMagneticCardReader : IHardwareSupported {
    suspend fun open(): Container
    suspend fun setting(): Container
    suspend fun readTracks(command: MagnetCardCommand): Container
    suspend fun listenerForCard(command: MagnetCardCommand): Container
    suspend fun status(): Container
    suspend fun reset(): Container
    suspend fun destroy(): Container

    interface MagneticCardListener {
        fun onReadSuccess(data: ByteArray)
        fun onFail(code: Int, message: String)
    }
}