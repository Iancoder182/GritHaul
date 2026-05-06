package com.ian.grithaul.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trucks")
data class TruckEntity(
    @PrimaryKey
    val id: String = "",
    val driverId: String = "",
    val zoneId: String = "",
    val plateNumber: String = "",
    val capacityTonnes: Float = 0f,
    val currentLoadTonnes: Float = 0f,
    val status: String = "idle",
    val lastUpdated: Long = System.currentTimeMillis()
)