package com.getir.finalcase.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.getir.finalcase.data.local.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
/**
 * Data Access Object (DAO) interface for interacting with the basket products in the local database.
 */
@Dao
interface BasketProductDao {

    /**
     * Inserts a product entity into the database.
     *
     * @param productEntity The product entity to be inserted.
     * @return The ID of the inserted product entity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productEntity: ProductEntity): Long

    /**
     * Inserts a basket product entity into the database.
     *
     * @param basketProductEntity The basket product entity to be inserted.
     * @return The ID of the inserted basket product entity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBasketProduct(basketProductEntity: ProductEntity): Long

    /**
     * Retrieves all basket products from the database.
     *
     * @return A Flow of list of basket product entities.
     */
    @Query("SELECT * FROM products")
    fun getAllBasketProducts(): Flow<List<ProductEntity>>

    /**
     * Retrieves a product entity by its ID from the database.
     *
     * @param productId The ID of the product.
     * @return The retrieved product entity, or null if not found.
     */
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: String): ProductEntity?

    /**
     * Deletes a basket product from the database by its ID.
     *
     * @param productId The ID of the product to be deleted.
     */
    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteBasketProductById(productId: String)

    /**
     * Deletes all basket products from the database.
     */
    @Query("DELETE FROM products")
    suspend fun deleteAllBasketProducts()

    /**
     * Retrieves a product entity by its ID from the database.
     *
     * @param productId The ID of the product.
     * @return The retrieved product entity.
     */
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductFromId(productId: String): ProductEntity

    /**
     * Decreases the count of a product in the basket by the specified amount.
     *
     * @param productId The ID of the product.
     * @param decrementAmount The amount to decrement.
     */
    @Query("UPDATE products SET amount = amount - :decrementAmount WHERE id = :productId")
    suspend fun decreaseProductCount(productId: String, decrementAmount: Int = 1)

    /**
     * Increases the count of a product in the basket by the specified amount.
     *
     * @param productId The ID of the product.
     * @param incrementAmount The amount to increment.
     */
    @Query("UPDATE products SET amount = amount + :incrementAmount WHERE id = :productId")
    suspend fun increaseProductCount(productId: String, incrementAmount: Int = 1)

}
