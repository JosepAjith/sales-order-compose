package com.joseph.salesorderapp.data.remote

import com.joseph.salesorderapp.data.remote.model.LoginRequest
import com.joseph.salesorderapp.data.remote.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login/ananthu")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
