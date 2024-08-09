package com.mcac.devices.ks8123.util

import com.mcac.devices.models.DeviceStatus


object StatusList {
    val STATUS_UNKNOWN = -1
    val STATUS_IDLE = 240
    val STATUS_IN_ACTION = 241
    val STATUS_CLOSE = 242
    val STATUS_OPENED = 0
    val STATUS_CLOSED = 1
    val STATUS_POWER_ON = 2
    val STATUS_POWER_OFF = 3
    val STATUS_CARD_INSERT = 4
    val STATUS_CARD_REMOVE = 5

    private val smartCardStatusMap = HashMap<Int,String>().apply {
        put(-1,"STATUS_UNKNOWN")
        put(240,"STATUS_IDLE")
        put(241,"STATUS_IN_ACTION")
        put(242,"STATUS_CLOSED")
        put(0,"STATUS_OPENED")
        put(1,"STATUS_CLOSED")
        put(2,"STATUS_POWER_ON")
        put(3,"STATUS_POWER_OFF")
        put(4,"STATUS_CARD_INSERT")
        put(5,"STATUS_CARD_REMOVE")

    }

    fun getSmartCardStatus(code:Int): DeviceStatus {
        return if (smartCardStatusMap.containsKey(code)){
            DeviceStatus(code, smartCardStatusMap[code]!!)
        } else{
            DeviceStatus(code, smartCardStatusMap[-1]!!)
        }
    }


    private val printerStatusMap = HashMap<Int,String>().apply {
        put(-1,"STATUS_UNKNOWN")
        put(-210,"STATUS_NO_POWER")
        put(240,"STATUS_IDLE")
        put(241,"STATUS_IN_ACTION")
        put(242,"STATUS_CLOSED")
        put(0,"STATUS_NO_PAPER")
        put(1,"STATUS_OK")
        put(2,"STATUS_HARD_ERR")
        put(3,"STATUS_OVERHEAT")
        put(4,"STATUS_LOW_VOL")
        put(5,"STATUS_PAPER_JAM")
        put(6,"STATUS_BUSY")
        put(7,"STATUS_LIFT_HEAD")
        put(8,"STATUS_CUT_POSITION_ERR")
        put(9,"STATUS_LOW_TEMP")

    }

    fun getPrinterStatus(code:Int): DeviceStatus {
        return if (printerStatusMap.containsKey(code)){
            DeviceStatus(code, printerStatusMap[code]!!)
        } else{
            DeviceStatus(code, printerStatusMap[-1]!!)
        }
    }

    private val publicStatusMap = HashMap<Int,String>().apply {
        put(-1,"STATUS_UNKNOWN")
        put(240,"STATUS_IDLE")
        put(241,"STATUS_IN_ACTION")
        put(242,"STATUS_CLOSED")

    }

    fun getPublicStatus(code:Int): DeviceStatus {
        return if (publicStatusMap.containsKey(code)){
            DeviceStatus(code, publicStatusMap[code]!!)
        } else{
            DeviceStatus(code, publicStatusMap[-1]!!)
        }
    }
}