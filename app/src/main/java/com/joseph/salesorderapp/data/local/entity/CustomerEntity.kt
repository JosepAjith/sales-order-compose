package com.joseph.salesorderapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val serverId: Int,
    val name: String,
    val phoneNo: String,
    val email: String? = null,

    val address: String? = null,
    val city: String? = null,
    val pinCode: String? = null,

    val trnNo: String? = null,
    val gstNo: String? = null,

    val customerType: String = "Retail",
    val isSynced: Boolean = false,

    val createdAt: String? = null,
    val updatedAt: String? = null
)
