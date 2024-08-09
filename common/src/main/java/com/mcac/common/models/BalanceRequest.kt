package com.mcac.common.models

data class BalanceRequest(
    var trackingNumber:String = "",
    var cardNumber:String = "",
    var lang:String = "",
    var pin1:String = "",
    var terminalId:String = "",
    var terminalType:String = "",
    var track2:String = ""
)
