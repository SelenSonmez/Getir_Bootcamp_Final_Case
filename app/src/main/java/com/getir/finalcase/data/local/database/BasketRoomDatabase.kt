package com.getir.finalcase.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.getir.finalcase.data.local.database.dao.BasketProductDao
import com.getir.finalcase.data.local.database.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 2, exportSchema = false)
abstract class BasketRoomDatabase : RoomDatabase() {
    abstract fun basketProductDao(): BasketProductDao
}
