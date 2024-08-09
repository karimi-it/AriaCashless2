package com.mcac.devices.ks8123

import com.mcac.devices.enums.CommandType
import com.mcac.devices.enums.DeviceType
import com.mcac.devices.enums.Status
import com.mcac.devices.hardwares.IContactLessReader
import com.mcac.devices.ks8123.util.ErrorList
import com.mcac.devices.ks8123.util.StatusList
import com.mcac.devices.models.Container
import com.mcac.devices.models.RfCommand
import com.mcac.devices.models.RfidResponse
import com.szzt.android.util.HexDump
import com.szzt.sdk.device.Constants
import com.szzt.sdk.device.card.ContactlessCardReader
import com.szzt.sdk.device.card.M1KeyType
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ContactLessReaderImpl private constructor(private val contactlessCardReader: ContactlessCardReader) :
    IContactLessReader {

    companion object {
        var instance: ContactLessReaderImpl? = null

        fun getInstance(contactlessCardReader: ContactlessCardReader): ContactLessReaderImpl {
            if (instance == null) {
                synchronized(this) {
                    instance = ContactLessReaderImpl(contactlessCardReader)
                }
            }
            return instance!!
        }
    }

    override suspend fun open(): Container {
        val result = contactlessCardReader.open()
        return if (result >= Constants.Error.OK) {
            Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.OPEN,
                Status.SUCCESS
            )
        } else {
            Container(
                DeviceType.CONTACT_LESS_READER,
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
        val rowData = ByteArray(256)
        val result = contactlessCardReader.powerOn(rowData)
        return if (result >= Constants.Error.OK) {
            var data = rowData.slice(0 until result).toByteArray()
            Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.CONTACT_LESS_POWER_ON,
                Status.SUCCESS,
                RfidResponse().apply {
                    this.data = String(data)
                }
            )
        } else {
            Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.CONTACT_LESS_POWER_ON,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun powerOff(): Container {
        val result = contactlessCardReader.powerOff()
        return if (result >= Constants.Error.OK) {
            Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.CONTACT_LESS_POWER_OFF,
                Status.SUCCESS
            )
        } else {
            Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.CONTACT_LESS_POWER_OFF,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun waitForCard(command: RfCommand): Container {
        val result = contactlessCardReader.waitForCard(command.timeOut)
        return if (result >= Constants.Error.OK)
            Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.CONTACT_LESS_WAIT_FOR_CARD,
                Status.SUCCESS
            )
        else
            Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.CONTACT_LESS_WAIT_FOR_CARD,
                Status.FAIL,
                ErrorList.getError(result)
            )
    }

    override suspend fun listenForCard(command: RfCommand): Container = suspendCoroutine {
        contactlessCardReader.listenForCard(
            command.timeOut
        ) { nEvent, errorCode ->
            if (nEvent >= Constants.Error.OK) {
                it.resume(
                    Container(
                        DeviceType.CONTACT_LESS_READER,
                        CommandType.CONTACT_LESS_LISTEN_FOR_CARD,
                        Status.SUCCESS
                    )
                )
            } else
                it.resume( Container(
                    DeviceType.CONTACT_LESS_READER,
                    CommandType.CONTACT_LESS_LISTEN_FOR_CARD,
                    Status.FAIL,
                    ErrorList.getError(errorCode))
                )
        }

    }

    override suspend fun cardType(): Container {
        val cardType = contactlessCardReader.cardType
        return if (cardType >= Constants.Error.OK) {
            Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.CONTACT_LESS_CARD_TYPE,
                Status.SUCCESS,
                RfidResponse().apply {
                    this.cardType = cardType
                }
            )
        } else {
            Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.CONTACT_LESS_CARD_TYPE,
                Status.FAIL,
                ErrorList.getError(-1)
            )
        }
    }

    override suspend fun verifyPin(command: RfCommand): Container {
        val result = contactlessCardReader.verifyPinMifare(
            command.sector,
            M1KeyType.KEYTYPE_A,
            HexDump.hexStringToByteArray(command.pin)
        )
        return if (result >= Constants.Error.OK) {
            Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.CONTACT_LESS_VERIFY_PIN,
                Status.SUCCESS
            )
        } else {
            Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.CONTACT_LESS_VERIFY_PIN,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override suspend fun cardInfo(): Container {
        val cardInfo = contactlessCardReader.cardInfo
        if (cardInfo != null) {
            return Container(
                DeviceType.CONTACT_LESS_READER,
                CommandType.CONTACT_LESS_CARD_INFO,
                Status.SUCCESS,
                RfidResponse().apply {
                    this.cardInfo = cardInfo.toString()
                }
            )
        }
        return Container(
            DeviceType.CONTACT_LESS_READER,
            CommandType.CONTACT_LESS_CARD_INFO,
            Status.FAIL,
            ErrorList.getError(-1)
        )
    }

    override suspend fun transmit(command: RfCommand): Container {
        var result = -1
        try {
            val rowData = ByteArray(256)
            result =
                contactlessCardReader.transmit(HexDump.hexStringToByteArray(command.apdu), rowData)
            if (result >= Constants.Error.OK) {
                var data = rowData.slice(0 until result).toByteArray()
                return Container(
                    DeviceType.CONTACT_LESS_READER,
                    CommandType.CONTACT_LESS_TRANSMIT,
                    Status.SUCCESS,
                    RfidResponse().apply {
                        this.data = String(data)
                    }
                )
            }
        } catch (e: Exception) {
            e.stackTrace
        }
        return Container(
            DeviceType.CONTACT_LESS_READER,
            CommandType.CONTACT_LESS_TRANSMIT,
            Status.FAIL,
            ErrorList.getError(result)
        )
    }

    override suspend fun readMifare(command: RfCommand): Container {
        var result = -1
        try {
            val rowData = ByteArray(512)
            result = contactlessCardReader.readMifare(command.sector, command.index, rowData)
            if (result >= Constants.Error.OK) {
                var data = rowData.slice(0 until result).toByteArray()
                return Container(
                    DeviceType.CONTACT_LESS_READER,
                    CommandType.CONTACT_LESS_READ_MIFARE,
                    Status.SUCCESS,
                    RfidResponse().apply {
                        this.data = String(data)
                    }
                )
            }

        } catch (e: Exception) {
            e.stackTrace
        }
        return Container(
            DeviceType.CONTACT_LESS_READER,
            CommandType.CONTACT_LESS_READ_MIFARE,
            Status.FAIL,
            ErrorList.getError(result)
        )
    }

    override suspend fun writeMifare(command: RfCommand): Container {
        var result = -1
        try {
            result = contactlessCardReader.writeMifare(
                command.sector,
                command.index,
                command.data.toByteArray()
            )
            if (result >= Constants.Error.OK)
                return Container(
                    DeviceType.CONTACT_LESS_READER,
                    CommandType.CONTACT_LESS_WRITE_MIFARE,
                    Status.SUCCESS
                )
        } catch (e: Exception) {
            e.message
        }
        return Container(
            DeviceType.CONTACT_LESS_READER,
            CommandType.CONTACT_LESS_WRITE_MIFARE,
            Status.FAIL,
            ErrorList.getError(result)
        )
    }

    override suspend fun read(command: RfCommand): Container {
        var result = -1
        try {
            val rowData = ByteArray(512)
            result = contactlessCardReader.read(command.sector, command.index, rowData)
            if (result >= Constants.Error.OK) {
                var data = rowData.slice(0 until result).toByteArray()
                return Container(
                    DeviceType.CONTACT_LESS_READER,
                    CommandType.CONTACT_LESS_READ_MIFARE,
                    Status.SUCCESS,
                    RfidResponse().apply {
                        this.data = String(data)
                    }
                )
            }
        } catch (e: Exception) {
        }
        return Container(
            DeviceType.CONTACT_LESS_READER,
            CommandType.CONTACT_LESS_READ_MIFARE,
            Status.FAIL,
            ErrorList.getError(result)

        )
    }

    override suspend fun write(command: RfCommand): Container {
        val result = contactlessCardReader.write(
            command.sector,
            command.index,
            HexDump.hexStringToByteArray(command.data)
        )
        try {
            if (result >= Constants.Error.OK)
                return Container(
                    DeviceType.CONTACT_LESS_READER,
                    CommandType.CONTACT_LESS_WRITE_MIFARE,
                    Status.SUCCESS
                )
        } catch (e: Exception) {

        }
        return Container(
            DeviceType.CONTACT_LESS_READER,
            CommandType.CONTACT_LESS_WRITE_MIFARE,
            Status.FAIL,
            ErrorList.getError(result)
        )
    }

    override suspend fun status(): Container {
        val result = contactlessCardReader.status
        return Container(
            DeviceType.CONTACT_LESS_READER,
            CommandType.STATUS,
            Status.SUCCESS,
            StatusList.getPublicStatus(result)
        )
    }

    override suspend fun reset(): Container {
        if (destroy().status == Status.SUCCESS && open().status == Status.SUCCESS) {
            return Container(DeviceType.CONTACT_LESS_READER, CommandType.RESET, Status.SUCCESS)
        }
        return Container(DeviceType.CONTACT_LESS_READER, CommandType.RESET, Status.FAIL)
    }

    override suspend fun destroy(): Container {
        var result = contactlessCardReader.close()
        return if (result >= Constants.Error.OK) {
            Container(DeviceType.CONTACT_LESS_READER, CommandType.CLOSE, Status.SUCCESS)
        } else {
            Container(
                DeviceType.CONTACT_LESS_READER,
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