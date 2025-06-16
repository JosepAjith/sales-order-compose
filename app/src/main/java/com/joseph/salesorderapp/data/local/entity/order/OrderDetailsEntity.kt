package com.joseph.salesorderapp.data.local.entity.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_details")
data class OrderDetailsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Long,
    val productName: String,
    val quantity: Int,
    val pricePerUnit: Double,
    val totalPrice: Double,
    val isSynced: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)