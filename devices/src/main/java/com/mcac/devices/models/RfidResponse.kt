package com.mcac.devices.models

import java.io.Serializable

data class RfidResponse(
    var data:String = "",
    var atr:String = "",
    var cardType:Int = 0,
    var cardInfo:String = ""
):Serializable {
}