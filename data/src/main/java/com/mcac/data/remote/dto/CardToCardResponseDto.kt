package com.mcac.data.remote.dto

import com.mcac.common.models.CardToCardResponse

data class CardToCardResponseDto(
    var response:String = "",
    var stan:String = "",
    var rrn:String = "",
    var persianDate:String = "",
    var persianTime:String = "",
    var status:String = "",
    var message:String = ""
)

    fun CardToCardResponseDto.toModel(): CardToCardResponse {
        return CardToCardResponse(response,stan,rrn,persianDate,persianTime,status,message)
    }

