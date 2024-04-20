package com.getir.finalcase.domain.model

import com.google.gson.annotations.SerializedName

class Product {
    @SerializedName("id")
     val id: String? = null

    @SerializedName("name")
     val name: String? = null

    @SerializedName("attribute")
     val attribute: String? = null

    @SerializedName("thumbnailURL")
     val thumbnailURL: String? = null

    @SerializedName("imageURL")
     val imageURL: String? = null

    @SerializedName("price")
     val price = 0.0

    @SerializedName("priceText")
     val priceText: String? = null // Getters and setters
}

class ProductCategory {
    @SerializedName("id")
     val id: String? = null

    @SerializedName("name")
     val name: String? = null

    @SerializedName("productCount")
     val productCount = 0

    @SerializedName("products")
     val products: List<Product>? = null // Getters and setters
}
