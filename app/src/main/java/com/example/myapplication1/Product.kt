package com.example.myapplication1

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String? = null
)

@Serializable
data class Product(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("price")
    val price: Double,
    @SerialName("category_id")
    val category_id: Long,
    @SerialName("in_stock")
    val in_stock: Boolean
)

@Serializable
data class CartItem(
    val id: Long,
    val product_id: Long,
    var quantity: Int
)
