package com.practice.stockapp.domain.usecase

import com.practice.stockapp.domain.model.Stock
import com.practice.stockapp.domain.repository.StockRepository
import javax.inject.Inject

class GetStocksUseCase @Inject constructor(
    private val repository: StockRepository
) {
    suspend operator fun invoke(): List<Stock> = repository.getStocks()
}