package com.joseph.salesorderapp.presentation.dashboard

import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity
import com.joseph.salesorderapp.data.remote.model.CustomersItem
import com.joseph.salesorderapp.data.remote.model.ProductDataItem


data class DashBoardState(
    val error: String? = null,
    val success: Boolean = false,
    val customers: List<CustomersItem> = emptyList(),
    val products: List<ProductDataItem> = emptyList(),
    val unsyncedCustomersCount: Int? = 0,
    val unsyncedOrdersCount: Int? = 0,
    val isSyncing: Boolean = false,
    val unsyncedOrders: List<OrderSummaryEntity> = emptyList(),
    val itemList: List<OrderDetailsEntity> = emptyList(),
    val customerList: List<CustomerEntity> = emptyList()
)