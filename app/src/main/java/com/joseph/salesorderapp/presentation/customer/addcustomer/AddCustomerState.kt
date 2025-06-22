package com.joseph.salesorderapp.presentation.customer.addcustomer

data class AddCustomerState(
    val name: String = "",
    val phoneNo: String = "",
    val address: String = "",
    val isLoading: Boolean = false,
    val nameError: String? = null,
    val phoneNoError: String? = null,
    val error: String? = null,
    val success: Boolean = false
)
