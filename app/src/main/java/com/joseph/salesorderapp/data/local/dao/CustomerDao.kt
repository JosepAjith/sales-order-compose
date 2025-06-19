package com.joseph.salesorderapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(customers: List<CustomerEntity>)

    @Query("SELECT * FROM customers WHERE name LIKE '%' || :query || '%' LIMIT 10")
    fun fetchCustomers(query: String): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Int): CustomerEntity?

    @Query("DELETE FROM customers")
    suspend fun deleteAllCustomers()

    @Query("SELECT * FROM customers WHERE isSynced = 0")
    suspend fun getUnsyncedCustomers(): List<CustomerEntity>

    @Update
    suspend fun updateCustomers(customers: List<CustomerEntity>)

}
