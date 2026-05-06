package com.ian.grithaul.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zones")
data class ZoneEntity(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val color: String = "",
    val residentCount: Int = 0,
    val collectionDays: String = "",
    val assignedTruckId: String = ""
)