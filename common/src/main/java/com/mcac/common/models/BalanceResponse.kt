package com.mcac.common.models

data class BalanceResponse(
    var response:String = "",
    var totalBalance:String = "",
    var availableBalance:String = "",
    var stan:String = "",
    var rrn:String = "",
    var persianDate:String = "",
    var persianTime:String = "",
    var status:String = "",
    var message:String = ""
)