package com.joseph.salesorderapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    // Insert a single customer
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    // Insert a list of customers
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(customers: List<CustomerEntity>)

    // Get all customers
    @Query("SELECT * FROM customers")
    fun fetchCustomers(): Flow<List<CustomerEntity>>

    // Get customer by ID
    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Int): CustomerEntity?

    // Delete all customers (for syncing or clearing data)
    @Query("DELETE FROM customers")
    suspend fun deleteAllCustomers()

}
