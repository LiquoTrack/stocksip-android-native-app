package com.liquotrack.stocksip.features.procurementordering.purchaseorders.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_cart")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: String,
    val productName: String,
    val unitPrice: Double,
    val imageUrl: String?,
    val quantity: Int,
    val subTotal: Double,
    val addedAt: Long = System.currentTimeMillis()
)
