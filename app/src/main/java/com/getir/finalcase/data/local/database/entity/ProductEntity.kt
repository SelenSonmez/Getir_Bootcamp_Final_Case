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
    val imageURL: String?,
    val price: Double?,
    val priceText: String?
)

fun ProductEntity.toProduct(): Product =
    Product(
        id = this.id,
        name = this.name,
        attribute = this.attribute,
        thumbnailURL = this.thumbnailURL,
        imageURL = this.imageURL,
        price = this.price,
        priceText = this.priceText
    )