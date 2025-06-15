package com.joseph.salesorderapp.data.remote

import com.joseph.salesorderapp.data.remote.model.CustomerResponse
import com.joseph.salesorderapp.data.remote.model.LoginRequest
import com.joseph.salesorderapp.data.remote.model.LoginResponse
import com.joseph.salesorderapp.data.remote.model.ProductResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return apiService.login(request)
    }

    suspend fun downloadCustomers(): Response<CustomerResponse> {
        return apiService.downloadCustomers()
    }

    suspend fun downloadProducts(): Response<ProductResponse>{
        return apiService.downloadProducts()
    }
}
