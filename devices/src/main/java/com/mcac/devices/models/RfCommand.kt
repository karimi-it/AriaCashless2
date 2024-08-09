package com.mcac.devices.models

import java.io.Serializable

data class RfCommand(
    var timeOut:Int = 0,
    var sector:Int = 0,
    var index:Int = 0,
    var pin:String = "",
    var data:String = "",
    var apdu:String = "",
    var setting:String = ""
): Serializable
