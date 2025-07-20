package com.joseph.salesorderapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val serverId: Int = 0,

    val name: String,
    val barcode: String = "",
    val category: String = "",
    val productCode: String = "",
    val brand: String = "",
    val description: String = "",

    val purchasePrice: Double = 0.0,
    var sellingPrice: Double = 0.0,

    val stockQty: Int = 0,
    val unit: String = "pcs",

    val taxPercentage: Double = 0.0,
    val hsnCode: String = "",

    val isSynced: Boolean = false,
    val isActive: Boolean = true,

    val createdAt: String = "",
    val updatedAt: String = ""
)
