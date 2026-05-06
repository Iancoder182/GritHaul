package com.ian.grithaul.data.local.dao

import androidx.room.*
import com.ian.grithaul.data.local.entities.TruckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckDao {

    @Query("SELECT * FROM trucks")
    fun getAllTrucks(): Flow<List<TruckEntity>>

    @Query("SELECT * FROM trucks WHERE id = :truckId LIMIT 1")
    fun getTruck(truckId: String): Flow<TruckEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTruck(truck: TruckEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrucks(trucks: List<TruckEntity>)

    @Update
    suspend fun updateTruck(truck: TruckEntity)

    @Delete
    suspend fun deleteTruck(truck: TruckEntity)

    @Query("DELETE FROM trucks")
    suspend fun clearTrucks()
}

