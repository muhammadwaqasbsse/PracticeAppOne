package com.practice.stockapp.data.repository

import com.practice.stockapp.data.local.dao.StockDao
import com.practice.stockapp.data.local.entity.toDomain
import com.practice.stockapp.data.local.entity.toEntity
import com.practice.stockapp.data.remote.api.StockApi
import com.practice.stockapp.data.remote.dto.toDomain
import com.practice.stockapp.domain.model.Stock
import com.practice.stockapp.domain.repository.StockRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val dao: StockDao
) : StockRepository {
    override suspend fun getStocks(): List<Stock> {
        return try {
            val stocks = api.getStocks().map { it.toDomain() }
            dao.insertAll(stocks.map { it.toEntity() })
            stocks
        } catch (e: Exception) {
            dao.getAllStocks().first().map { it.toDomain() }
        }
    }

    override fun searchStocks(query: String): Flow<List<Stock>> {
        return dao.getAllStocks().map { list ->
            list.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.ticker.contains(query, ignoreCase = true)
            }.map { it.toDomain() }
        }
    }
}