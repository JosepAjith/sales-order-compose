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

    @Query("SELECT * FROM order_summary")
    fun fetchSummary(): Flow<List<OrderSummaryEntity>>

    @Query("SELECT * FROM order_summary WHERE orderDate BETWEEN :fromDate AND :toDate")
    fun fetchOrderSummary(fromDate: String, toDate: String): Flow<List<OrderSummaryEntity>>

    @Query("SELECT * FROM order_summary WHERE id = :orderID LIMIT 1")
    fun fetchOrderSummaryById(orderID: Int): Flow<OrderSummaryEntity?>

    @Query("SELECT * FROM order_summary WHERE isSynced = 0")
    suspend fun getUnsyncedOrders(): List<OrderSummaryEntity>

    @Update
    suspend fun updateOrders(orders: List<OrderSummaryEntity>)


}