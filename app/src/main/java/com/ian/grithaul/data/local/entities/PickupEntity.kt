package com.ian.grithaul.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pickups")
data class PickupEntity(
    @PrimaryKey
    val id: String = "",
    val residentId: String = "",
    val zoneId: String = "",
    val truckId: String = "",
    val scheduledDate: String = "",
    val scheduledTime: String = "",
    val wasteType: String = "",
    val volume: String = "",
    val status: String = "scheduled",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
