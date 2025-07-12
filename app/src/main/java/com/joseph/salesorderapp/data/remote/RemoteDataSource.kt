package com.joseph.salesorderapp.data.remote

import com.joseph.salesorderapp.data.local.preferences.AppPreferences
import com.joseph.salesorderapp.data.remote.model.CustomerResponse
import com.joseph.salesorderapp.data.remote.model.LoginRequest
import com.joseph.salesorderapp.data.remote.model.LoginResponse
import com.joseph.salesorderapp.data.remote.model.OrderResponse
import com.joseph.salesorderapp.data.remote.model.ProductResponse
import com.joseph.salesorderapp.data.remote.model.SaveCustomerInput
import com.joseph.salesorderapp.data.remote.model.SaveCustomerResponse
import com.joseph.salesorderapp.data.remote.model.SaveOrderInput
import com.joseph.salesorderapp.data.remote.model.SettingsResponse
import com.joseph.salesorderapp.data.remote.model.UserResponse
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val appPreferences: AppPreferences,
) {
    private suspend fun getSlug(): String {
        return appPreferences.getString(AppPreferences.KEY_DOMAIN_ADDRESS).firstOrNull() ?: ""
    }

    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        val slug = getSlug()
        return apiService.login(slug, request)
    }

    suspend fun downloadSettings(): Response<SettingsResponse> {
        val slug = getSlug()
        return apiService.downloadSettings(slug)
    }

    suspend fun downloadCustomers(): Response<CustomerResponse> {
        val slug = getSlug()
        return apiService.downloadCustomers(slug)
    }

    suspend fun downloadProducts(): Response<ProductResponse> {
        val slug = getSlug()
        return apiService.downloadProducts(slug)
    }

    suspend fun downloadUsers(): Response<UserResponse> {
        val slug = getSlug()
        return apiService.downloadUsers(slug)
    }

    suspend fun saveOrder(request: SaveOrderInput): Response<OrderResponse> {
        val slug = getSlug()
        return apiService.saveOrder(slug, request)
    }

    suspend fun saveCustomer(request: SaveCustomerInput): Response<SaveCustomerResponse> {
        val slug = getSlug()
        return apiService.saveCustomer(slug, request)
    }

}
