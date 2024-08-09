package com.mcac.devices.models

import com.mcac.devices.enums.CryptographyMode
import com.mcac.devices.enums.KeyType

data class Key(
    var keyType:Int = KeyType.NONE,
    var maskerId:Int = 0,
    var index:Int = 0,
    var keyValue:String = "",
    var cryptographyMode: Int = CryptographyMode.NONE

)
