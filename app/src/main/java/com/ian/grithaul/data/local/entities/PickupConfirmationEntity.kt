package com.ian.grithaul.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pickup_confirmations")
data class PickupConfirmationEntity(
    @PrimaryKey
    val id: String = "",
    val pickupId: String = "",
    val residentId: String = "",
    val wasteType: String = "",
    val volume: String = "",
    val notes: String = "",
    val confirmedAt: Long = System.currentTimeMillis(),
    val status: String = "confirmed"
)