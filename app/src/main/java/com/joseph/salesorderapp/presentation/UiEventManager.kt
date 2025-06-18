package com.joseph.salesorderapp.presentation

import com.joseph.salesorderapp.util.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UiEventManager @Inject constructor() {
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    suspend fun showSnackbar(message: String, actionLabel: String? = null) {
        _eventFlow.emit(UiEvent.ShowSnackbar(message, actionLabel))
    }

    suspend fun showToast(message: String) {
        _eventFlow.emit(UiEvent.ShowToast(message))
    }

    suspend fun navigate(
        route: String,
        popUpToRoute: String? = null,
        popUpToInclusive: Boolean = false
    ) {
        _eventFlow.emit(UiEvent.Navigate(route, popUpToRoute, popUpToInclusive))
    }

    suspend fun navigateUp() {
        _eventFlow.emit(UiEvent.NavigateUp)
    }

    suspend fun showLoader(message: String, isVisible: Boolean){
        _eventFlow.emit(UiEvent.CircleLoader(message,isVisible))
    }

    suspend fun showDatePicker(
        initialDate: Date,
        onDateSelected: (Date) -> Unit
    ) {
        _eventFlow.emit(UiEvent.ShowDatePicker(initialDate, onDateSelected))
    }
}

