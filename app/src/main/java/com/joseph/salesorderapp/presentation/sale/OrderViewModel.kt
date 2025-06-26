package com.joseph.salesorderapp.presentation.sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.data.local.preferences.AppPreferences
import com.joseph.salesorderapp.domain.model.OrderItem
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.presentation.UiEventManager
import com.joseph.salesorderapp.util.PrinterHelper
import com.joseph.salesorderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: AppRepository,
    private val uiEventManager: UiEventManager,
    private val appPreferences: AppPreferences,
    private val printerHelper: PrinterHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderState())
    val uiState: StateFlow<OrderState> = _uiState

    private val customerSearchQuery = MutableStateFlow("")
    private val productSearchQuery = MutableStateFlow("")

    private val _requestProductFocus = MutableStateFlow(false)
    val requestProductFocus: StateFlow<Boolean> = _requestProductFocus

    private val _requestQuantityFocus = MutableStateFlow(false)
    val requestQuantityFocus: StateFlow<Boolean> = _requestQuantityFocus

    init {
        observeCustomerSearch()
        observeProductSearch()
        fetchNextOrderId()
    }


    private fun fetchNextOrderId() {
        viewModelScope.launch {
            repository.fetchLastOrderNo().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                    }

                    is Resource.Success -> {
                        val tableId = (result.data ?: 0) + 1
                        _uiState.update { currentState ->
                            currentState.copy(nextOrderID = tableId)
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(message = result.message ?: "Unknown error")
                        }
                    }
                }
            }
        }

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

    private fun observeProductSearch() {
        viewModelScope.launch {
            productSearchQuery
                .debounce(150)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    repository.fetchProducts(query)
                        .flowOn(Dispatchers.IO)
                }
                .collect { result ->
                    if (result is Resource.Success) {
                        _uiState.update { it.copy(products = result.data ?: emptyList()) }
                    }
                }
        }
    }

    fun updateCustomerSearch(query: String) {
        customerSearchQuery.value = query
    }

    fun updateProductSearch(query: String) {
        productSearchQuery.value = query
    }

    fun selectCustomer(customer: CustomerEntity?) {
        _uiState.update { it.copy(selectedCustomer = customer) }
    }

    fun selectProduct(product: ProductEntity?) {
        _uiState.update { it.copy(selectedProduct = product) }
        if (product != null) {
            _requestQuantityFocus.value = true
        }
    }

    fun removeOrderItem(index: Int) {
        viewModelScope.launch {
            val updatedItems = _uiState.value.orderItems.toMutableList().apply {
                removeAt(index)
            }
            _uiState.update { it.copy(orderItems = updatedItems) }
            uiEventManager.showToast("Removed")
        }

    }


    fun selectPaymentMode(mode: String?) {
        _uiState.update { it.copy(selectedPaymentMode = mode) }
    }


    fun clearRequestQuantityFocus() {
        _requestQuantityFocus.value = false
    }

    fun updateQuantity(quantity: String) {
        _uiState.update { it.copy(quantity = quantity) }
    }

    fun addProductToOrder() {
        val state = _uiState.value
        val product = state.selectedProduct ?: return
        val quantity = state.quantity.toIntOrNull() ?: return

        val newItem = OrderItem(product, quantity)
        _uiState.update {
            it.copy(
                orderItems = it.orderItems + newItem,
                selectedProduct = null,
                quantity = ""
            )
        }
        _requestProductFocus.value = true
    }

    fun resetProductFocusRequest() {
        _requestProductFocus.value = false
    }

    fun onBackPress() {
        viewModelScope.launch {
            uiEventManager.navigateUp()
        }
    }

    fun clearState() {
        _uiState.value = OrderState()
    }

    fun saveOrder() {
        val state = _uiState.value
        val totalAmount = state.orderItems.sumOf { it.product.sellingPrice * it.quantity }

        viewModelScope.launch {
            val userID = appPreferences.getString(AppPreferences.KEY_USER_ID).first()
            val insertedId: Long = repository.insertOrderSummary(
                "SO${state.nextOrderID}",
                state.selectedCustomer,
                state.orderItems.size,
                totalAmount,
                state.selectedPaymentMode.toString(),
                userID.toString(),
            )

            repository.insertOrderDetails(
                state.orderItems,
                insertedId,
                state.selectedCustomer,
                userID.toString()
            )
            uiEventManager.showToast("Oder saved successfully")
            printReceipt(insertedId)
        }
    }

    fun printReceipt(insertedId: Long) {
        viewModelScope.launch {
            try {
                val summaryResult = repository
                    .fetchOrderSummaryById(insertedId.toInt())
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
                                newOrder()
                            },
                            onFailure = { error ->
                                uiEventManager.showToast("Print failed: $error")
                                newOrder()
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

    fun newOrder() {
        fetchNextOrderId()
        clearState()
    }
}

