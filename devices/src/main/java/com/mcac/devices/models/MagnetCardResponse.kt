package com.mcac.devices.models

import java.io.Serializable

class MagnetCardResponse(
    var track1: String = "",
    var track2: String = "",
    var track3: String = ""
) : Serializable {
}