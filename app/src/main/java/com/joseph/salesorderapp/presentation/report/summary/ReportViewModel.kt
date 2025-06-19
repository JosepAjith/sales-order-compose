package com.joseph.salesorderapp.presentation.report.summary


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.presentation.UiEventManager
import com.joseph.salesorderapp.presentation.navigation.Routes
import com.joseph.salesorderapp.util.DateUtils
import com.joseph.salesorderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: AppRepository,
    private val uiEventManager: UiEventManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportState())
    val uiState: StateFlow<ReportState> = _uiState

    init {
        val today = Date()
        pickFromDate(today)
        pickToDate(today)
        loadOrders()
    }


    fun pickFromDate(date: Date) {
        _uiState.update {
            it.copy(
                fromDate = date,
                fromDateFormatted = DateUtils.formatDate(date)
            )
        }
    }

    fun pickToDate(date: Date) {
        _uiState.update {
            it.copy(
                toDate = date,
                toDateFormatted = DateUtils.formatDate(date)
            )
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            uiEventManager.showLoader("Loading..", true)
            repository.fetchOrderSummary(
                _uiState.value.fromDateFormatted,
                _uiState.value.toDateFormatted
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(error = null) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                allOrders = result.data ?: emptyList(),
                                error = null,
                                success = true
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(error = result.message)
                        }
                    }
                }
            }
            uiEventManager.showLoader("", false)
        }
    }

    fun showFromDatePicker() {
        viewModelScope.launch {
            uiEventManager.showDatePicker(
                initialDate = _uiState.value.fromDate,
                onDateSelected = { pickFromDate(it) }
            )
        }
    }

    fun showToDatePicker() {
        viewModelScope.launch {
            uiEventManager.showDatePicker(
                initialDate = _uiState.value.toDate,
                onDateSelected = { pickToDate(it) }
            )
        }
    }

    fun searchOrders() {
        viewModelScope.launch {
            loadOrders()
        }
    }

    fun onOrderClicked(orderId: Int) {
        viewModelScope.launch {
            uiEventManager.navigate(route = Routes.ReportDetails.createRoute(orderId))
        }
    }


    fun onBackPress() {
        viewModelScope.launch {
            uiEventManager.navigateUp()
        }
    }
}
