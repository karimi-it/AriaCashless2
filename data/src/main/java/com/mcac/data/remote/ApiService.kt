package com.mcac.data.remote

import com.mcac.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //@Headers("Authorization : Basic bWFibmE6TUBCTkAxNDAx")
    @POST("v1/sw/getBalance")
    suspend fun getBalance(@Header("Authorization") authorization:String, @Body balanceRequestDto: BalanceRequestDto): Response<BalanceResponseDto>

    //@Headers("Authorization : Basic bWFibmE6TUBCTkAxNDAx")
    @POST("v1/sw/cardValidate")
    suspend fun cardValidate(@Header("Authorization") authorization:String,@Body cardValidateRequestDto: CardValidateRequestDto): Response<CardValidateResponseDto>

    //@Headers("Authorization : Basic bWFibmE6TUBCTkAxNDAx")
    @POST("v1/sw/cardToCard")
    suspend fun cardToCard(@Header("Authorization") authorization:String,@Body cardToCardRequestDto: CardToCardRequestDto): Response<CardToCardResponseDto>
}