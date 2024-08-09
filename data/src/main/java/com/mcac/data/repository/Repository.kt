package com.mcac.data.repository

import com.mcac.common.models.*

interface Repository {

    suspend fun getBalance(balanceRequest: BalanceRequest): Result<BalanceResponse>

    suspend fun cardValidate(cardValidateRequest: CardValidateRequest): Result<CardValidateResponse>

    suspend fun cardToCard(cardToCardRequest: CardToCardRequest): Result<CardToCardResponse>
}