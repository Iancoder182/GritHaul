package com.ian.grithaul.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ondemand_requests")
data class OnDemandRequestEntity(
    @PrimaryKey
    val id: String = "",
    val residentId: String = "",
    val zoneId: String = "",
    val wasteType: String = "",
    val volume: String = "",
    val preferredDate: String = "",
    val timeWindow: String = "",
    val notes: String = "",
    val status: String = "pending",
    val assignedTruckId: String = "",
    val adminNotes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
