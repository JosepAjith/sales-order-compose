package com.joseph.salesorderapp.data.local.dao.order

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderSummaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(summary: OrderSummaryEntity): Long

    @Update
    suspend fun updateOrderSummary(summary: OrderSummaryEntity)

    @Query("SELECT * FROM order_summary")
    fun fetchSummary(): Flow<List<OrderSummaryEntity>>

    @Query("SELECT * FROM order_summary WHERE orderDate BETWEEN :fromDate AND :toDate AND isDeleted = 0")
    fun fetchOrderSummary(fromDate: String, toDate: String): Flow<List<OrderSummaryEntity>>

    @Query("SELECT * FROM order_summary WHERE id = :orderID LIMIT 1")
    fun fetchOrderSummaryById(orderID: Int): Flow<OrderSummaryEntity?>

    @Update
    suspend fun updateOrders(orders: List<OrderSummaryEntity>)

    @Query("SELECT COUNT(*) FROM order_summary WHERE isSynced = 0 AND isDeleted=0")
    suspend fun getUnsyncedOrdersCount(): Int

    @Query("SELECT * FROM order_summary WHERE isSynced = 0")
    fun getUnsyncedOrderEntities(): Flow<List<OrderSummaryEntity>>

    @Query("UPDATE order_summary SET isSynced = 1 WHERE id = :orderId")
    suspend fun updateOrderSynced(orderId: Long)

    @Query("UPDATE order_summary SET isDeleted = 1 WHERE id = :orderId")
    suspend fun updateDeleteStatus(orderId: Long)

    @Query("SELECT MAX(id) FROM order_summary")
    suspend fun getLastOrderId(): Int?

    @Query("UPDATE order_summary SET customerID = :serverID WHERE customerID = :customerID")
    suspend fun updateOrderCustomerID(serverID: Int?, customerID: Int)



}