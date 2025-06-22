package com.joseph.salesorderapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
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

    @Update
    suspend fun updateCustomers(customers: List<CustomerEntity>)

    @Query("SELECT COUNT(*) FROM customers WHERE isSynced = 0")
    suspend fun getUnsyncedCustomersCount(): Int

    @Query("SELECT * FROM customers WHERE isSynced = 0")
    fun getUnsyncedCustomerEntities(): Flow<List<CustomerEntity>>

    @Query("UPDATE customers SET isSynced = 1 WHERE id = :customerID")
    suspend fun updateCustomerSynced(customerID: Long)


}
