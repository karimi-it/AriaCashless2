package com.mcac.data.remote.dto

import com.mcac.common.models.BalanceResponse

data class BalanceResponseDto(
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

fun BalanceResponseDto.toModel(): BalanceResponse {
    return BalanceResponse(response,totalBalance,availableBalance,stan,rrn,persianDate,persianTime,status,message)
}