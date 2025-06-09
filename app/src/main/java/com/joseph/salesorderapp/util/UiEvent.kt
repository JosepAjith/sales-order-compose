package com.joseph.salesorderapp.util


sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class ShowSnackbar(val message: String, val actionLabel: String? = null) : UiEvent()
    object NavigateUp : UiEvent()

    data class Navigate(
        val route: String,
        val popUpToRoute: String? = null,
        val popUpToInclusive: Boolean = false
    ) : UiEvent()
}
