package com.practice.stockapp.data.remote.api

import com.practice.stockapp.data.remote.dto.StockDto
import retrofit2.http.GET

interface StockApi {
    @GET("stocks")
    suspend fun getStocks(): List<StockDto>
}
