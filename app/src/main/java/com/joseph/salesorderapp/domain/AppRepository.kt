package com.joseph.salesorderapp.domain

import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.remote.model.CustomerResponse
import com.joseph.salesorderapp.data.remote.model.CustomersItem
import com.joseph.salesorderapp.data.remote.model.LoginResponse
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
    suspend fun fetchCustomers(): Flow<Resource<List<CustomerEntity>>>
    suspend fun deleteAllCustomers()

    suspend fun insertProducts(products: List<ProductDataItem>)
    suspend fun fetchProducts(): Flow<Resource<List<ProductEntity>>>
    suspend fun deleteAllProducts()

}