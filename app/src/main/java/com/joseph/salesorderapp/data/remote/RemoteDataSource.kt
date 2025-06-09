package com.joseph.salesorderapp.data.remote

import com.joseph.salesorderapp.data.remote.model.LoginRequest
import com.joseph.salesorderapp.data.remote.model.LoginResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return apiService.login(request)
    }
}
