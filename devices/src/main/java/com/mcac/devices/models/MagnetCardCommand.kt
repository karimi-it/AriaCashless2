package com.mcac.devices.models

import java.io.Serializable

class MagnetCardCommand(
    var timeOut:Int  = 0,
    var setting:String = "",
) : Serializable {
}