package com.joseph.salesorderapp.domain.model

data class ItemWiseReport(
    val productId: Int,
    val productName: String,
    val productCode: String,
    val totalQty: Int,
    val totalAmount: Double
)