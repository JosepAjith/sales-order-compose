package com.joseph.salesorderapp.data.remote

import com.joseph.salesorderapp.data.remote.model.CustomerResponse
import com.joseph.salesorderapp.data.remote.model.LoginRequest
import com.joseph.salesorderapp.data.remote.model.LoginResponse
import com.joseph.salesorderapp.data.remote.model.ProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login/ananthu")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("get-customers/ananthu")
    suspend fun downloadCustomers(): Response<CustomerResponse>

    @POST("get-item/ananthu")
    suspend fun downloadProducts(): Response<ProductResponse>

}
