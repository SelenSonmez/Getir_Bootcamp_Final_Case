package com.getir.finalcase.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.getir.finalcase.data.local.database.dao.BasketProductDao
import com.getir.finalcase.data.local.database.entity.BasketProductEntity
import com.getir.finalcase.data.local.database.entity.ProductEntity
import dagger.hilt.android.AndroidEntryPoint

@Database(entities = [BasketProductEntity::class, ProductEntity::class], version = 2)
abstract class BasketRoomDatabase : RoomDatabase() {
    abstract fun basketProductDao(): BasketProductDao
}
