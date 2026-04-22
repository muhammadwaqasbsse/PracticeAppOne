package com.practice.stockapp.domain.repository

import com.practice.stockapp.domain.model.Stock
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getStocks()
    fun searchStocks(query: String): Flow<List<Stock>>
}
