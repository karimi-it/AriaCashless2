package com.mcac.devices.ks8123

import android.util.Log
import com.mcac.devices.enums.CommandType
import com.mcac.devices.enums.DeviceType
import com.mcac.devices.enums.Status
import com.mcac.devices.hardwares.ISmartCardReader
import com.mcac.devices.ks8123.util.ErrorList
import com.mcac.devices.ks8123.util.StatusList
import com.mcac.devices.models.CardReaderCommand
import com.mcac.devices.models.CardReaderResponse
import com.mcac.devices.models.Container
import com.szzt.android.util.HexDump
import com.szzt.sdk.device.Constants
import com.szzt.sdk.device.card.SmartCardReader
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SmartCardReaderImpl private constructor(private val icCardReader: SmartCardReader) :
    ISmartCardReader {

    private var CURRENT_IC_CARD_INDEX = 0
    companion object {
        var instance: SmartCardReaderImpl? = null

        fun getInstance(icCardReader: SmartCardReader): SmartCardReaderImpl {
            if (instance == null) {
                synchronized(this) {
                    instance = SmartCardReaderImpl(icCardReader)
                }
            }
            return instance!!
        }
    }

    override suspend fun open(): Container {
        val result = icCardReader.open(CURRENT_IC_CARD_INDEX) { p0, key ->
        }
        return if (result >= Constants.Error.OK) {
            Container(DeviceType.CARD_READER, CommandType.OPEN, Status.SUCCESS)
        } else {
            Container(
                DeviceType.CARD_READER,
                CommandType.OPEN,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun setting(): Container {
        TODO("Not yet implemented")
    }

    override suspend fun powerOn(): Container {
        val rowData = ByteArray(512)
        val result = icCardReader.powerOn(CURRENT_IC_CARD_INDEX, rowData)
        return if (result >= Constants.Error.OK) {
            val data = rowData.slice(0 until result).toByteArray()
            Container(DeviceType.CARD_READER,
                CommandType.CARD_READER_POWER_ON,
                Status.SUCCESS,
                CardReaderResponse().apply {
                    atr = HexDump.toHexString(data).toString()
                    resultCode = result
                }
            )
        } else {
            Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_POWER_ON,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun powerOff(): Container {
        val result = icCardReader.powerOff(CURRENT_IC_CARD_INDEX)
        return if (result >= Constants.Error.OK) {
            Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_POWER_OFF,
                Status.SUCCESS
            )
        } else {
            Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_POWER_OFF,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun unlock(): Container {
        val result = icCardReader.unlock(CURRENT_IC_CARD_INDEX)
        if (result >= Constants.Error.OK) {
            return Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_UNLOCK,
                Status.SUCCESS
            )
        }
        return Container(
            DeviceType.CARD_READER,
            CommandType.CARD_READER_UNLOCK,
            Status.FAIL,
            ErrorList.getError(result)
        )
    }

    override suspend fun lock(): Container {
        val result = icCardReader.lock(CURRENT_IC_CARD_INDEX)
        if (result >= Constants.Error.OK) {
            return Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_LOCK,
                Status.SUCCESS
            )
        }
        return Container(
            DeviceType.CARD_READER,
            CommandType.CARD_READER_LOCK,
            Status.FAIL,
            ErrorList.getError(result)
        )
    }

    override suspend fun waitForCard(command: CardReaderCommand): Container {
        val result = icCardReader.waitForCard(CURRENT_IC_CARD_INDEX, command.timeOut)
        if (result >= Constants.Error.OK) {
            return Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_WAIT_FOR_CARD,
                Status.SUCCESS
            )
        }
        return Container(
            DeviceType.CARD_READER,
            CommandType.CARD_READER_WAIT_FOR_CARD,
            Status.FAIL,
            ErrorList.getError(result)
        )
    }

    override suspend fun listenerForCard(command: CardReaderCommand): Container = suspendCoroutine {
        icCardReader.listenerForCard(CURRENT_IC_CARD_INDEX,command.timeOut
        ) { nSlotIndex, nEvent, resultCode ->
            Log.d("tttt", "nSlotIndex :: $nSlotIndex,    nEvent:: $nEvent, resultCode::   $resultCode")
            if (resultCode >= Constants.Error.OK) {
                it.resume(
                    Container(
                        DeviceType.CARD_READER,
                        CommandType.CARD_READER_LISTENER_FOR_CARD,
                        Status.SUCCESS
                    )
                )
            } else {
                it.resume(
                    Container(
                        DeviceType.CARD_READER,
                        CommandType.CARD_READER_LISTENER_FOR_CARD,
                        Status.FAIL,
                        ErrorList.getError(resultCode)
                    )
                )
            }
        }
    }

    override suspend fun verify(command: CardReaderCommand): Container {
        val result = icCardReader.verify(CURRENT_IC_CARD_INDEX, command.pin.toByteArray(),command.pinType)
        if (result >= Constants.Error.OK) {
            return Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_VERIFY,
                Status.SUCCESS
            )
        }
        return Container(
            DeviceType.CARD_READER,
            CommandType.CARD_READER_VERIFY,
            Status.FAIL,
            ErrorList.getError(result)
        )
    }

    override suspend fun cardType(): Container {
        val result = icCardReader.getCardType(CURRENT_IC_CARD_INDEX)
        if (result >= Constants.Error.OK) {
            return Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_CARD_TYPE,
                Status.SUCCESS,
                CardReaderResponse().apply {
                    cardType == result
                }
            )
        }
        return Container(
            DeviceType.CARD_READER,
            CommandType.CARD_READER_CARD_TYPE,
            Status.FAIL,
            ErrorList.getError(result)
        )
    }

    override suspend fun transmit(command: CardReaderCommand): Container {
        val rowData = ByteArray(512)
        val result = icCardReader.transmit(
            CURRENT_IC_CARD_INDEX,
            HexDump.hexStringToByteArray(command.apdu),
            rowData
        )
        return if (result >= Constants.Error.OK) {
            var data = rowData.slice(0 until result).toByteArray()
            Container(DeviceType.CARD_READER, CommandType.CARD_READER_TRANSMIT, Status.SUCCESS,
                CardReaderResponse().apply {
                    cardData = HexDump.toHexString(data).toString()
                    resultCode = result
                }
            )
        } else {
            Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_TRANSMIT,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun read(command: CardReaderCommand): Container {
        val rowData = ByteArray(512)
        val result = icCardReader.read(CURRENT_IC_CARD_INDEX, command.area, command.address, rowData)
        if (result >= Constants.Error.OK) {
            var data = rowData.slice(0 until result).toByteArray()
            return Container(DeviceType.CARD_READER,
                CommandType.CARD_READER_READ,
                Status.SUCCESS,
                CardReaderResponse().apply {
                    cardData = HexDump.toHexString(data).toString()
                    resultCode = result
                }
            )
        } else {
            return  Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_READ,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun write(command: CardReaderCommand): Container {
        val result = icCardReader.write(
            CURRENT_IC_CARD_INDEX,
            command.area,
            command.address,
            command.data.toByteArray()
        )
        return if (result >= Constants.Error.OK) {
            Container(DeviceType.CARD_READER,
                CommandType.CARD_READER_WRITE,
                Status.SUCCESS,
                CardReaderResponse().apply {
                    resultCode = result
                }
            )
        } else {
            Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_WRITE,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun setSpecialApdu(command: CardReaderCommand): Container {
        val result = icCardReader.setSpecialApdu(CURRENT_IC_CARD_INDEX)
        if (result >= Constants.Error.OK) {
            return Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_SET_SPECIAL_APDU,
                Status.SUCCESS
            )
        }
        return Container(
            DeviceType.CARD_READER,
            CommandType.CARD_READER_SET_SPECIAL_APDU,
            Status.FAIL, ErrorList.getError(result)
        )
    }

    override suspend fun isCardReady(): Container {
        val result = icCardReader.isCardReady(CURRENT_IC_CARD_INDEX)
        if (result > Constants.Error.OK) {
            return Container(
                DeviceType.CARD_READER,
                CommandType.CARD_READER_IS_CARD_READY,
                Status.SUCCESS
            )
        }
        return Container(
            DeviceType.CARD_READER,
            CommandType.CARD_READER_IS_CARD_READY,
            Status.FAIL,
            ErrorList.getError(result)
        )
    }



    override suspend fun status(): Container {
        val result = icCardReader.status
        return Container(
            DeviceType.CARD_READER,
            CommandType.STATUS,
            Status.SUCCESS,
            StatusList.getSmartCardStatus(result)
        )
    }

    override suspend fun reset(): Container {
        if (destroy().status == Status.SUCCESS && open().status == Status.SUCCESS) {
            return Container(DeviceType.CARD_READER, CommandType.RESET, Status.SUCCESS)
        }
        return Container(DeviceType.CARD_READER, CommandType.RESET, Status.FAIL)
    }

    override suspend fun destroy(): Container {
        var result = icCardReader.close(CURRENT_IC_CARD_INDEX)
        return if (result >= Constants.Error.OK) {
            Container(DeviceType.CARD_READER, CommandType.CLOSE, Status.SUCCESS)
        } else {
            Container(
                DeviceType.CARD_READER,
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