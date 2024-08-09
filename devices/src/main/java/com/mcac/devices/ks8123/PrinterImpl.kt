package com.mcac.devices.ks8123

import android.graphics.Paint
import android.util.Log
import com.mcac.devices.enums.CommandType
import com.mcac.devices.enums.DeviceType
import com.mcac.devices.enums.Status
import com.mcac.devices.hardwares.IPrinter

import com.mcac.devices.ks8123.util.ErrorList
import com.mcac.devices.ks8123.util.ImageUtils
import com.mcac.devices.ks8123.util.StatusList
import com.mcac.devices.models.Container
import com.mcac.devices.models.PrinterCommand
import com.szzt.sdk.device.Constants
import com.szzt.sdk.device.aidl.IPrinterListener
import com.szzt.sdk.device.printer.Printer
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class PrinterImpl constructor(private val printer: Printer) : IPrinter {

    private var fontSize = Printer.Font.NORMAL
    private var textAlign = Printer.Align.LEFT
    private var textSize = 23f

    companion object {

        var Instance: PrinterImpl? = null

        fun getInstance(printer: Printer): PrinterImpl {
            if (Instance == null) {
                synchronized(this) {
                    Instance = PrinterImpl(printer)
                }
            }
            return Instance!!
        }
    }


    override fun open(): Container {
        val result = printer.open()
        Log.d("tttttt","result="+result)
        return if (result >= 0)
            Container(DeviceType.PRINTER, CommandType.OPEN, Status.SUCCESS)
        else
            Container(DeviceType.PRINTER, CommandType.OPEN, Status.FAIL, ErrorList.getError(result))
    }

    fun setting(command: PrinterCommand) {
        try {
            when {
                command.setting.textSize < 15 -> fontSize = Printer.Font.SMALL
                command.setting.textSize < 25 -> fontSize = Printer.Font.NORMAL
                command.setting.textSize >= 25 -> fontSize = Printer.Font.LARGE
            }
            textSize = command.setting.textSize.toFloat()
            textAlign = when (command.setting.align) {
                1 -> Printer.Align.LEFT
                2 -> Printer.Align.RIGHT
                else -> Printer.Align.CENTER
            }
        } catch (e: Exception) {
        }

    }

    override suspend fun print(command: PrinterCommand): Container = suspendCoroutine {
        setting(command)
//        printer.print(command.text.toByteArray())
//        printer.addStr(command.text,Printer.Font.FONT_2, false, Printer.Align.RIGHT)
        val bm = ImageUtils.textToBitmap(
            command.text, textSize, when (textAlign) {
                Printer.Align.RIGHT -> Paint.Align.RIGHT
                Printer.Align.LEFT -> Paint.Align.LEFT
                else -> Paint.Align.CENTER
            }
        )
        printer.addImg(bm)
        printer.addFeedPaper(3, Printer.Unit.LINE)
        printer.start(object : IPrinterListener.Stub() {
            override fun PrinterNotify(status: Int) {
                if (status >= 0) {
                    it.resume(
                        Container(
                            DeviceType.PRINTER,
                            CommandType.PRINTER_PRINT,
                            Status.SUCCESS
                        )
                    )
                } else {
                    it.resume(
                        Container(
                            DeviceType.PRINTER,
                            CommandType.PRINTER_PRINT,
                            Status.FAIL,
                            ErrorList.getError(status)
                        )
                    )
                }
            }
        })
    }

    override suspend fun printImage(command: PrinterCommand): Container = suspendCoroutine {
        setting(command)
        val bm = ImageUtils.base64ToBitmap(command.image)
        printer.addImg(bm)
        //printer.addFeedPaper(3, Printer.Unit.LINE)
        printer.start(object : IPrinterListener.Stub() {
            override fun PrinterNotify(status: Int) {
                if (status >= Constants.Error.OK) {
                    it.resume(
                        Container(
                            DeviceType.PRINTER,
                            CommandType.PRINTER_PRINT_IMAGE,
                            Status.SUCCESS
                        )
                    )
                } else {
                    it.resume(
                        Container(
                            DeviceType.PRINTER,
                            CommandType.PRINTER_PRINT_IMAGE,
                            Status.FAIL,
                            ErrorList.getError(status)
                        )
                    )
                }
            }
        })
    }

    override suspend fun printBarcode(command: PrinterCommand): Container = suspendCoroutine {
        setting(command)
        printer.addBarCode(command.barCode)
        printer.addStr(command.barCode, Printer.Font.FONT_2, false, Printer.Align.CENTER)
        printer.addFeedPaper(3, Printer.Unit.LINE)
        printer.start(object : IPrinterListener.Stub() {
            override fun PrinterNotify(status: Int) {
                if (status >= Constants.Error.OK) {
                    it.resume(
                        Container(
                            DeviceType.PRINTER,
                            CommandType.PRINTER_PRINT_BARCODE,
                            Status.SUCCESS
                        )
                    )
                } else {
                    it.resume(
                        Container(
                            DeviceType.PRINTER,
                            CommandType.PRINTER_PRINT_BARCODE,
                            Status.FAIL,
                            ErrorList.getError(status)
                        )
                    )
                }
            }
        })
    }

    override suspend fun printQrCode(command: PrinterCommand): Container = suspendCoroutine {
        setting(command)
        printer.addQrCode(command.qrCode)
        printer.addStr(command.qrCode, Printer.Font.FONT_2, false, Printer.Align.CENTER)
        printer.addFeedPaper(3, Printer.Unit.LINE)
        printer.start(object : IPrinterListener.Stub() {
            override fun PrinterNotify(status: Int) {
                if (status >= Constants.Error.OK) {
                    it.resume(
                        Container(
                            DeviceType.PRINTER,
                            CommandType.PRINTER_PRINT_QRCODE,
                            Status.SUCCESS
                        )
                    )
                } else {
                    it.resume(
                        Container(
                            DeviceType.PRINTER,
                            CommandType.PRINTER_PRINT_QRCODE,
                            Status.FAIL,
                            ErrorList.getError(status)
                        )
                    )
                }
            }
        })
    }

    override suspend fun status(): Container {
        val result = printer.status
        return Container(
            DeviceType.PRINTER,
            CommandType.STATUS,
            Status.SUCCESS,
            StatusList.getPrinterStatus(result)
        )
    }

    override suspend fun reset(): Container {
        if (destroy().status == Status.SUCCESS && open().status == Status.SUCCESS) {
            return Container(DeviceType.PRINTER, CommandType.RESET, Status.SUCCESS)
        }
        return Container(DeviceType.PRINTER, CommandType.RESET, Status.FAIL)
    }

    override suspend fun destroy(): Container {
        var result = printer.close()
        return if (result >= Constants.Error.OK) {
            Container(DeviceType.PRINTER, CommandType.CLOSE, Status.SUCCESS)
        } else {
            Container(
                DeviceType.PRINTER,
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