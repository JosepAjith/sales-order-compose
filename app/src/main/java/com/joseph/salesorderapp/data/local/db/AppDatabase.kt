package com.joseph.salesorderapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joseph.salesorderapp.data.local.dao.CustomerDao
import com.joseph.salesorderapp.data.local.dao.ProductDao
import com.joseph.salesorderapp.data.local.dao.UserDao
import com.joseph.salesorderapp.data.local.dao.order.OrderDetailsDao
import com.joseph.salesorderapp.data.local.dao.order.OrderSummaryDao
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import com.joseph.salesorderapp.data.local.entity.UserEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderDetailsEntity
import com.joseph.salesorderapp.data.local.entity.order.OrderSummaryEntity

@Database(
    entities = [
        CustomerEntity::class,
        ProductEntity::class,
        OrderSummaryEntity::class,
        OrderDetailsEntity::class,
        UserEntity::class,
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun orderSummaryDao(): OrderSummaryDao
    abstract fun orderDetailsDao(): OrderDetailsDao
}
