package com.mcac.data.remote.dto

import com.mcac.common.models.CardToCardRequest

data class CardToCardRequestDto(
    var trackingNumber:String = "",
    var cardNumber:String = "",
    var toCardNumber:String = "",
    var amount:String = "",
    var lang:String = "",
    var pin1:String = "",
    var terminalId:String = "",
    var terminalType:String = "",
    var track2:String = ""
)
{
    constructor(item: CardToCardRequest) : this(
        item.trackingNumber,
        item.cardNumber,
        item.toCardNumber,
        item.amount,
        item.lang,
        item.pin1,
        item.terminalId,
        item.terminalType,
        item.track2
    )
}
    fun CardToCardRequestDto.toModel(): CardToCardRequest {
        return CardToCardRequest(
            trackingNumber,
            cardNumber,
            toCardNumber,
            amount,
            lang,
            pin1,
            terminalId,
            terminalType,
            track2
        )
    }


