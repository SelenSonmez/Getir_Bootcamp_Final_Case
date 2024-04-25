package com.getir.finalcase.data.local.repository

import com.getir.finalcase.domain.model.BaseResponse
import com.getir.finalcase.domain.model.Product
import javax.inject.Inject

/**
 * Repository class responsible for handling operations related to basket products in the local database.
 *
 * @property basketProductRepository The implementation of the BasketProductRepository interface.
 */
class BasketProductRepository @Inject constructor(
    private val basketProductRepository: BasketProductRepositoryImpl,
) {
    suspend fun addProductToBasket(product: Product): BaseResponse<String> {
        return basketProductRepository.addProductToBasketIfFound(product)
    }

    suspend fun removeProductFromBasket(product: Product): BaseResponse<String> {
        return basketProductRepository.removeProductFromBasket(product)
    }

    suspend fun deleteAllBasketProducts(): BaseResponse<String> {
        return basketProductRepository.deleteAllBasketProducts()
    }

    suspend fun increaseProductCount(productId: String): BaseResponse<String> {
        return basketProductRepository.increaseProductCount(productId)
    }

    suspend fun getBasketProductById(productId: String): BaseResponse<Product?> {
        return basketProductRepository.getBasketProductById(productId)
    }
}
