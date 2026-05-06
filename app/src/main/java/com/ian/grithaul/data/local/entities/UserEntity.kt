package com.ian.grithaul.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val zone: String = "",
    val role: String = "resident",
    val createdAt: Long = System.currentTimeMillis()
)

