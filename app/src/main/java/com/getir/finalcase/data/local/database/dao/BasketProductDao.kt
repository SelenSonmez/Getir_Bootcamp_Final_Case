package com.getir.finalcase.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.getir.finalcase.data.local.database.entity.BasketProductEntity
import com.getir.finalcase.data.local.database.entity.ProductEntity
import com.getir.finalcase.domain.model.BasketProduct
import kotlinx.coroutines.flow.Flow
@Dao
interface BasketProductDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertProduct(productEntity: ProductEntity): Long

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertBasketProduct(basketProductEntity: BasketProductEntity): Long
   @Query("SELECT * FROM basket_products")
    fun getAllBasketProducts(): Flow<List<BasketProductEntity>>

    @Query("SELECT * FROM basket_products WHERE productId = :productId")
    suspend fun getBasketProductById(productId: String): BasketProductEntity?

    @Query("DELETE FROM basket_products WHERE productId = :productId")
    suspend fun deleteBasketProductById(productId: String)

    @Query("DELETE FROM basket_products")
    suspend fun deleteAllBasketProducts()

    @Query("UPDATE basket_products SET count = count - :decrementAmount WHERE productId = :productId")
    suspend fun decreaseProductCount(productId: String, decrementAmount: Int = 1)

    @Query("UPDATE basket_products SET count = count + :incrementAmount WHERE productId = :productId")
    suspend fun increaseProductCount(productId: String, incrementAmount: Int = 1)

}
