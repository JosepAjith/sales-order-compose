package com.joseph.salesorderapp.data.repository


import android.content.Context
import com.joseph.salesorderapp.data.local.LocalDataSource
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.remote.RemoteDataSource
import com.joseph.salesorderapp.data.remote.model.CustomerResponse
import com.joseph.salesorderapp.data.remote.model.LoginRequest
import com.joseph.salesorderapp.data.remote.model.LoginResponse
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.util.Resource
import com.joseph.salesorderapp.util.isNetworkAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class AppRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : AppRepository {
    override suspend fun login(username: String, password: String): Flow<Resource<LoginResponse>> =
        flow {
            emit(Resource.Loading())
            try {
                if (context.isNetworkAvailable()) {
                    val response = remoteDataSource.login(LoginRequest(username, password))
                    if (response.isSuccessful) {
                        emit(Resource.Success(response.body()!!))
                    } else {
                        emit(Resource.Error("Login failed: ${response.code()}"))
                    }
                } else {
                    emit(Resource.Error("Check internet connection"))
                }

            } catch (e: Exception) {
                emit(Resource.Error("Exception: ${e.localizedMessage}"))
            }
        }

    override suspend fun fetchCustomers(): Flow<Resource<CustomerResponse>> =
        flow {
            emit(Resource.Loading())
            try {
                if (context.isNetworkAvailable()) {
                    val response = remoteDataSource.fetchCustomers()
                    if (response.isSuccessful) {
                        emit(Resource.Success(response.body()!!))
                    } else {
                        emit(Resource.Error("Login failed: ${response.code()}"))
                    }
                } else {
                    emit(Resource.Error("Check internet connection"))
                }

            } catch (e: Exception) {
                emit(Resource.Error("Exception: ${e.localizedMessage}"))
            }
        }

    override suspend fun getOfflineProducts(): Flow<List<ProductEntity>> {
        return localDataSource.getAllProducts()
    }
}
