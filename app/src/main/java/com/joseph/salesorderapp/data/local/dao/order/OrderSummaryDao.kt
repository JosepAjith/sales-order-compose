package com.joseph.salesorderapp.data.local.dao.order

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

}