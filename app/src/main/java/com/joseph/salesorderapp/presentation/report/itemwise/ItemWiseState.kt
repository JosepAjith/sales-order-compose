package com.joseph.salesorderapp.presentation.report.itemwise

import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.domain.model.ItemWiseReport
import com.joseph.salesorderapp.util.DateUtils
import java.util.Date

data class ItemWiseState(
    val error: String? = null,
    val success: Boolean = false,
    val isLoading: Boolean=false,
    val itemList: List<ItemWiseReport> = emptyList(),
    val fromDate: Date = Date(),
    val toDate: Date = Date(),
    val fromDateFormatted: String = DateUtils.formatDate(Date()),
    val toDateFormatted: String = DateUtils.formatDate(Date()),
    val showDateFilterDialog: Boolean = false,
    val selectedCustomer: CustomerEntity? = null,
    val customers: List<CustomerEntity> = emptyList(),
)