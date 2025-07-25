package com.joseph.salesorderapp.data.repository


import android.content.Context
import com.joseph.salesorderapp.data.local.LocalDataSource
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.local.entity.UserEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity
import com.joseph.salesorderapp.data.remote.RemoteDataSource
import com.joseph.salesorderapp.data.remote.model.CustomerResponse
import com.joseph.salesorderapp.data.remote.model.CustomersItem
import com.joseph.salesorderapp.data.remote.model.LoginRequest
import com.joseph.salesorderapp.data.remote.model.LoginResponse
import com.joseph.salesorderapp.data.remote.model.OrderResponse
import com.joseph.salesorderapp.domain.model.OrderItem
import com.joseph.salesorderapp.data.remote.model.ProductDataItem
import com.joseph.salesorderapp.data.remote.model.ProductResponse
import com.joseph.salesorderapp.data.remote.model.SaveCustomerInput
import com.joseph.salesorderapp.data.remote.model.SaveCustomerResponse
import com.joseph.salesorderapp.data.remote.model.SaveOrderInput
import com.joseph.salesorderapp.data.remote.model.SettingsResponse
import com.joseph.salesorderapp.data.remote.model.UserDataItem
import com.joseph.salesorderapp.data.remote.model.UserResponse
import com.joseph.salesorderapp.domain.AppRepository
import com.joseph.salesorderapp.domain.model.ItemWiseReport
import com.joseph.salesorderapp.util.DateUtils
import com.joseph.salesorderapp.util.Resource
import com.joseph.salesorderapp.util.isNetworkAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class AppRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : AppRepository {

    //Remote API
    override suspend fun login(username: String, password: String): Flow<Resource<LoginResponse>> =
        flow {
            emit(Resource.Loading())
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
        }.catch { e ->
            emit(Resource.Error("Exception: ${e.localizedMessage}"))
        }

    override suspend fun downloadUsers(): Flow<Resource<UserResponse>> = flow {
        emit(Resource.Loading())
        try {
            if (context.isNetworkAvailable()) {
                val response = remoteDataSource.downloadUsers()
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

    override suspend fun downloadSettings(): Flow<Resource<SettingsResponse>> = flow {
        emit(Resource.Loading())
        try {
            if (context.isNetworkAvailable()) {
                val response = remoteDataSource.downloadSettings()
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

    override suspend fun saveOrder(request: SaveOrderInput): Flow<Resource<OrderResponse>> =
        flow {
            emit(Resource.Loading())
            if (context.isNetworkAvailable()) {
                val response = remoteDataSource.saveOrder(request)
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    emit(Resource.Error("Sync Failed: ${response.code()}"))
                }
            } else {
                emit(Resource.Error("Check internet connection"))
            }

        }.catch { e ->
            emit(Resource.Error("Exception: ${e.localizedMessage}"))
        }


    override suspend fun saveCustomer(request: SaveCustomerInput): Flow<Resource<SaveCustomerResponse>> =
        flow {
            emit(Resource.Loading())
            if (context.isNetworkAvailable()) {
                val response = remoteDataSource.saveCustomer(request)
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    emit(Resource.Error("Customer save Failed: ${response.code()}"))
                }
            } else {
                emit(Resource.Error("Check internet connection"))
            }
        }.catch { e ->
            emit(Resource.Error("Exception: ${e.localizedMessage}"))
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
            val localCustomers =
                localDataSource.fetchCustomers(query).first()
            emit(Resource.Success(localCustomers))

        }.catch { e ->
            emit(Resource.Error("Error: ${e.message}"))
        }
    }


    override suspend fun deleteAllCustomers() {
        localDataSource.deleteAllCustomers()
    }

    override suspend fun deleteAllUsers() {
        localDataSource.deleteAllUsers()
    }


    override suspend fun insertUsers(users: List<UserDataItem>) {
        val usersEntities = users.map {
            UserEntity(
                serverId = it.masterId ?: 0,
                name = it.name.toString(),
                email = it.email.toString(),
                phoneNo = "",
                isSynced = true,
                createdAt = DateUtils.getCurrentTimestamp(),
                updatedAt = DateUtils.getCurrentTimestamp()
            )
        }
        localDataSource.insertUsers(usersEntities)
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
            val localProducts =
                localDataSource.fetchProducts(query).first()
            emit(Resource.Success(localProducts))
        }.catch { e ->
            emit(Resource.Error("Error: ${e.message}"))
        }
    }


    override suspend fun deleteAllProducts() {
        localDataSource.deleteAllProducts()
    }


    override suspend fun insertOrderSummary(
        orderID: String,
        selectedCustomer: CustomerEntity?,
        totItems: Int,
        total: Double,
        discountAmount: Double,
        paymentMode: String,
        userID: String,
    ): Long {
        val customerID = if (selectedCustomer?.serverId != 0) {
            selectedCustomer?.serverId ?: 0
        } else {
            selectedCustomer.id
        }

        val orderSummary = OrderSummaryEntity(
            customerName = selectedCustomer?.name.toString(),
            customerID = customerID,
            userID = userID.toIntOrNull() ?: 0,
            orderID = orderID,
            totalItems = totItems,
            totalAmount = total,
            discountAmount = discountAmount,
            isSynced = false,
            paymentMode = paymentMode,
            orderDate = DateUtils.getCurrentDate(),
            orderTime = DateUtils.getCurrentTimeOnly(),
            createdAt = DateUtils.getCurrentTimestamp(),
            updatedAt = DateUtils.getCurrentTimestamp()
        )

        return localDataSource.insertOrderSummary(orderSummary)
    }


    override suspend fun updateOrderSummary(
        id: Int,
        orderID: String,
        selectedCustomer: CustomerEntity?,
        totItems: Int,
        total: Double,
        discountAmount: Double,
        paymentMode: String,
        userID: String
    ) {
        val customerID = if (selectedCustomer?.serverId != 0) {
            selectedCustomer?.serverId ?: 0
        } else {
            selectedCustomer.id ?: 0
        }

        val orderSummary = OrderSummaryEntity(
            id = id,
            customerName = selectedCustomer?.name.toString(),
            customerID = customerID,
            userID = userID.toIntOrNull() ?: 0,
            orderID = "SO$orderID",
            totalItems = totItems,
            totalAmount = total,
            discountAmount = discountAmount,
            isSynced = false,
            paymentMode = paymentMode,
            orderDate = DateUtils.getCurrentDate(),
            orderTime = DateUtils.getCurrentTimeOnly(),
            updatedAt = DateUtils.getCurrentTimestamp()
        )

        localDataSource.updateOrderSummary(orderSummary)
    }



    override suspend fun insertCustomer(
        name: String, phone: String, address: String
    ) {
        val customer = CustomerEntity(
            serverId = 0,
            name = name,
            phoneNo = phone,
            address = address,
            isSynced = false,
            createdAt = DateUtils.getCurrentTimestamp(),
            updatedAt = DateUtils.getCurrentTimestamp()
        )

        return localDataSource.insertCustomer(customer)
    }

    override suspend fun insertOrderDetails(
        itemList: List<OrderItem>,
        orderId: Long,
        selectedCustomer: CustomerEntity?,
        userID: String
    ) {
        val customerID = if (selectedCustomer?.serverId != 0) {
            selectedCustomer?.serverId ?: 0
        } else {
            selectedCustomer.id
        }

        val orderItems = itemList.map {
            OrderDetailsEntity(
                orderId = orderId,
                productName = it.product.name,
                productID = it.product.serverId,
                quantity = it.quantity,
                taxPercentage = it.product.taxPercentage,
                pricePerUnit = it.product.sellingPrice,
                productCode = it.product.productCode,
                discount = 0.0,
                totalPrice = it.product.sellingPrice * it.product.stockQty,
                isSynced = false,
                customerID = customerID,
                customerName = selectedCustomer?.name ?: "",
                userID = userID.toIntOrNull() ?: 0,
                orderDate = DateUtils.getCurrentDate(),
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
            val loadSummary =
                localDataSource.fetchOrderSummary(fromDate, toDate).first()
            emit(Resource.Success(loadSummary))
        }.catch { e ->
            emit(Resource.Error("Error: ${e.message}"))
        }
    }

    override suspend fun fetchOrderSummaryById(
        orderId: Int
    ): Flow<Resource<OrderSummaryEntity>> {
        return flow {
            emit(Resource.Loading())
            val orderSummary = localDataSource.fetchOrderSummaryById(orderId).first()
            if (orderSummary != null) {
                emit(Resource.Success(orderSummary))
            } else {
                emit(Resource.Error("Order not found"))
            }
        }.catch { e ->
            emit(Resource.Error("Error: ${e.message}"))
        }
    }


    override suspend fun fetchReportItemList(orderId: Int): Flow<Resource<List<OrderDetailsEntity>>> {
        return flow {
            emit(Resource.Loading())
            val orderItems =
                localDataSource.fetchReportItemList(orderId).first()
            emit(Resource.Success(orderItems))
        }.catch { e ->
            emit(Resource.Error("Error: ${e.message}"))
        }
    }

    override fun fetchUnsyncedOrdersCount(): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            val count = localDataSource.fetchUnsyncedOrdersCountOnce()
            emit(Resource.Success(count))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override fun fetchUnsyncedCustomersCount(): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            val count = localDataSource.fetchUnsyncedCustomersCountOnce()
            emit(Resource.Success(count))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun fetchSyncPendingOrders(): Flow<Resource<List<OrderSummaryEntity>>> {
        return flow {
            emit(Resource.Loading())
            val loadSummary =
                localDataSource.fetchSyncPendingOrders().first()
            emit(Resource.Success(loadSummary))
        }.catch { e ->
            emit(Resource.Error("Error: ${e.message}"))
        }
    }

    override suspend fun updateOrderSyncStatus(orderId: Long) {
        localDataSource.updateOrderSyncStatus(orderId)
    }

    override suspend fun updateDeleteStatus(orderId: Long) {
        localDataSource.updateDeleteStatus(orderId)
    }

    override suspend fun updateItemDeleteStatus(orderId: Long) {
        localDataSource.updateItemDeleteStatus(orderId)
    }

    override suspend fun deleteOrderDetails(orderId: Long) {
        localDataSource.deleteOrderDetails(orderId)
    }

    override suspend fun updateOrderItemsSyncStatus(orderId: Long) {
        localDataSource.updateOrderItemsSyncStatus(orderId)
    }

    override fun fetchSyncPendingCustomer(): Flow<Resource<List<CustomerEntity>>> {
        return flow {
            emit(Resource.Loading())
            val customers =
                localDataSource.fetchSyncPendingCustomer().first()
            emit(Resource.Success(customers))
        }.catch { e ->
            emit(Resource.Error("Error: ${e.message}"))
        }
    }

    override suspend fun updateCustomerSyncStatus(customerID: Long, serverID: Int?) {
        localDataSource.updateCustomerSyncStatus(customerID, serverID)
    }

    override suspend fun updateOrderCustomerID(serverID: Int?, customerID: Int) {
        localDataSource.updateOrderCustomerID(serverID, customerID)
    }

    override suspend fun updateOrderDetailCustomerID(serverID: Int?, customerID: Int) {
        localDataSource.updateOrderDetailCustomerID(serverID, customerID)
    }


    override suspend fun fetchItemWiseReport(
        fromDate: String,
        toDate: String,
        customerID: Int?,
    ): Flow<Resource<List<ItemWiseReport>>> {
        return flow {
            emit(Resource.Loading())
            val orderItems =
                localDataSource.fetchItemWiseReport(fromDate, toDate, customerID).first()
            emit(Resource.Success(orderItems))
        }.catch { e ->
            emit(Resource.Error("Error: ${e.message}"))
        }
    }


    override suspend fun fetchLastOrderNo(): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            val lastNo: Int = localDataSource.fetchLastOrderNo() ?: 0
            emit(Resource.Success(lastNo))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }
}
