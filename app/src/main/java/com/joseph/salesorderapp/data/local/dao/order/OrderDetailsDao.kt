package com.joseph.salesorderapp.data.local.dao.order

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.domain.model.ItemWiseReport
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderDetailsEntity: OrderDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderDetailsEntity>)

    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    fun fetchReportItemList(orderId: Int): Flow<List<OrderDetailsEntity>>

    @Query("UPDATE order_details SET isSynced = 1 WHERE orderId = :orderId")
    suspend fun updateOrderItemsSyncStatus(orderId: Long)

    @Query(
        """
    SELECT productID AS productId, productName, productCode,
           SUM(quantity) AS totalQty,
           SUM(quantity * pricePerUnit) AS totalAmount
    FROM order_details
    WHERE orderDate BETWEEN :fromDate AND :toDate
      AND (:customerId IS NULL OR customerID = :customerId)
      AND isDeleted IS 0
    GROUP BY productID
    """
    )
    fun fetchItemWiseReport(
        fromDate: String,
        toDate: String,
        customerId: Int? = null
    ): Flow<List<ItemWiseReport>>


    @Query("UPDATE order_details SET customerID = :serverID WHERE customerID = :customerID")
    suspend fun updateOrderDetailCustomerID(serverID: Int?, customerID: Int)

    @Query("UPDATE order_details SET isDeleted = 1 WHERE orderId = :orderId")
    suspend fun updateItemDeleteStatus(orderId: Long)

}

