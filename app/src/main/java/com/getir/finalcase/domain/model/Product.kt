package com.getir.finalcase.domain.model

import com.getir.finalcase.data.local.database.entity.ProductEntity
import java.io.Serializable

data class Product(
    val id: String,
    val name: String? = null,
    val attribute: String? = null,
    val thumbnailURL: String? = null,
    val squareThumbnailURL: String? = null,
    val imageURL: String? = null,
    val price: Double? = 0.0,
    val priceText: String? = null,
    var amount: Int = 0
) : Serializable

fun Product.toProductEntity() = ProductEntity(
    id = this.id,
    name = this.name,
    attribute = this.attribute,
    thumbnailURL = this.thumbnailURL,
    squareThumbnailURL = this.squareThumbnailURL,
    imageURL = this.imageURL,
    price = this.price,
    priceText = this.priceText,
    amount = this.amount

)

data class ProductCategory(
    var id: String?,
    var name: String? = null,
    val productCount: Int? = 0,
    val products: List<Product>? = null
) : Serializable
