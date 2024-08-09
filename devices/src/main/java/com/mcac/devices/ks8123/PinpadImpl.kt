package com.mcac.devices.ks8123

import com.mcac.common.utils.StringUtility
import com.mcac.devices.enums.CommandType
import com.mcac.devices.enums.DeviceType
import com.mcac.devices.enums.KeyType
import com.mcac.devices.enums.Status
import com.mcac.devices.hardwares.IPinpad

import com.mcac.devices.ks8123.util.ErrorList
import com.mcac.devices.ks8123.util.StatusList
import com.mcac.devices.models.Container
import com.mcac.devices.models.PinpadCommand
import com.mcac.devices.models.PinpadResponse
import com.szzt.android.util.HexDump
import com.szzt.sdk.device.Constants
import com.szzt.sdk.device.pinpad.PinPad
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PinpadImpl(private val pinPad: PinPad) : IPinpad {

    private var listener: IPinpad.PinpadListener? = null

    companion object {
        var Instance: PinpadImpl? = null

        fun getInstance(pinPad: PinPad): PinpadImpl {
            if (Instance == null) {
                synchronized(this) {
                    Instance = PinpadImpl(pinPad)
                }
            }
            return Instance!!
        }
    }

    override suspend fun open(listener: IPinpad.PinpadListener): Container {
        this.listener = listener
        val result = pinPad.open {
            listener?.onKey(it)
        }
        return if (result >= Constants.Error.OK) {
            Container(DeviceType.CONTACT_LESS_READER, CommandType.OPEN, Status.SUCCESS)
        } else {
            Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.OPEN,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }

    }

    override suspend fun setting(command: PinpadCommand): Container {
        TODO("Not yet implemented")
    }

    override suspend fun calculatePinBlack(command: PinpadCommand): Container = suspendCoroutine {
        pinPad.calculatePinBlock(
            0,
            command.cardNumber.toByteArray(),
            command.timeOut.toInt(),
            0
        ) { result, data ->
            if (result >= Constants.Error.OK) {
                it.resume(
                    Container(
                        DeviceType.PIN_PAD,
                        CommandType.PINPAD_CALCULATE_PIN_BLACK,
                        Status.SUCCESS,
                        PinpadResponse().apply {
                            pinBlack = HexDump.toHexString(data)
                        })
                )
            } else {
                it.resume(
                    Container(
                        DeviceType.PIN_PAD,
                        CommandType.PINPAD_CALCULATE_PIN_BLACK,
                        Status.FAIL,
                        ErrorList.getError(result)
                    )
                )
            }
        }
    }

    override suspend fun calculateMac(command: PinpadCommand): Container = suspendCoroutine {
        var macKey = command.keys.findLast { it.keyType == KeyType.MAC }
        try {
            var rowData = ByteArray(512)
            val result = pinPad.calculateMac(
                macKey!!.index,
                command.buffer.toByteArray(),
                macKey.keyType,
                rowData
            )
            if (result >= Constants.Error.OK) {
                var data = rowData.slice(0 until result).toByteArray()
                it.resume(
                    Container(
                        DeviceType.PIN_PAD,
                        CommandType.PINPAD_CALCULATE_MAC,
                        Status.SUCCESS,
                        PinpadResponse().apply {
                            this.buffer = HexDump.toHexString(data)
                        })
                )
            } else {
                it.resume(
                    Container(
                        DeviceType.PIN_PAD, CommandType.PINPAD_CALCULATE_MAC, Status.FAIL,
                        ErrorList.getError(result)
                    )
                )
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun calculateSpecialPinBlock(command: PinpadCommand): Container {
        TODO("Not yet implemented")
    }

    override suspend fun calculateOfflinePin(command: PinpadCommand): Container {
        TODO("Not yet implemented")
    }

    override suspend fun cancelInput(): Container = suspendCoroutine {
        try {
            val result = pinPad.cancleInput()
            if (result >= Constants.Error.OK) {
                it.resume(
                    Container(
                        DeviceType.PIN_PAD, CommandType.PINPAD_CANCEL_INPUT, Status.SUCCESS
                    )
                )
            } else {
                it.resume(
                    Container(
                        DeviceType.PIN_PAD,
                        CommandType.PINPAD_CANCEL_INPUT,
                        Status.FAIL,
                        ErrorList.getError(result)
                    )

                )
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun confirmInput(): Container = suspendCoroutine {
        try {
            val result = pinPad.confirmInput()
            if (result >= Constants.Error.OK) {
                it.resume(
                    Container(
                        DeviceType.PIN_PAD,
                        CommandType.PINPAD_CONFIRM_INPUT,
                        Status.SUCCESS
                    )
                )
            } else {
                it.resume(
                    Container(
                        DeviceType.PIN_PAD,
                        CommandType.PINPAD_CONFIRM_INPUT,
                        Status.FAIL,
                        ErrorList.getError(result)
                    )
                )
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteMainKey(command: PinpadCommand): Container {
        var result =
            pinPad.deleteMainKey(command!!.deleteMainKey.index, command.deleteKey.cryptographyMode)
        return if (result >= Constants.Error.OK) {
            Container(
                DeviceType.PIN_PAD,
                CommandType.PINPAD_DELETE_MAIN_KEY,
                Status.SUCCESS
            )
        } else {
            Container(
                DeviceType.PIN_PAD,
                CommandType.PINPAD_DELETE_MAIN_KEY,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun deleteKey(command: PinpadCommand): Container {
        var result = pinPad.deleteKey(
            command!!.deleteKey.keyType,
            command!!.deleteKey.index,
            command.deleteKey.keyType
        )
        return if (result >= Constants.Error.OK) {
            Container(DeviceType.PIN_PAD,
                CommandType.PINPAD_DELETE_KEY,
                Status.SUCCESS)
        } else {
            Container(DeviceType.PIN_PAD,
                CommandType.PINPAD_DELETE_KEY,
                Status.FAIL,
                ErrorList.getError(result))
        }
    }

    override suspend fun deleteAllKey(command: PinpadCommand): Container {
        var result = 0
        for (item in command.deleteKeys) {
            result += pinPad.deleteKey(item.keyType, item.index, item.keyType)
        }
        return if (result >= Constants.Error.OK) {
            Container(DeviceType.PIN_PAD, CommandType.PINPAD_DELETE_ALL_KEY, Status.SUCCESS)
        } else {
            Container(DeviceType.PIN_PAD, CommandType.PINPAD_DELETE_ALL_KEY, Status.FAIL, ErrorList.getError(result))
        }
    }

    override suspend fun isKeyExist(command: PinpadCommand): Container {
        var result = pinPad.isKeyExist(
            command!!.iSKeyExist.keyType,
            command.iSKeyExist.index,
            command.iSKeyExist.cryptographyMode
        )
        return if (result >= Constants.Error.OK) {
            Container(DeviceType.PIN_PAD, CommandType.PINPAD_IS_KEY_EXIST, Status.SUCCESS)
        } else {
            Container(DeviceType.PIN_PAD, CommandType.PINPAD_IS_KEY_EXIST, Status.FAIL, ErrorList.getError(result))
        }
    }

    override suspend fun getKey(command: PinpadCommand): Container {
        TODO("Not yet implemented")
    }

    override suspend fun getKCV(command: PinpadCommand): Container {
        TODO(
            "Not yet im" +
                    "plemented"
        )
    }

    override suspend fun getSerialNo(): Container = suspendCoroutine {
        try {
            val result = pinPad.serialNo
            if (result.size >= Constants.Error.OK) {
                it.resume(
                    Container(
                        DeviceType.PIN_PAD,
                        CommandType.PINPAD_GET_SERIAL_NO,
                        Status.SUCCESS,
                        PinpadResponse().apply {
                            this.serialNo = String(result)
                        })
                )
            } else {
                it.resume(
                    Container(
                        DeviceType.PIN_PAD,
                        CommandType.PINPAD_GET_SERIAL_NO,
                        Status.FAIL,
                        ErrorList.getError(-1)
                    )
                )
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun showText(command: PinpadCommand): Container {
        val result = pinPad.showText(0, command.buffer.toByteArray(), command.keySound != 0)
        return if (result >= Constants.Error.OK) {
            Container(DeviceType.PIN_PAD, CommandType.PINPAD_SHOW_TEXT, Status.SUCCESS)
        } else {
            Container(
                DeviceType.PIN_PAD,
                CommandType.PINPAD_SHOW_TEXT,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun encryptData(command: PinpadCommand): Container = suspendCoroutine {
        var macKey = command.keys.findLast { it.keyType == KeyType.TD }
        try {
            var rowData = ByteArray(512)
            val result = pinPad.encryptData(macKey!!.index, command.buffer.toByteArray(), rowData)
            if (result >= Constants.Error.OK) {
                var data = rowData.slice(0 until result).toByteArray()
                it.resume(
                    Container(
                        DeviceType.PIN_PAD,
                        CommandType.PINPAD_ENCRYPT_DATA,
                        Status.SUCCESS,
                        PinpadResponse().apply {
                            this.buffer = StringUtility.ByteArrayToString(data, data.size)
                        }
                    )
                )
            } else {
                it.resume(
                    Container(
                        DeviceType.PIN_PAD,
                        CommandType.PINPAD_ENCRYPT_DATA,
                        Status.FAIL,
                        ErrorList.getError(result)
                    )
                )
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun setPinLimit(command: PinpadCommand): Container {
        val result = pinPad.setPinLimit(byteArrayOf(0, command.pinLength.toByte()))
        return if (result >= Constants.Error.OK) {
            Container(
                DeviceType.PIN_PAD,
                CommandType.PINPAD_SET_PIN_LIMIT,
                Status.SUCCESS
            )
        } else {
            Container(
                DeviceType.PIN_PAD,
                CommandType.PINPAD_SET_PIN_LIMIT,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun setPinViewStyle(command: PinpadCommand): Container {
        TODO("Not yet implemented")
    }

    override suspend fun setSpecialMode(command: PinpadCommand): Container {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserKey(command: PinpadCommand): Container {
        var result = 0
        for (item in command.keys) {
            if (item.keyType == KeyType.PIN || item.keyType == KeyType.MAC || item.keyType == KeyType.TD)
                result += pinPad.updateUserKey(
                    item!!.maskerId,
                    item.keyType,
                    item.index,
                    item?.keyValue!!.toByteArray()
                )
        }
        return if (result >= Constants.Error.OK) {
            Container(
                DeviceType.PIN_PAD,
                CommandType.PINPAD_UPDATE_USER_KEY,
                Status.SUCCESS
            )
        } else {
            Container(
                DeviceType.PIN_PAD,
                CommandType.PINPAD_UPDATE_USER_KEY,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun updateMasterKey(command: PinpadCommand): Container {
        var key = command.keys.findLast { it.keyType == KeyType.MAIN }
        var ptkKey = command.keys.findLast { it.keyType == KeyType.PTK }
        var result = pinPad.updateMasterKey(
            ptkKey!!.index,
            key!!.index,
            key?.keyValue!!.toByteArray(),
            null,
            key.cryptographyMode
        )
        return if (result >= Constants.Error.OK) {
            Container(
                DeviceType.PIN_PAD,
                CommandType.PINPAD_UPDATE_MASTER_KEY,
                Status.SUCCESS
            )
        } else {
            Container(
                DeviceType.PIN_PAD,
                CommandType.PINPAD_UPDATE_MASTER_KEY,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun updateKey(command: PinpadCommand): Container {
        TODO("Not yet implemented")
    }

    override suspend fun updatePTK(command: PinpadCommand): Container {
        var key = command.keys.findLast { it.keyType == KeyType.PTK }
        var result =
            pinPad.updatePTK(key!!.index, key?.keyValue!!.toByteArray(), null, key.cryptographyMode)
        return if (result >= Constants.Error.OK) {
            Container(
                DeviceType.PIN_PAD,
                CommandType.PINPAD_UPDATE_PTK,
                Status.SUCCESS
            )
        } else {
            Container(
                DeviceType.PIN_PAD,
                CommandType.PINPAD_UPDATE_PTK,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun status(): Container {
        val result = pinPad.status
        return Container(
            DeviceType.PIN_PAD,
            CommandType.STATUS,
            Status.SUCCESS,
            StatusList.getPublicStatus(result)
        )
    }

    override suspend fun reset(): Container {
        if (destroy().status == Status.SUCCESS && open(listener!!).status == Status.SUCCESS) {
            return Container(DeviceType.PIN_PAD, CommandType.RESET, Status.SUCCESS)
        }
        return Container(DeviceType.PIN_PAD, CommandType.RESET, Status.FAIL)
    }

    override suspend fun destroy(): Container {
        var result = pinPad.close()
        return if (result >= Constants.Error.OK) {
            Container(DeviceType.PIN_PAD, CommandType.CLOSE, Status.SUCCESS)
        } else {
            Container(
                DeviceType.PIN_PAD,
                CommandType.CLOSE,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override fun isSupported(): Boolean {
        return true
    }

}