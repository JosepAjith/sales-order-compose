package com.joseph.salesorderapp.presentation.sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.remote.model.OrderItem
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: AppRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderState())
    val uiState: StateFlow<OrderState> = _uiState

    init {
        loadDropDown()
    }

    private fun loadDropDown() {
        viewModelScope.launch {

            repository.fetchCustomers().collect { result ->
                if (result is Resource.Success) {
                    _uiState.update {
                        it.copy(
                            customers = result.data ?: emptyList(),
                        )
                    }
                }
            }

            repository.fetchProducts().collect { result ->
                if (result is Resource.Success) {
                    _uiState.update {
                        it.copy(
                            products = result.data ?: emptyList(),
                        )
                    }
                }
            }
        }
    }

    fun selectCustomer(customer: CustomerEntity?) {
        _uiState.update { it.copy(selectedCustomer = customer) }
    }

    fun selectProduct(product: ProductEntity?) {
        _uiState.update { it.copy(selectedProduct = product) }
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
    }

    fun saveOrder() {
        _uiState.update { it.copy(isOrderSaved = true) }
    }
}
