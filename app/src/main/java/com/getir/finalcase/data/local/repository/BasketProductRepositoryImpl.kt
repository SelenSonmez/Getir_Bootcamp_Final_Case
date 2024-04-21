package com.getir.finalcase.data.local.repository

import android.util.Log
import com.getir.finalcase.data.local.database.dao.BasketProductDao
import com.getir.finalcase.data.local.database.entity.BasketProductEntity
import com.getir.finalcase.data.local.database.entity.ProductEntity
import com.getir.finalcase.domain.model.BasketProduct
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.domain.model.BaseResponse
import com.getir.finalcase.domain.model.toBasketProductEntity
import com.getir.finalcase.domain.model.toProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BasketProductRepositoryImpl @Inject constructor(
    private val basketProductDao: BasketProductDao
) {
    suspend fun sss(product: Product): BaseResponse<String> {
        val basketProduct = basketProductDao.getBasketProductById(productId = product.id!!)

        return if (basketProduct != null) {
            basketProductDao.insertBasketProduct(basketProduct)
            BaseResponse.Success("Product has been added to the basket")
        } else {
            BaseResponse.Error("Basket product not found")
        }
    }

    suspend fun addProductToBasketIfFound(product: Product): BaseResponse<String> {
        val productEntity = product.toProductEntity()
        basketProductDao.insertProduct(productEntity)

        // After inserting the product, add it to the basket
        val basketProductEntity = BasketProduct(product, 1).toBasketProductEntity()
        basketProductDao.insertBasketProduct(basketProductEntity)

        return BaseResponse.Success("Product added to the basket")

    }

    suspend fun removeProductFromBasket(product: Product): BaseResponse<String> {
        val existingProduct = basketProductDao.getBasketProductById(product.id!!)
        return if (existingProduct != null) {
            if (existingProduct.count > 1) {
                // More than one item, decrease count
                basketProductDao.decreaseProductCount(product.id)
                BaseResponse.Success("Product count decreased in the basket")
            } else {
                // Only one item, remove from basket
                basketProductDao.deleteBasketProductById(product.id)
                BaseResponse.Success("Product removed from the basket")
            }
        } else {
            BaseResponse.Error("Product not found in the basket")
        }
    }

  /*  fun getAllBasketProducts(): Flow<List<BasketProduct>> {
        return basketProductDao.getAllBasketProducts().map { basketProductEntities ->
            basketProductEntities.map { entity ->
                BasketProduct(
                    product = basketProductDao.getBasketProductById(entity.productId) ?: Product("", "", ""),
                    count = entity.count
                )
            }
        }
    }*/

    suspend fun deleteAllBasketProducts(): BaseResponse<String> {
        basketProductDao.deleteAllBasketProducts()
        return BaseResponse.Success("All products removed from the basket")
    }

    suspend fun increaseProductCount(productId: String): BaseResponse<String> {
        val existingProduct = basketProductDao.getBasketProductById(productId)
        return if (existingProduct != null) {
            basketProductDao.increaseProductCount(productId)
            BaseResponse.Success("Product count increased in the basket")
        } else {
            BaseResponse.Error("Product not found in the basket")
        }
    }

    suspend fun getBasketProductById(productId: String): BaseResponse<String> {
        val existingProduct = basketProductDao.getBasketProductById(productId)
        return if (existingProduct != null) {
            BaseResponse.Success("Product Found")
        } else {
            BaseResponse.Error("Product Not Found")
        }
    }
}
