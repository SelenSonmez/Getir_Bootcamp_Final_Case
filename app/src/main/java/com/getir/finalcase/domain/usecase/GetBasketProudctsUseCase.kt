/*
package com.getir.finalcase.domain.usecase

import com.getir.finalcase.common.domain.NoParaMeterUseCase
import com.getir.finalcase.data.local.repository.BasketProductRepository
import com.getir.finalcase.domain.model.BaseResponse
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.domain.model.ProductCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBasketProductsUseCase @Inject constructor(
    private val basketProductRepository: BasketProductRepository
): NoParaMeterUseCase<Flow<BaseResponse<List<Product>>>>{
    override fun execute(): Flow<BaseResponse<List<Product>>> = basketProductRepository.getAllBasketProducts()


}*/
