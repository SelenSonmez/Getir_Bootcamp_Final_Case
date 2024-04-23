package com.getir.finalcase.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.getir.finalcase.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String?,
    val attribute: String?,
    val thumbnailURL: String?,
    val squareThumbnailURL: String?,
    val imageURL: String?,
    val price: Double?,
    val priceText: String?,
    val amount: Int = 0
)

fun ProductEntity.toProduct(): Product =
    Product(
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