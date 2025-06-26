package com.joseph.salesorderapp.presentation.report.itemwise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.presentation.UiEventManager
import com.joseph.salesorderapp.util.DateUtils
import com.joseph.salesorderapp.util.PrinterHelper
import com.joseph.salesorderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ItemWiseViewModel @Inject constructor(
    private val repository: AppRepository,
    private val uiEventManager: UiEventManager,
    private val printerHelper: PrinterHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemWiseState())
    val uiState: StateFlow<ItemWiseState> = _uiState

    private val customerSearchQuery = MutableStateFlow("")

    init {
        val today = Date()
        pickFromDate(today)
        pickToDate(today)
        observeCustomerSearch()
        loadItems()
    }

    private fun observeCustomerSearch() {
        viewModelScope.launch {
            customerSearchQuery
                .debounce(150)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    repository.fetchCustomers(query)
                }
                .collect { result ->
                    if (result is Resource.Success) {
                        _uiState.update { it.copy(customers = result.data ?: emptyList()) }
                    }
                }
        }
    }

    fun selectCustomer(customer: CustomerEntity?) {
        _uiState.update { it.copy(selectedCustomer = customer) }
    }

    fun updateCustomerSearch(query: String) {
        customerSearchQuery.value = query
    }


    private fun loadItems() {
        viewModelScope.launch {
            uiEventManager.showLoader("Loading..", true)
            repository.fetchItemWiseReport(
                _uiState.value.fromDateFormatted,
                _uiState.value.toDateFormatted,
                _uiState.value.selectedCustomer?.serverId,
            ).collect { result ->
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

    fun searchItems() {
        viewModelScope.launch {
            loadItems()
        }
    }


    fun onBackPress() {
        viewModelScope.launch {
            uiEventManager.navigateUp()
        }
    }

    fun printReport() {
        viewModelScope.launch {
            try {
                printerHelper.printItemWiseReport(
                    customer = uiState.value.selectedCustomer,
                    orderItems = uiState.value.itemList,
                    fromDate = uiState.value.fromDateFormatted,
                    toDate = uiState.value.fromDateFormatted,
                    onSuccess = {
                        uiEventManager.showToast("Printed successfully")
                    },
                    onFailure = { error ->
                        uiEventManager.showToast("Print failed: $error")
                    }
                )
            } catch (e: Exception) {
                uiEventManager.showToast("Error: ${e.message}")
            } finally {
                uiEventManager.showLoader("", false)
            }
        }
    }
}