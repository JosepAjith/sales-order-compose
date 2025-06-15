package com.joseph.salesorderapp.presentation.customer

import com.joseph.salesorderapp.data.local.entity.CustomerEntity

data class CustomerState(
    val error: String? = null,
    val success: Boolean = false,
//    val customers: List<CustomersItem> = emptyList()
    val customers: List<CustomerEntity> = emptyList()
)