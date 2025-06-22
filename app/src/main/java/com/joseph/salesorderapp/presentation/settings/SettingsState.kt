package com.joseph.salesorderapp.presentation.settings


data class SettingsState(
    val printerName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val printNameError: String? = null,
)
