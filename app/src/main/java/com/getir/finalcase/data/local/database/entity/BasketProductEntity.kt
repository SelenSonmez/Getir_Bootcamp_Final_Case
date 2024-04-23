/*
package com.getir.finalcase.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.getir.finalcase.domain.model.BasketProduct
import com.getir.finalcase.domain.model.Product

@Entity(
    tableName = "basket_products",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BasketProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String,
    val count: Int
)*/
