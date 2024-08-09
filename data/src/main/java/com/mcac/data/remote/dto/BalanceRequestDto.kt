package com.mcac.data.remote.dto

import com.mcac.common.models.BalanceRequest

data class BalanceRequestDto(
    var trackingNumber:String = "",
    var cardNumber:String = "",
    var lang:String = "",
    var pin1:String = "",
    var terminalId:String = "",
    var terminalType:String = "",
    var track2:String = ""
)
{
    constructor(item: BalanceRequest) : this(
        item.trackingNumber,
        item.cardNumber,
        item.lang,
        item.pin1,
        item.terminalId,
        item.terminalType,
        item.track2)
}

fun BalanceRequestDto.toModel():BalanceRequest{
    return BalanceRequest(trackingNumber,cardNumber,lang,pin1,terminalId,terminalType,track2)
}
