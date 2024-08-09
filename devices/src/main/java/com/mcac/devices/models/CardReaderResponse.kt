package com.mcac.devices.models

data class CardReaderResponse(
    var cardType:Int = 0,
    var atr:String = "",
    var resultCode:Int = 0,
    var cardData: String ="",
    var isCardReady: Int =-1)