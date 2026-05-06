package com.ian.grithaul.data.local.dao

import androidx.room.*
import com.ian.grithaul.data.local.entities.OnDemandRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OnDemandRequestDao {

    @Query("SELECT * FROM ondemand_requests WHERE residentId = :userId ORDER BY createdAt DESC")
    fun getUserRequests(userId: String): Flow<List<OnDemandRequestEntity>>

    @Query("SELECT * FROM ondemand_requests WHERE status = :status")
    fun getRequestsByStatus(status: String): Flow<List<OnDemandRequestEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: OnDemandRequestEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequests(requests: List<OnDemandRequestEntity>)

    @Update
    suspend fun updateRequest(request: OnDemandRequestEntity)

    @Delete
    suspend fun deleteRequest(request: OnDemandRequestEntity)

    @Query("DELETE FROM ondemand_requests")
    suspend fun clearRequests()
}

