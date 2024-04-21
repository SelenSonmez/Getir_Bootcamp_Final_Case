package com.getir.finalcase.domain.model

import com.google.gson.annotations.SerializedName

import android.os.Parcel
import android.os.Parcelable

data class Product(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("attribute") val attribute: String? = null,
    @SerializedName("thumbnailURL") val thumbnailURL: String? = null,
    @SerializedName("imageURL") val imageURL: String? = null,
    @SerializedName("price") val price: Double = 0.0,
    @SerializedName("priceText") val priceText: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(attribute)
        parcel.writeString(thumbnailURL)
        parcel.writeString(imageURL)
        parcel.writeDouble(price)
        parcel.writeString(priceText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
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
