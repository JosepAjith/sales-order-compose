package com.joseph.salesorderapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joseph.salesorderapp.data.local.dao.CustomerDao
import com.joseph.salesorderapp.data.local.dao.ProductDao
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity

@Database(
    entities = [
        CustomerEntity::class,
        ProductEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun productDao(): ProductDao
}
