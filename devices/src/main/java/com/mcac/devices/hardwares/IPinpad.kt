package com.mcac.devices.hardwares

import com.mcac.devices.models.Container
import com.mcac.devices.models.PinpadCommand

interface IPinpad : IHardwareSupported {

    suspend fun open(listener: PinpadListener): Container
    suspend fun setting(command: PinpadCommand): Container
    suspend fun calculatePinBlack(command: PinpadCommand): Container
    suspend fun calculateMac(command: PinpadCommand): Container
    suspend fun calculateSpecialPinBlock(command: PinpadCommand): Container
    suspend fun calculateOfflinePin(command: PinpadCommand): Container
    suspend fun cancelInput(): Container
    suspend fun confirmInput(): Container
    suspend fun deleteMainKey(command: PinpadCommand): Container
    suspend fun deleteKey(command: PinpadCommand): Container
    suspend fun deleteAllKey(command: PinpadCommand): Container
    suspend fun isKeyExist(command: PinpadCommand): Container
    suspend fun getKey(command: PinpadCommand): Container
    suspend fun getKCV(command: PinpadCommand): Container
    suspend fun getSerialNo(): Container
    suspend fun showText(command: PinpadCommand): Container
    suspend fun encryptData(command: PinpadCommand): Container
    suspend fun setPinLimit(command: PinpadCommand): Container
    suspend fun setPinViewStyle(command: PinpadCommand): Container
    suspend fun setSpecialMode(command: PinpadCommand): Container
    suspend fun updateUserKey(command: PinpadCommand): Container
    suspend fun updateMasterKey(command: PinpadCommand): Container
    suspend fun updateKey(command: PinpadCommand): Container
    suspend fun updatePTK(command: PinpadCommand): Container
    suspend fun status(): Container
    suspend fun reset(): Container
    suspend fun destroy(): Container

    interface PinpadListener {
        fun onKey(key: Int)
        fun onFail(code: Int, msg: String)
    }
}