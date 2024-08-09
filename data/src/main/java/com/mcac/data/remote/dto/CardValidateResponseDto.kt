package com.mcac.data.remote.dto

import com.mcac.common.models.BalanceResponse
import com.mcac.common.models.CardValidateResponse

data class CardValidateResponseDto(
    var response:String = "",
    var customerName:String = "",
    var stan:String = "",
    var rrn:String = "",
    var persianDate:String = "",
    var persianTime:String = "",
    var status:String = "",
    var message:String = ""

)
fun CardValidateResponseDto.toModel(): CardValidateResponse {
        return CardValidateResponse(response,customerName,stan,rrn,persianDate,persianTime,status,message)
    }

