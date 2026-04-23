package com.practice.stockapp.data.remote.dto

import com.practice.stockapp.domain.model.Stock
import com.squareup.moshi.Json

data class StockDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "ticker") val ticker: String,
    @field:Json(name = "price") val price: Double,
    @field:Json(name = "changePercent") val changePercent: Double
)

fun StockDto.toDomain(): Stock {
    return Stock(
        id = id,
        name = name,
        ticker = ticker,
        price = price,
        changePercent = changePercent
    )
}