package com.mcac.devices.models

import java.io.Serializable

data class FingerPrintCommand(
    var timeOut:Long = 0,
    var registeredFingers: List<Int> = arrayListOf(),
    var setting:String = "",

) : Serializable {

}