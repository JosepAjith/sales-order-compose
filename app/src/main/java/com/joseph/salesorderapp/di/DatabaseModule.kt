package com.joseph.salesorderapp.di

import android.content.Context
import androidx.room.Room
import com.joseph.salesorderapp.data.local.dao.CustomerDao
import com.joseph.salesorderapp.data.local.dao.ProductDao
import com.joseph.salesorderapp.data.local.dao.UserDao
import com.joseph.salesorderapp.data.local.dao.order.OrderDetailsDao
import com.joseph.salesorderapp.data.local.dao.order.OrderSummaryDao
import com.joseph.salesorderapp.data.local.db.AppDatabase
import com.joseph.salesorderapp.data.local.db.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).addMigrations(MIGRATION_1_2).build()

    @Provides
    fun provideCustomerDao(db: AppDatabase): CustomerDao = db.customerDao()

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()

    @Provides
    fun provideOrderSummaryDao(db: AppDatabase): OrderSummaryDao = db.orderSummaryDao()

    @Provides
    fun provideOrderDetailsDao(db: AppDatabase): OrderDetailsDao = db.orderDetailsDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
}
