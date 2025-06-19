package com.joseph.salesorderapp.presentation.report.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.presentation.UiEventManager
import com.joseph.salesorderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReportDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AppRepository,
    private val uiEventManager: UiEventManager
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
}