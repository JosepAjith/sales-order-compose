package com.joseph.salesorderapp.domain

import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.remote.model.LoginResponse
import com.joseph.salesorderapp.util.Resource
import kotlinx.coroutines.flow.Flow


interface AppRepository {
    suspend fun login(username: String, password: String): Flow<Resource<LoginResponse>>
    suspend fun getOfflineProducts(): Flow<List<ProductEntity>>
}