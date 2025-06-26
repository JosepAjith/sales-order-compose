package com.joseph.salesorderapp.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity
import com.joseph.salesorderapp.data.remote.model.CustomerPayload
import com.joseph.salesorderapp.data.remote.model.OrderItemPayload
import com.joseph.salesorderapp.data.remote.model.SaveCustomerInput
import com.joseph.salesorderapp.data.remote.model.SaveOrderInput
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.presentation.UiEventManager
import com.joseph.salesorderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val repository: AppRepository,
    private val uiEventManager: UiEventManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashBoardState())
    val uiState: StateFlow<DashBoardState> = _uiState.asStateFlow()

    init {
        loadUnsyncedCounts()
    }

    fun loadUnsyncedCounts() {
        viewModelScope.launch {
            repository.fetchUnsyncedOrdersCount().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(error = null) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                unsyncedOrdersCount = result.data,
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

            repository.fetchUnsyncedCustomersCount().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(error = null) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                unsyncedCustomersCount = result.data,
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
        }
    }

    fun onCardClicked(route: String) {
        viewModelScope.launch {
            uiEventManager.navigate(route = route)
        }
    }

    fun downloadMasters() {
        viewModelScope.launch {
            uiEventManager.showLoader("Loading..", true)

            repository.downloadCustomers().collect { customerResult ->
                when (customerResult) {

                    is Resource.Success -> {
                        if (customerResult.data?.status == 1) {
                            val customerList =
                                customerResult.data.customers?.filterNotNull() ?: emptyList()
                            repository.deleteAllCustomers()
                            repository.insertCustomers(customerList)
                        } else {
                            uiEventManager.showToast(customerResult.data?.message.toString())
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(error = customerResult.message)
                        }
                    }

                    else -> {}
                }
            }
            repository.downloadProducts().collect { productResult ->
                when (productResult) {

                    is Resource.Success -> {
                        if (productResult.data?.status == 1) {
                            val productList =
                                productResult.data.productDataItem?.filterNotNull() ?: emptyList()
                            repository.deleteAllProducts()
                            repository.insertProducts(productList)
                        } else {
                            uiEventManager.showToast(productResult.data?.message.toString())
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(error = productResult.message)
                        }
                    }

                    else -> {}
                }
            }

            repository.downloadUsers().collect { usersResult ->
                when (usersResult) {

                    is Resource.Success -> {
                        if (usersResult.data?.status == 1) {
                            val userList =
                                usersResult.data.userDataItem?.filterNotNull() ?: emptyList()
                            repository.deleteAllUsers()
                            repository.insertUsers(userList)
                            uiEventManager.showToast("Masters downloaded successfully")
                        } else {
                            uiEventManager.showToast(usersResult.data?.message.toString())
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(error = usersResult.message)
                        }
                    }

                    else -> {}
                }
            }

            uiEventManager.showLoader("", false)
        }
    }

    fun fetchUnsyncedOrdersToSync() {
        viewModelScope.launch {
            val ordersCount = uiState.value.unsyncedOrdersCount ?: 0
            val customersCount = uiState.value.unsyncedCustomersCount ?: 0

            if (ordersCount == 0) {
                uiEventManager.showSnackbar("No data to sync")
                return@launch
            }

            if (customersCount >0) {
                uiEventManager.showSnackbar("Please sync customer data before syncing orders.")
                return@launch
            }

            uiEventManager.showLoader("Fetching unsynced orders...", true)

            repository.fetchSyncPendingOrders().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(error = null) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                unsyncedOrders = result.data ?: emptyList(),
                                error = null,
                                success = true
                            )
                        }
                        syncOrdersOneByOne(uiState.value.unsyncedOrders)
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(error = result.message)
                        }
                        uiEventManager.showToast("Failed to fetch orders: ${result.message}")
                    }
                }
            }

            uiEventManager.showLoader("", false)
        }

    }

    private suspend fun syncOrdersOneByOne(orderList: List<OrderSummaryEntity>) {
        for ((index, order) in orderList.withIndex()) {
            uiEventManager.showLoader("Syncing order ${index + 1} of ${orderList.size}", true)

            val orderItems = repository.fetchReportItemList(order.id)
                .firstOrNull { it is Resource.Success }
                ?.data ?: emptyList()

            if (orderItems.isEmpty()) {
                _uiState.update { it.copy(error = "No items for order ${order.id}") }
                continue
            }

            val payload = SaveOrderInput(
                orderToken = order.id.toString(),
                userId = order.userID.toString(),
                customerMasterId = order.customerID,
                payMode = order.paymentMode,
                salesMasterId = order.id,
                details = orderItems.map {
                    OrderItemPayload(
                        productMasterId = it.productID,
                        qty = it.quantity,
                        tax = it.taxPercentage,
                        discount = it.discount,
                        price = it.pricePerUnit
                    )
                }
            )


            repository.saveOrder(payload).collect { orderResult ->
                when (orderResult) {

                    is Resource.Success -> {
                        if (orderResult.data?.status == 1) {
                            repository.updateOrderSyncStatus(order.id.toLong())
                            repository.updateOrderItemsSyncStatus(order.id.toLong())

                            _uiState.update {
                                it.copy(unsyncedOrders = it.unsyncedOrders.filterNot { o -> o.id == order.id })
                            }
                        } else {
                            uiEventManager.showToast(orderResult.data?.message.toString())
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(error = "Failed to sync order ${order.id}: ${orderResult.message}")
                        }
                    }

                    else -> Unit
                }
            }
        }

        uiEventManager.showLoader("", false)
        _uiState.update { it.copy(success = true, error = null) }
        loadUnsyncedCounts()
        uiEventManager.showToast("Successfully synced")
    }

    fun fetchUnsyncedCustomerToSync() {
        viewModelScope.launch {
            uiEventManager.showLoader("Fetching unsynced orders...", true)
            repository.fetchSyncPendingCustomer().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(error = null) }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                customerList = result.data ?: emptyList(),
                                error = null,
                                success = true
                            )
                        }
                        syncCustomerOneByOne(uiState.value.customerList)
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

    private suspend fun syncCustomerOneByOne(customerList: List<CustomerEntity>) {
        for ((index, customer) in customerList.withIndex()) {
            uiEventManager.showLoader("Syncing customer ${index + 1} of ${customerList.size}", true)

            val payload = SaveCustomerInput(
                customer = listOf(
                    CustomerPayload(
                        name = customer.name,
                        address = customer.address,
                        phone = customer.phoneNo,
                        masterId = 0,
                        trdNo = "hghg",
                    )
                )
            )

            repository.saveCustomer(payload).collect { customerResult ->
                when (customerResult) {

                    is Resource.Success -> {
                        if (customerResult.data?.status == 1) {
                            repository.updateCustomerSyncStatus(customer.id.toLong())

                            _uiState.update {
                                it.copy(unsyncedOrders = it.unsyncedOrders.filterNot { o -> o.id == customer.id })
                            }
                        } else {
                            uiEventManager.showToast(customerResult.data?.message.toString())
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(error = "Failed to sync order ${customer.id}: ${customerResult.message}")
                        }
                    }

                    else -> Unit
                }
            }
        }

        uiEventManager.showLoader("", false)
        _uiState.update { it.copy(success = true, error = null) }
        loadUnsyncedCounts()
        uiEventManager.showToast("Successfully synced")
    }

}


