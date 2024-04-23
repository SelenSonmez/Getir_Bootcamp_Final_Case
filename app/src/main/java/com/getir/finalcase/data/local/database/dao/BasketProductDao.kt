package com.getir.finalcase.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.getir.finalcase.data.local.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface BasketProductDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertProduct(productEntity: ProductEntity): Long

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertBasketProduct(basketProductEntity: ProductEntity): Long
   @Query("SELECT * FROM products")
    fun getAllBasketProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: String): ProductEntity?

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteBasketProductById(productId: String)

    @Query("DELETE FROM products")
    suspend fun deleteAllBasketProducts()

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductFromId(productId: String): ProductEntity

    @Query("UPDATE products SET amount = amount - :decrementAmount WHERE id = :productId")
    suspend fun decreaseProductCount(productId: String, decrementAmount: Int = 1)

    @Query("UPDATE products SET amount = amount + :incrementAmount WHERE id = :productId")
    suspend fun increaseProductCount(productId: String, incrementAmount: Int = 1)

}
