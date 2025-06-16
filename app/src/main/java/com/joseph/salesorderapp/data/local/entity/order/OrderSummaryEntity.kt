package com.joseph.salesorderapp.data.local.entity.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_summary")
data class OrderSummaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerName: String,
    val totalItems: Int,
    val totalAmount: Double,
    val isSynced: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
