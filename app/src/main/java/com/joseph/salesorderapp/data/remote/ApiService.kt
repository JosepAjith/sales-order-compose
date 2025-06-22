package com.joseph.salesorderapp.data.remote

import com.joseph.salesorderapp.data.remote.model.CustomerResponse
import com.joseph.salesorderapp.data.remote.model.LoginRequest
import com.joseph.salesorderapp.data.remote.model.LoginResponse
import com.joseph.salesorderapp.data.remote.model.OrderResponse
import com.joseph.salesorderapp.data.remote.model.ProductResponse
import com.joseph.salesorderapp.data.remote.model.SaveCustomerInput
import com.joseph.salesorderapp.data.remote.model.SaveCustomerResponse
import com.joseph.salesorderapp.data.remote.model.SaveOrderInput
import com.joseph.salesorderapp.data.remote.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("login/{slug}")
    suspend fun login(
        @Path("slug") slug: String,
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("get-customers/{slug}")
    suspend fun downloadCustomers(@Path("slug") slug: String): Response<CustomerResponse>

    @POST("get-item/{slug}")
    suspend fun downloadProducts(@Path("slug") slug: String): Response<ProductResponse>

    @POST("get-user/{slug}")
    suspend fun downloadUsers(@Path("slug") slug: String): Response<UserResponse>

    @POST("sale-orders/{slug}")
    suspend fun saveOrder(
        @Path("slug") slug: String,
        @Body request: SaveOrderInput
    ): Response<OrderResponse>

    @POST("customer-sync/{slug}")
    suspend fun saveCustomer(
        @Path("slug") slug: String,
        @Body request: SaveCustomerInput
    ): Response<SaveCustomerResponse>

}
