package com.joseph.salesorderapp.data.local.dao.order

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity

@Dao
interface OrderDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderDetailsEntity: OrderDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderDetailsEntity>)


}

