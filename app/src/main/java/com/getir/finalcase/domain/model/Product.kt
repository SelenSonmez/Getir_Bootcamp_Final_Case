package com.getir.finalcase.domain.model

import com.google.gson.annotations.SerializedName

import android.os.Parcel
import android.os.Parcelable
import com.getir.finalcase.data.local.database.entity.ProductEntity
import java.io.Serializable

data class Product(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String? = null,
    @SerializedName("attribute") val attribute: String? = null,
    @SerializedName("thumbnailURL") val thumbnailURL: String? = null,
    @SerializedName("imageURL") val imageURL: String? = null,
    @SerializedName("price") val price: Double? = 0.0,
    @SerializedName("priceText") val priceText: String? = null
) : Serializable

fun Product.toProductEntity() = ProductEntity(
    id = this.id,
    name = this.name,
    attribute = this.attribute,
    thumbnailURL = this.thumbnailURL,
    imageURL = this.imageURL,
    price = this.price,
    priceText = this.priceText

)

data class ProductCategory (
    @SerializedName("id")
     val id: String? = null,

    @SerializedName("name")
     val name: String? = null,

    @SerializedName("productCount")
     val productCount: Int = 0,

    @SerializedName("products")
     val products: List<Product>? = null
): Serializable
