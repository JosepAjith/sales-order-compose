package com.joseph.salesorderapp.presentation.dashboard

import com.joseph.salesorderapp.data.remote.model.CustomersItem
import com.joseph.salesorderapp.data.remote.model.ProductDataItem


data class DashBoardState(
    val error: String? = null,
    val success: Boolean = false,
    val customers: List<CustomersItem> = emptyList(),
    val products: List<ProductDataItem> = emptyList()
)