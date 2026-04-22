package com.practice.stockapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practice.stockapp.data.local.dao.StockDao
import com.practice.stockapp.data.local.entity.StockEntity

@Database(entities = [StockEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
}
