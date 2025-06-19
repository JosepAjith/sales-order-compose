package com.joseph.salesorderapp.presentation.report.details

import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity

data class ReportDetailsState(
    val error: String? = null,
    val success: Boolean = false,
    val isLoading: Boolean=false,
    val orderSummary: OrderSummaryEntity? = null,
    val itemList: List<OrderDetailsEntity> = emptyList()
)