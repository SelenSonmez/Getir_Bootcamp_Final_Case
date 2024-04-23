
package com.getir.finalcase.domain.repository

import com.getir.finalcase.data.remote.ApiService
import com.getir.finalcase.data.remote.CallBack
import com.getir.finalcase.di.IoDispatcher
import com.getir.finalcase.domain.model.BaseResponse
import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.domain.model.ProductCategory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductListRepository @Inject constructor(
    private val apiService: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    fun getAllProducts(): Flow<BaseResponse<List<ProductCategory>>> = callbackFlow<BaseResponse<List<ProductCategory>>> {
        apiService.getProductList().enqueue(CallBack(this.channel))
        awaitClose { close() }
    }.flowOn(ioDispatcher)

    fun getAllHorizontalProducts(): Flow<BaseResponse<List<ProductCategory>>> = callbackFlow<BaseResponse<List<ProductCategory>>> {
        apiService.getHorizontalProductList().enqueue(CallBack(this.channel))
        awaitClose { close() }
    }.flowOn(ioDispatcher)




}

