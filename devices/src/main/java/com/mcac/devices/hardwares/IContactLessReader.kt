package com.mcac.devices.hardwares

import com.mcac.devices.models.Container
import com.mcac.devices.models.RfCommand

interface IContactLessReader : IHardwareSupported {
    suspend fun open(): Container
    suspend fun setting(): Container
    suspend fun powerOn(): Container
    suspend fun powerOff(): Container
    suspend fun waitForCard(command: RfCommand): Container
    suspend fun listenForCard(command: RfCommand): Container
    suspend fun cardType(): Container
    suspend fun verifyPin(command: RfCommand): Container
    suspend fun cardInfo(): Container
    suspend fun transmit(command: RfCommand): Container
    suspend fun readMifare(command: RfCommand): Container
    suspend fun writeMifare(command: RfCommand): Container
    suspend fun read(command: RfCommand): Container
    suspend fun write(command: RfCommand): Container
    suspend fun status(): Container
    suspend fun reset(): Container
    suspend fun destroy(): Container

}