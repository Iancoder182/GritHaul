package com.ian.grithaul.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ian.grithaul.data.local.dao.ComplaintDao
import com.ian.grithaul.data.local.dao.NotificationDao
import com.ian.grithaul.data.local.dao.OnDemandRequestDao
import com.ian.grithaul.data.local.dao.PickupDao
import com.ian.grithaul.data.local.dao.TruckDao
import com.ian.grithaul.data.local.dao.UserDao
import com.ian.grithaul.data.local.entities.ComplaintEntity
import com.ian.grithaul.data.local.entities.NotificationEntity
import com.ian.grithaul.data.local.entities.OnDemandRequestEntity
import com.ian.grithaul.data.local.entities.PickupConfirmationEntity
import com.ian.grithaul.data.local.entities.PickupEntity
import com.ian.grithaul.data.local.entities.StopLogEntity
import com.ian.grithaul.data.local.entities.TruckEntity
import com.ian.grithaul.data.local.entities.UserEntity
import com.ian.grithaul.data.local.entities.ZoneEntity

@Database(
    entities = [
        UserEntity::class,
        ZoneEntity::class,
        PickupEntity::class,
        TruckEntity::class,
        PickupConfirmationEntity::class,
        OnDemandRequestEntity::class,
        StopLogEntity::class,
        ComplaintEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun pickupDao(): PickupDao
    abstract fun truckDao(): TruckDao
    abstract fun complaintDao(): ComplaintDao
    abstract fun notificationDao(): NotificationDao
    abstract fun onDemandRequestDao(): OnDemandRequestDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "grithaul_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}