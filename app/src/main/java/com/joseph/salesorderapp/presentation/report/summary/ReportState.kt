package com.joseph.salesorderapp.presentation.report.summary


import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity
import com.joseph.salesorderapp.util.DateUtils
import java.util.Date


data class ReportState(
    val allOrders: List<OrderSummaryEntity> = emptyList(),
    val fromDate: Date = Date(),
    val toDate: Date = Date(),
    val fromDateFormatted: String = DateUtils.formatDate(Date()),
    val toDateFormatted: String = DateUtils.formatDate(Date()),
    val showDateFilterDialog: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)
