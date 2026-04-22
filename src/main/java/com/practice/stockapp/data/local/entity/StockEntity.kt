package com.practice.stockapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practice.stockapp.domain.model.Stock

@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey val id: String,
    val name: String,
    val ticker: String,
    val price: Double,
    val changePercent: Double
)

fun StockEntity.toDomain() = Stock(
    id = id,
    name = name,
    ticker = ticker,
    price = price,
    changePercent = changePercent
)

fun Stock.toEntity() = StockEntity(
    id = id,
    name = name,
    ticker = ticker,
    price = price,
    changePercent = changePercent
)
