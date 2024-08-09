package com.mcac.devices.hardwares

import com.mcac.devices.models.Container
import com.mcac.devices.models.FingerPrintCommand


interface IFingerPrint {
    suspend fun open(): Container
    suspend fun captureFingerImageBig(command: FingerPrintCommand): Container
    suspend fun captureFingerImage152(command: FingerPrintCommand): Container
    suspend fun status(): Container
    suspend fun reset(): Container
    suspend fun destroy(): Container

}