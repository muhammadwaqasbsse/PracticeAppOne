package com.practice.stockapp.domain.model

data class Stock(
    val id: String,
    val name: String,
    val ticker: String,
    val price: Double,
    val changePercent: Double
)
