package com.joseph.salesorderapp.util

import java.util.Date


sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class ShowSnackbar(val message: String, val actionLabel: String? = null) : UiEvent()
    object NavigateUp : UiEvent()

    data class Navigate(
        val route: String,
        val popUpToRoute: String? = null,
        val popUpToInclusive: Boolean = false
    ) : UiEvent()

    data class CircleLoader(val message: String, val isVisible: Boolean) : UiEvent()

    data class ShowDatePicker(
        val initialDate: Date,
        val onDateSelected: (Date) -> Unit
    ) : UiEvent()
}
