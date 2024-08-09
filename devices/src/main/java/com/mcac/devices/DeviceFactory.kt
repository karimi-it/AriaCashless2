package com.mcac.devices

import android.content.Context
import com.mcac.devices.ks8123.SZZTKS8123
import com.mcac.devices.hardwares.IDevice
import com.mcac.devices.ks8123.util.DeviceUtils

class DeviceFactory constructor(val context: Context) {

    fun getDevice(): IDevice {
        when(DeviceUtils.getModel()){
            SZZTKS8123.DEVICE_MODEL -> return SZZTKS8123(context)
        }
            throw Exception()
    }
}