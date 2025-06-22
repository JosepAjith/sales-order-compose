package com.joseph.salesorderapp.data.local

import com.joseph.salesorderapp.data.local.dao.CustomerDao
import com.joseph.salesorderapp.data.local.dao.ProductDao
import com.joseph.salesorderapp.data.local.dao.UserDao
import com.joseph.salesorderapp.data.local.dao.order.OrderDetailsDao
import com.joseph.salesorderapp.data.local.dao.order.OrderSummaryDao
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.local.entity.UserEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity
import com.joseph.salesorderapp.domain.model.ItemWiseReport
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val customerDao: CustomerDao,
    private val productDao: ProductDao,
    private val orderSummaryDao: OrderSummaryDao,
    private val orderDetailsDao: OrderDetailsDao,
    private val userDao: UserDao,
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

    suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }

    suspend fun insertUsers(users: List<UserEntity>) {
        userDao.insertUsers(users)
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

    fun fetchOrderSummary(fromDate: String, toDate: String): Flow<List<OrderSummaryEntity>> {
        return orderSummaryDao.fetchOrderSummary(fromDate, toDate)
    }

    fun fetchOrderSummaryById(orderID: Int): Flow<OrderSummaryEntity?> {
        return orderSummaryDao.fetchOrderSummaryById(orderID)

    }

    fun fetchReportItemList(orderID: Int): Flow<List<OrderDetailsEntity>> {
        return orderDetailsDao.fetchReportItemList(orderID)
    }

    suspend fun fetchUnsyncedOrdersCountOnce(): Int {
        return orderSummaryDao.getUnsyncedOrdersCount()
    }

    suspend fun fetchUnsyncedCustomersCountOnce(): Int {
        return customerDao.getUnsyncedCustomersCount()
    }

    fun fetchSyncPendingOrders(): Flow<List<OrderSummaryEntity>> {
        return orderSummaryDao.getUnsyncedOrderEntities()
    }

    suspend fun updateOrderSyncStatus(orderId: Long) {
        orderSummaryDao.updateOrderSynced(orderId)
    }

    suspend fun updateOrderItemsSyncStatus(orderId: Long) {
        orderDetailsDao.updateOrderItemsSyncStatus(orderId)
    }

    suspend fun updateCustomerSyncStatus(customerID: Long) {
        customerDao.updateCustomerSynced(customerID)
    }

    fun fetchSyncPendingCustomer(): Flow<List<CustomerEntity>> {
        return customerDao.getUnsyncedCustomerEntities()
    }

    suspend fun insertCustomer(customer: CustomerEntity) {
        return customerDao.insertCustomer(customer)
    }

    fun fetchItemWiseReport(
        fromDate: String,
        toDate: String,
    ): Flow<List<ItemWiseReport>> {
        return orderDetailsDao.fetchItemWiseReport(fromDate, toDate)
    }

}
