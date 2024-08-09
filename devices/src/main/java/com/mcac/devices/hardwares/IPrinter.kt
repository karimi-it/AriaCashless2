package com.mcac.devices.hardwares

import com.mcac.devices.models.Container
import com.mcac.devices.models.PrinterCommand

interface IPrinter : IHardwareSupported {

    fun open(): Container
    suspend fun print(command: PrinterCommand): Container
    suspend fun printImage(command: PrinterCommand): Container
    suspend fun printBarcode(command: PrinterCommand): Container
    suspend fun printQrCode(command: PrinterCommand): Container
    suspend fun status(): Container
    suspend fun reset(): Container
    suspend fun destroy(): Container
}
