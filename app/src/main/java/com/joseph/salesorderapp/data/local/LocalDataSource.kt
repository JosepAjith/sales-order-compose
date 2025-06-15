package com.joseph.salesorderapp.data.local

import com.joseph.salesorderapp.data.local.dao.CustomerDao
import com.joseph.salesorderapp.data.local.dao.ProductDao
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val customerDao: CustomerDao,
    private val productDao: ProductDao
) {

    suspend fun insertCustomers(customers: List<CustomerEntity>) {
        customerDao.insertCustomers(customers)
    }

    fun fetchCustomers(): Flow<List<CustomerEntity>> {
        return customerDao.fetchCustomers()
    }

    suspend fun deleteAllCustomers() {
        customerDao.deleteAllCustomers()
    }

    suspend fun insertProducts(products: List<ProductEntity>) {
        productDao.insertProducts(products)
    }

    fun fetchProducts(): Flow<List<ProductEntity>> {
        return productDao.fetchProducts()
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }
}
