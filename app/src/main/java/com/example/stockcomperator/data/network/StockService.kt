package com.example.stockcomperator.data.network

import com.example.stockcomperator.data.model.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StockService {

    @GET("/dummy/path")
    suspend fun getStockList(@Query("type") type: String): Response<BaseResponse>

}