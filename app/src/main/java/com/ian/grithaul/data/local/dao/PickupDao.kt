package com.ian.grithaul.data.local.dao

import androidx.room.*
import com.ian.grithaul.data.local.entities.ComplaintEntity
import com.ian.grithaul.data.local.entities.PickupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PickupDao {

    @Query("SELECT * FROM complaints WHERE residentId = :userId ORDER BY createdAt DESC")
    fun getComplaintsByUser(userId: String): List<ComplaintEntity>

    @Query("SELECT * FROM pickups WHERE status = :status")
    fun getPickupsByStatus(status: String): Flow<List<PickupEntity>>

    @Query("SELECT * FROM pickups WHERE id = :pickupId LIMIT 1")
    suspend fun getPickupById(pickupId: String): PickupEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPickup(pickup: PickupEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPickups(pickups: List<PickupEntity>)

    @Update
    suspend fun updatePickup(pickup: PickupEntity)

    @Delete
    suspend fun deletePickup(pickup: PickupEntity)

    @Query("DELETE FROM pickups")
    suspend fun clearPickups()
}