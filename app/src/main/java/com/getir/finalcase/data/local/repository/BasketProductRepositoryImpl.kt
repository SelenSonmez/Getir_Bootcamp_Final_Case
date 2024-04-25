package com.getir.finalcase.data.local.repository

import com.getir.finalcase.data.local.database.dao.BasketProductDao
import com.getir.finalcase.data.local.database.entity.toProduct
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.domain.model.BaseResponse
import com.getir.finalcase.domain.model.toProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BasketProductRepositoryImpl @Inject constructor(
    private val basketProductDao: BasketProductDao
) {
    suspend fun addProductToBasketIfFound(product: Product): BaseResponse<String> {
        val existingProduct = basketProductDao.getProductById(product.id)
        if(existingProduct != null) {
            basketProductDao.increaseProductCount(existingProduct.id,1)
            return BaseResponse.Success("Product Count increased")
        }
        val productEntity = product.toProductEntity()
        basketProductDao.insertProduct(productEntity)

        return BaseResponse.Success("Product added to the basket")
    }

    suspend fun removeProductFromBasket(product: Product): BaseResponse<String> {
        val existingProduct = basketProductDao.getProductById(product.id)
        return if (existingProduct != null) {
            if (existingProduct.amount > 1) {
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

    suspend fun getAllProductsInBasket(): Flow<BaseResponse<List<Product>>> {
        return basketProductDao.getAllBasketProducts().map {productEntities ->
            val productsInBasket = productEntities.map {productEntity ->
                productEntity.toProduct()
            }
            BaseResponse.Success(productsInBasket)
        }
    }

    suspend fun deleteAllBasketProducts(): BaseResponse<String> {
        basketProductDao.deleteAllBasketProducts()
        return BaseResponse.Success("All products removed from the basket")
    }

    suspend fun increaseProductCount(productId: String): BaseResponse<String> {
        val existingProduct = basketProductDao.getProductById(productId)
        return if (existingProduct != null) {
            basketProductDao.increaseProductCount(productId)
            BaseResponse.Success("Product count increased in the basket")
        } else {
            BaseResponse.Error("Product not found in the basket")
        }
    }

    suspend fun getBasketProductById(productId: String): BaseResponse<Product?> {
        val existingProduct = basketProductDao.getProductById(productId)
        return if (existingProduct != null) {
            val product = existingProduct.toProduct()
            BaseResponse.Success(product)
        } else {
            BaseResponse.Error("Product Not Found")
        }
    }
}
