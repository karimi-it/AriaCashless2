package com.mcac.data.remote.source

import com.mcac.common.models.*
import com.mcac.data.remote.ApiService
import com.mcac.data.remote.dto.*
import retrofit2.Response

class RemoteDataSourceImpl constructor(private val apiService: ApiService) : RemoteDataSource {


    override suspend fun getBalance(balanceRequest: BalanceRequest): Result<BalanceResponse> {
        try {
            val result =  apiService.getBalance("Basic bWFibmE6TUBCTkAxNDAx",BalanceRequestDto(balanceRequest))
            if(result.isSuccessful && result.body() != null){
                return Result.success(result.body()!!.toModel())
            }
        }
        catch (ex:Exception){
            return Result.failure(ex)
        }
        return Result.failure(Exception())
    }


    override suspend fun cardValidate(cardValidateRequest: CardValidateRequest): Result<CardValidateResponse> {
        try {
            val result =  apiService.cardValidate("Basic bWFibmE6TUBCTkAxNDAx",CardValidateRequestDto(cardValidateRequest))
            if(result.isSuccessful && result.body() != null){
                return Result.success(result.body()!!.toModel())
            }
        }
        catch (ex:Exception){
            return Result.failure(ex)
        }
        return Result.failure(Exception())
    }


    override suspend fun cardToCard(cardToCardRequest: CardToCardRequest): Result<CardToCardResponse> {
        try {
            val result =  apiService.cardToCard("Basic bWFibmE6TUBCTkAxNDAx",CardToCardRequestDto(cardToCardRequest))
            if(result.isSuccessful && result.body() != null){
                return Result.success(result.body()!!.toModel())
            }
        }
        catch (ex:Exception){
            return Result.failure(ex)
        }
        return Result.failure(Exception())

    }

}