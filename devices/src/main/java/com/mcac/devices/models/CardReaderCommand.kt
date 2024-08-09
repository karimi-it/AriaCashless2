package com.mcac.devices.models

import java.io.Serializable

data class CardReaderCommand(
    var slotIndex:Int  = 0,
    var timeOut:Int  = 0,
    var apdu: String = "",
    var data: String = "",
    var pin: String = "",
    var pinType: Int = 0,
    var area:Int  = 0,
    var address:Int  = 0,
    var setting: String = "",
    ) : Serializable {

}