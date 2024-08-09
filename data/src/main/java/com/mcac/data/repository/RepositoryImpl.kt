package com.mcac.data.repository

import com.mcac.common.models.*
import com.mcac.data.remote.source.RemoteDataSource

class RepositoryImpl constructor(private val remoteDataSource: RemoteDataSource) : Repository {
    override suspend fun getBalance(balanceRequest: BalanceRequest): Result<BalanceResponse> {
        return remoteDataSource.getBalance(balanceRequest)
    }

    override suspend fun cardValidate(cardValidateRequest: CardValidateRequest): Result<CardValidateResponse> {
        return remoteDataSource.cardValidate(cardValidateRequest)
    }

    override suspend fun cardToCard(cardToCardRequest: CardToCardRequest): Result<CardToCardResponse> {
        return remoteDataSource.cardToCard(cardToCardRequest)
    }
}