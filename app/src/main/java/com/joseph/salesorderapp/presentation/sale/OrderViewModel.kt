package com.joseph.salesorderapp.presentation.sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.domain.model.OrderItem
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.presentation.UiEventManager
import com.joseph.salesorderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: AppRepository,
    private val uiEventManager: UiEventManager
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

    fun saveOrder() {
        val state = _uiState.value
        val totalAmount = state.orderItems.sumOf { it.product.sellingPrice * it.quantity }

        viewModelScope.launch {
            val insertedId: Long = repository.insertOrderSummary(
                state.selectedCustomer?.name.toString(),
                state.orderItems.size,
                totalAmount
            )

            repository.insertOrderDetails(state.orderItems, insertedId)
            uiEventManager.showSnackbar("Oder saved successfully")
            _uiState.value = OrderState()
        }
    }
}

