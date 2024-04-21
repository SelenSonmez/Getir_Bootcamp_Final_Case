package com.getir.finalcase.data.local.repository

import com.getir.finalcase.di.IoDispatcher
import com.getir.finalcase.domain.model.BaseResponse
import com.getir.finalcase.domain.model.BasketProduct
import com.getir.finalcase.domain.model.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BasketProductRepository @Inject constructor(
    private val basketProductRepository: BasketProductRepositoryImpl,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun addProductToBasketIfFound(product: Product): BaseResponse<String> {
        return basketProductRepository.addProductToBasketIfFound(product)
    }



    suspend fun removeProductFromBasket(product: Product): BaseResponse<String> {
        return basketProductRepository.removeProductFromBasket(product)
    }

   /* fun getAllBasketProducts(): Flow<List<BasketProduct>> {
        return basketProductRepository.getAllBasketProducts()
    }*/

    suspend fun deleteAllBasketProducts(): BaseResponse<String> {
        return basketProductRepository.deleteAllBasketProducts()
    }

    suspend fun increaseProductCount(productId: String): BaseResponse<String> {
        return basketProductRepository.increaseProductCount(productId)
    }

    suspend fun getBasketProductById(productId: String): BaseResponse<String> {
        return basketProductRepository.getBasketProductById(productId)
    }
}
