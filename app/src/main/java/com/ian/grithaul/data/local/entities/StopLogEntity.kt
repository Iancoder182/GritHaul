package com.ian.grithaul.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stop_logs")
data class StopLogEntity(
    @PrimaryKey
    val id: String = "",
    val pickupId: String = "",
    val driverId: String = "",
    val truckId: String = "",
    val action: String = "",
    val weightKg: Float = 0f,
    val reason: String = "",
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

