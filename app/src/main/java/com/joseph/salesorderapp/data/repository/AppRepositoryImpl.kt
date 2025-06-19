package com.joseph.salesorderapp.data.repository


import android.content.Context
import com.joseph.salesorderapp.data.local.LocalDataSource
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity
import com.joseph.salesorderapp.data.remote.RemoteDataSource
import com.joseph.salesorderapp.data.remote.model.CustomerResponse
import com.joseph.salesorderapp.data.remote.model.CustomersItem
import com.joseph.salesorderapp.data.remote.model.LoginRequest
import com.joseph.salesorderapp.data.remote.model.LoginResponse
import com.joseph.salesorderapp.domain.model.OrderItem
import com.joseph.salesorderapp.data.remote.model.ProductDataItem
import com.joseph.salesorderapp.data.remote.model.ProductResponse
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.util.DateUtils
import com.joseph.salesorderapp.util.Resource
import com.joseph.salesorderapp.util.isNetworkAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class AppRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : AppRepository {

    //Remote API
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

    override suspend fun downloadCustomers(): Flow<Resource<CustomerResponse>> = flow {
        emit(Resource.Loading())
        try {
            if (context.isNetworkAvailable()) {
                val response = remoteDataSource.downloadCustomers()
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

    override suspend fun downloadProducts(): Flow<Resource<ProductResponse>> = flow {
        emit(Resource.Loading())
        try {
            if (context.isNetworkAvailable()) {
                val response = remoteDataSource.downloadProducts()
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


    //Local Database
    override suspend fun insertCustomers(customers: List<CustomersItem>) {
        val customerEntities = customers.map {
            CustomerEntity(
                serverId = it.id ?: 0,
                name = it.name.orEmpty(),
                phoneNo = it.phone.toString(),
                email = "",
                address = it.address.toString(),
                city = "",
                pinCode = "",
                trnNo = "",
                gstNo = "",
                isSynced = true,
                createdAt = DateUtils.getCurrentTimestamp(),
                updatedAt = DateUtils.getCurrentTimestamp()
            )
        }
        localDataSource.insertCustomers(customerEntities)
    }

    override suspend fun fetchCustomers(query: String): Flow<Resource<List<CustomerEntity>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val localCustomers =
                    localDataSource.fetchCustomers(query).first() // get first item from Flow
                emit(Resource.Success(localCustomers))
            } catch (e: Exception) {
                emit(Resource.Error("Error: ${e.message}"))
            }
        }
    }


    override suspend fun deleteAllCustomers() {
        localDataSource.deleteAllCustomers()
    }


    override suspend fun insertProducts(products: List<ProductDataItem>) {
        val productsEntities = products.map {
            ProductEntity(
                serverId = it.id ?: 0,
                name = it.name.orEmpty(),
                barcode = "",
                category = "",
                productCode = it.itemCode.toString(),
                brand = "",
                description = "",
                purchasePrice = 0.0,
                sellingPrice = it.amount?.toDoubleOrNull() ?: 0.0,
                stockQty = it.qty ?: 0,
                unit = it.units.toString(),
                taxPercentage = it.taxPercentage?.toDoubleOrNull() ?: 0.0,
                hsnCode = "",
                isSynced = true,
                isActive = true,
                createdAt = DateUtils.getCurrentTimestamp(),
                updatedAt = DateUtils.getCurrentTimestamp()
            )
        }
        localDataSource.insertProducts(productsEntities)
    }

    override suspend fun fetchProducts(query: String): Flow<Resource<List<ProductEntity>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val localProducts =
                    localDataSource.fetchProducts(query).first() // get first item from Flow
                emit(Resource.Success(localProducts))
            } catch (e: Exception) {
                emit(Resource.Error("Error: ${e.message}"))
            }
        }
    }


    override suspend fun deleteAllProducts() {
        localDataSource.deleteAllProducts()
    }


    override suspend fun insertOrderSummary(
        customer: String,
        totItems: Int,
        total: Double,
        paymentMode: String
    ): Long {
        val orderSummary = OrderSummaryEntity(
            customerName = customer,
            totalItems = totItems,
            totalAmount = total,
            isSynced = false,
            paymentMode = paymentMode,
            orderDate = DateUtils.getCurrentDate(),
            createdAt = DateUtils.getCurrentTimestamp(),
            updatedAt = DateUtils.getCurrentTimestamp()
        )

        return localDataSource.insertOrderSummary(orderSummary)
    }

    override suspend fun insertOrderDetails(itemList: List<OrderItem>, orderId: Long) {
        val orderItems = itemList.map {
            OrderDetailsEntity(
                orderId = orderId,
                productName = it.product.name,
                quantity = it.product.stockQty,
                pricePerUnit = it.product.sellingPrice,
                totalPrice = it.product.sellingPrice * it.product.stockQty,
                isSynced = false,
                createdAt = DateUtils.getCurrentTimestamp(),
                updatedAt = DateUtils.getCurrentTimestamp()
            )
        }

        localDataSource.insertOrderDetails(orderItems)
    }

    override suspend fun fetchOrderSummary(
        fromDate: String,
        toDate: String
    ): Flow<Resource<List<OrderSummaryEntity>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val loadSummary =
                    localDataSource.fetchOrderSummary(fromDate, toDate).first()
                emit(Resource.Success(loadSummary))
            } catch (e: Exception) {
                emit(Resource.Error("Error: ${e.message}"))
            }
        }

    }

    override suspend fun fetchOrderSummaryById(
        orderId: Int
    ): Flow<Resource<OrderSummaryEntity>> {
        return flow {
            emit(Resource.Loading())
            try {
                val orderSummary = localDataSource.fetchOrderSummaryById(orderId).first()
                if (orderSummary != null) {
                    emit(Resource.Success(orderSummary))
                } else {
                    emit(Resource.Error("Order not found"))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error: ${e.message}"))
            }
        }
    }


    override suspend fun fetchReportItemList(orderId: Int): Flow<Resource<List<OrderDetailsEntity>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val orderItems =
                    localDataSource.fetchReportItemList(orderId).first()
                emit(Resource.Success(orderItems))
            } catch (e: Exception) {
                emit(Resource.Error("Error: ${e.message}"))
            }
        }
    }


}
