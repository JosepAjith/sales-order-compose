package com.joseph.salesorderapp.data.local.entity.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_summary")
data class OrderSummaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerName: String,
    val customerID: Int,
    val orderID: String,
    val userID: Int,
    val totalItems: Int,
    val totalAmount: Double,
    val orderDate: String,
    val orderTime: String,
    val paymentMode: String,
    val isSynced: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
