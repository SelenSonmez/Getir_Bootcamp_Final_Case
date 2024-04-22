package com.getir.finalcase.domain.model

import com.getir.finalcase.data.local.database.entity.BasketProductEntity

data class BasketProduct (
    val product: Product,
    val count: Int
)

fun BasketProduct.toBasketProductEntity() =
    BasketProductEntity(
        productId = this.product.id,
        count = this.count
    )