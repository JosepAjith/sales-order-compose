package com.joseph.salesorderapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val serverId: Int,
    val name: String,
    val email: String,
    val phoneNo: String,
    val isSynced: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
