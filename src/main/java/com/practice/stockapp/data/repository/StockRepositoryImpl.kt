package com.practice.stockapp.data.repository

import com.practice.stockapp.data.local.dao.StockDao
import com.practice.stockapp.data.local.entity.toDomain
import com.practice.stockapp.data.local.entity.toEntity
import com.practice.stockapp.data.remote.api.StockApi
import com.practice.stockapp.data.remote.dto.toDomain
import com.practice.stockapp.domain.model.Stock
import com.practice.stockapp.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockApi: StockApi,
    private val stockDao: StockDao
) : StockRepository {
    override suspend fun getStocks(): List<Stock> {
       return try {
           val stocks = stockApi.getStocks().map { it.toDomain() }
           val stocksEntity = stocks.map { it.toEntity()}
           stockDao.insertAll(stocksEntity)
           stocks
       } catch (e: Exception) {
            return stockDao.getAllStocks().first().map { it.toDomain() }
       }
    }

    override fun searchStocks(query: String): Flow<List<Stock>> {
        return stockDao.getAllStocks().map { list ->
            list.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.ticker.contains(query, ignoreCase = true)
            }.map { it.toDomain() }
        }
    }
}