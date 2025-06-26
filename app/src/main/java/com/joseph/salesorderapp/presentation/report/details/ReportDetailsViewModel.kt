package com.joseph.salesorderapp.presentation.report.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.presentation.UiEventManager
import com.joseph.salesorderapp.util.PrinterHelper
import com.joseph.salesorderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReportDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AppRepository,
    private val uiEventManager: UiEventManager,
    private val printerHelper: PrinterHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportDetailsState())
    val uiState: StateFlow<ReportDetailsState> = _uiState

    init {
        val orderId = savedStateHandle.get<Int>("orderId") ?: 0
        loadOrders(orderId)
    }


    private fun loadOrders(orderId: Int) {
        viewModelScope.launch {
            uiEventManager.showLoader("Loading..", true)
            repository.fetchOrderSummaryById(orderId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(error = null) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                orderSummary = result.data,
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

            repository.fetchReportItemList(orderId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(error = null) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                itemList = result.data ?: emptyList(),
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


    fun onBackPress() {
        viewModelScope.launch {
            uiEventManager.navigateUp()
        }
    }

    fun printReceipt() {
        viewModelScope.launch {
            try {

                val summaryResult = repository
                    .fetchOrderSummaryById(uiState.value.orderSummary?.id ?: 0)
                    .first { it !is Resource.Loading }

                if (summaryResult is Resource.Success && summaryResult.data != null) {
                    val summary = summaryResult.data

                    val detailsResult = repository
                        .fetchReportItemList(summary.id)
                        .first { it !is Resource.Loading }

                    if (detailsResult is Resource.Success) {
                        val fetchedList = detailsResult.data
                        val details: List<OrderDetailsEntity> = fetchedList ?: emptyList()

                        printerHelper.printOrderReport(
                            summary = summary,
                            orderItems = details,
                            onSuccess = {
                                uiEventManager.showToast("Printed successfully")
                            },
                            onFailure = { error ->
                                uiEventManager.showToast("Print failed: $error")
                            }
                        )
                    } else {
                        uiEventManager.showToast("Failed to fetch order details")
                    }

                } else {
                    uiEventManager.showToast("No order summary found")
                }
            } catch (e: Exception) {
                uiEventManager.showToast("Error: ${e.message}")
            } finally {
                uiEventManager.showLoader("", false)
            }
        }
    }
}