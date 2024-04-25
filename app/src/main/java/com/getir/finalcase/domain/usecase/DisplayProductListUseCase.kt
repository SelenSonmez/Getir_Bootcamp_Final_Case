package com.getir.finalcase.domain.usecase

import com.getir.finalcase.common.domain.NoParaMeterUseCase
import com.getir.finalcase.domain.model.BaseResponse
import com.getir.finalcase.domain.model.ProductCategory
import com.getir.finalcase.domain.repository.ProductListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductListUseCase @Inject constructor(
    private val productListRepository: ProductListRepository,
): NoParaMeterUseCase<Flow<BaseResponse<List<ProductCategory>>>>{
    override fun execute(): Flow<BaseResponse<List<ProductCategory>>> = productListRepository.getAllProducts()

}