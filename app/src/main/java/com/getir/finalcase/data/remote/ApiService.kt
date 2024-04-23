package com.getir.finalcase.data.remote

import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.domain.model.ProductCategory
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("products")
     fun getProductList(): Call<List<ProductCategory>>
    @GET("suggestedProducts")
    fun getHorizontalProductList(): Call<List<ProductCategory>>

}
