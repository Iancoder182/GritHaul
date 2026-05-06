package com.ian.grithaul.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey
    val id: String = "",
    val residentId: String = "",
    val pickupId: String = "",
    val zoneId: String = "",
    val issueType: String = "",
    val description: String = "",
    val status: String = "pending",
    val adminResponse: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val resolvedAt: Long = 0L
)
