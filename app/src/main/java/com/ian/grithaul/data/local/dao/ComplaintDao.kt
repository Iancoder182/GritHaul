package com.ian.grithaul.data.local.dao

import androidx.room.*
import com.ian.grithaul.data.local.entities.ComplaintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintDao {

    @Query("SELECT * FROM complaints WHERE residentId = :userId ORDER BY createdAt DESC")
    fun getComplaintsByUser(userId: String): List<ComplaintEntity>

    @Query("SELECT * FROM complaints WHERE status = :status")
    fun getComplaintsByStatus(status: String): Flow<List<ComplaintEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: ComplaintEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaints(complaints: List<ComplaintEntity>)

    @Update
    suspend fun updateComplaint(complaint: ComplaintEntity)

    @Delete
    suspend fun deleteComplaint(complaint: ComplaintEntity)

    @Query("DELETE FROM complaints")
    suspend fun clearComplaints()
}

