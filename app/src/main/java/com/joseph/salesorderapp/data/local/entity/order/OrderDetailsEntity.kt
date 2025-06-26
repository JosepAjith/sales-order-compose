package com.joseph.salesorderapp.data.local.entity.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_details")
data class OrderDetailsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Long,
    val productName: String,
    val productID: Int,
    val quantity: Int,
    val taxPercentage: Double,
    val productCode: String,
    val pricePerUnit: Double,
    val orderDate: String,
    val customerName: String,
    val customerID: Int,
    val userID: Int,
    val discount: Double,
    val totalPrice: Double,
    val isSynced: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)