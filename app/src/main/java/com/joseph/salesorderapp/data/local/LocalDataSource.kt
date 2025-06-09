package com.joseph.salesorderapp.data.local

import com.joseph.salesorderapp.data.local.dao.ProductDao
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val productDao: ProductDao
) {
    suspend fun insertProducts(products: ProductEntity) {
        productDao.insertProducts(products)
    }

    suspend fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

}
