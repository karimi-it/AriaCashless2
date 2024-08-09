package com.mcac.devices.models

import com.mcac.devices.models.Key

data class PinpadCommand(
    var timeOut:Long = 0,
    var keys:List<Key> = listOf(),
    var deleteMainKey: Key = Key(),
    var deleteKey: Key = Key(),
    var deleteKeys:List<Key> = listOf(),
    var iSKeyExist: Key = Key(),
    var cardNumber:String = "",
    var pinLength:Int = 0,
    var keySound:Int = 0,
    var buffer:String = "",
    var setting:String = ""
)
