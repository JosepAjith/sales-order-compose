package com.joseph.salesorderapp.domain

import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity
import com.joseph.salesorderapp.data.remote.model.CustomerResponse
import com.joseph.salesorderapp.data.remote.model.CustomersItem
import com.joseph.salesorderapp.data.remote.model.LoginResponse
import com.joseph.salesorderapp.domain.model.OrderItem
import com.joseph.salesorderapp.data.remote.model.ProductDataItem
import com.joseph.salesorderapp.data.remote.model.ProductResponse
import com.joseph.salesorderapp.util.Resource
import kotlinx.coroutines.flow.Flow


interface AppRepository {
    //Remote API
    suspend fun login(username: String, password: String): Flow<Resource<LoginResponse>>
    suspend fun downloadCustomers(): Flow<Resource<CustomerResponse>>
    suspend fun downloadProducts(): Flow<Resource<ProductResponse>>

    // Local Database
    suspend fun insertCustomers(customers: List<CustomersItem>)
    suspend fun fetchCustomers(query: String): Flow<Resource<List<CustomerEntity>>>
    suspend fun deleteAllCustomers()

    suspend fun insertProducts(products: List<ProductDataItem>)
    suspend fun fetchProducts(query: String): Flow<Resource<List<ProductEntity>>>
    suspend fun deleteAllProducts()

    suspend fun insertOrderSummary(customer: String, totItems: Int, total: Double): Long
    suspend fun insertOrderDetails(itemList: List<OrderItem>, orderId: Long)

    suspend fun fetchOrderSummary(fromDate: String,toDate: String): Flow<Resource<List<OrderSummaryEntity>>>


}