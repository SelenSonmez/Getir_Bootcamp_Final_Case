package com.getir.finalcase.data.remote

import com.getir.finalcase.domain.model.Product
import com.getir.finalcase.domain.model.ProductCategory
import retrofit2.Call
import retrofit2.http.GET

/**
 * Interface defining API endpoints for fetching product data from the remote server.
 */
interface ApiService {

    /**
     * Retrieves a list of products grouped by categories from the server.
     *
     * @return A Retrofit Call object for fetching the product list.
     */
    @GET("products")
    fun getProductList(): Call<List<ProductCategory>>

    /**
     * Retrieves a list of suggested products from the server.
     *
     * @return A Retrofit Call object for fetching the horizontal product list.
     */
    @GET("suggestedProducts")
    fun getHorizontalProductList(): Call<List<ProductCategory>>
}

