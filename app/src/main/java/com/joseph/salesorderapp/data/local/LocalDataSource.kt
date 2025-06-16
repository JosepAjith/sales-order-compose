package com.joseph.salesorderapp.data.local

import com.joseph.salesorderapp.data.local.dao.CustomerDao
import com.joseph.salesorderapp.data.local.dao.ProductDao
import com.joseph.salesorderapp.data.local.dao.order.OrderDetailsDao
import com.joseph.salesorderapp.data.local.dao.order.OrderSummaryDao
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val customerDao: CustomerDao,
    private val productDao: ProductDao,
    private val orderSummaryDao: OrderSummaryDao,
    private val orderDetailsDao: OrderDetailsDao
) {

    suspend fun insertCustomers(customers: List<CustomerEntity>) {
        customerDao.insertCustomers(customers)
    }

    fun fetchCustomers(query: String): Flow<List<CustomerEntity>> {
        return customerDao.fetchCustomers(query)
    }

    suspend fun deleteAllCustomers() {
        customerDao.deleteAllCustomers()
    }

    suspend fun insertProducts(products: List<ProductEntity>) {
        productDao.insertProducts(products)
    }

    fun fetchProducts(query: String): Flow<List<ProductEntity>> {
        return productDao.fetchProducts(query)
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }

    suspend fun insertOrderSummary(orderSummary: OrderSummaryEntity): Long {
        return orderSummaryDao.insertOrder(orderSummary)
    }

    suspend fun insertOrderDetails(orderDetails: List<OrderDetailsEntity>) {
        orderDetailsDao.insertOrderItems(orderDetails)
    }
}
