package com.mcac.devices.models

import java.io.Serializable

data class BarcodeCommand(
    var timeOut:Int = 0,
    var setting:String = "",
) : Serializable {

}