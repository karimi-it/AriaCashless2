package com.mcac.devices.hardwares

import com.mcac.devices.models.Container
import com.mcac.devices.models.CardReaderCommand


interface ISmartCardReader : IHardwareSupported {
    suspend fun open(): Container
    suspend fun setting(): Container
    suspend fun powerOn(): Container
    suspend fun powerOff(): Container
    suspend fun unlock(): Container
    suspend fun lock(): Container
    suspend fun waitForCard(command: CardReaderCommand): Container
    suspend fun verify(command:CardReaderCommand): Container
    suspend fun cardType(): Container
    suspend fun transmit(command:CardReaderCommand): Container
    suspend fun read(command:CardReaderCommand): Container
    suspend fun write(command:CardReaderCommand): Container
    suspend fun listenerForCard(command:CardReaderCommand): Container
    suspend fun setSpecialApdu(command:CardReaderCommand): Container
    suspend fun isCardReady(): Container
    suspend fun status(): Container
    suspend fun reset(): Container
    suspend fun destroy(): Container

    enum class IcCardStatus {
        NONE,
        OPEN,
        CLOSE,
        NOT_FOUND,
        READY,
        NOT_READY,
        WAITING,
        SUCCESS,
        FAIL,
        EXCEPTION
    }
}