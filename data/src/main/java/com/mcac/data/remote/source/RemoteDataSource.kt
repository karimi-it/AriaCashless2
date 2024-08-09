package com.mcac.data.remote.source

import com.mcac.common.models.*
import com.mcac.data.remote.dto.*
interface RemoteDataSource {

    suspend fun getBalance(balanceRequest: BalanceRequest): Result<BalanceResponse>

    suspend fun cardValidate(cardValidateRequest: CardValidateRequest): Result<CardValidateResponse>

    suspend fun cardToCard(cardToCardRequest: CardToCardRequest): Result<CardToCardResponse>
}