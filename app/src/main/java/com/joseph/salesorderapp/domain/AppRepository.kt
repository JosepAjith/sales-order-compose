package com.joseph.salesorderapp.domain

import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity
import com.joseph.salesorderapp.data.remote.model.CustomerResponse
import com.joseph.salesorderapp.data.remote.model.CustomersItem
import com.joseph.salesorderapp.data.remote.model.LoginResponse
import com.joseph.salesorderapp.data.remote.model.OrderResponse
import com.joseph.salesorderapp.domain.model.OrderItem
import com.joseph.salesorderapp.data.remote.model.ProductDataItem
import com.joseph.salesorderapp.data.remote.model.ProductResponse
import com.joseph.salesorderapp.data.remote.model.SaveCustomerInput
import com.joseph.salesorderapp.data.remote.model.SaveCustomerResponse
import com.joseph.salesorderapp.data.remote.model.SaveOrderInput
import com.joseph.salesorderapp.data.remote.model.SettingsResponse
import com.joseph.salesorderapp.data.remote.model.UserDataItem
import com.joseph.salesorderapp.data.remote.model.UserResponse
import com.joseph.salesorderapp.domain.model.ItemWiseReport
import com.joseph.salesorderapp.util.Resource
import kotlinx.coroutines.flow.Flow


interface AppRepository {
    //Remote API
    suspend fun login(username: String, password: String): Flow<Resource<LoginResponse>>
    suspend fun downloadSettings(): Flow<Resource<SettingsResponse>>
    suspend fun downloadCustomers(): Flow<Resource<CustomerResponse>>
    suspend fun downloadProducts(): Flow<Resource<ProductResponse>>
    suspend fun downloadUsers(): Flow<Resource<UserResponse>>
    suspend fun saveOrder(request: SaveOrderInput): Flow<Resource<OrderResponse>>
    suspend fun saveCustomer(request: SaveCustomerInput): Flow<Resource<SaveCustomerResponse>>

    // Local Database
    suspend fun insertCustomers(customers: List<CustomersItem>)
    suspend fun fetchCustomers(query: String): Flow<Resource<List<CustomerEntity>>>
    suspend fun deleteAllCustomers()
    suspend fun deleteAllUsers()
    suspend fun insertUsers(users: List<UserDataItem>)
    suspend fun insertProducts(products: List<ProductDataItem>)
    suspend fun fetchProducts(query: String): Flow<Resource<List<ProductEntity>>>
    suspend fun deleteAllProducts()

    suspend fun insertOrderSummary(
        orderID: String,
        selectedCustomer: CustomerEntity?,
        totItems: Int,
        total: Double,
        discountAmount:Double,
        paymentMode: String,
        userID: String,
    ): Long

    suspend fun updateOrderSummary(
        id:Int,
        orderID: String,
        selectedCustomer: CustomerEntity?,
        totItems: Int,
        total: Double,
        discountAmount:Double,
        paymentMode: String,
        userID: String,
    )

    suspend fun insertCustomer(name: String, phone: String, address: String)

    suspend fun insertOrderDetails(
        itemList: List<OrderItem>,
        orderId: Long,
        selectedCustomer: CustomerEntity?,
        userID: String,
    )

    suspend fun fetchOrderSummary(
        fromDate: String,
        toDate: String
    ): Flow<Resource<List<OrderSummaryEntity>>>

    suspend fun fetchOrderSummaryById(orderId: Int): Flow<Resource<OrderSummaryEntity>>

    suspend fun fetchReportItemList(orderId: Int): Flow<Resource<List<OrderDetailsEntity>>>

    fun fetchUnsyncedOrdersCount(): Flow<Resource<Int>>

    fun fetchUnsyncedCustomersCount(): Flow<Resource<Int>>

    suspend fun fetchSyncPendingOrders(): Flow<Resource<List<OrderSummaryEntity>>>
    fun fetchSyncPendingCustomer(): Flow<Resource<List<CustomerEntity>>>

    suspend fun deleteOrderDetails(orderId: Long)
    suspend fun updateOrderSyncStatus(orderId: Long)
    suspend fun updateDeleteStatus(orderId: Long)
    suspend fun updateItemDeleteStatus(orderID: Long)
    suspend fun updateOrderItemsSyncStatus(orderId: Long)
    suspend fun updateCustomerSyncStatus(customerID: Long, serverID: Int?)
    suspend fun updateOrderCustomerID(serverID: Int?,customerID: Int)
    suspend fun updateOrderDetailCustomerID(serverID: Int?,customerID: Int)

    suspend fun fetchItemWiseReport(
        fromDate: String,
        toDate: String,
        customerID: Int?,
    ): Flow<Resource<List<ItemWiseReport>>>

    suspend fun fetchLastOrderNo(): Flow<Resource<Int>>

}