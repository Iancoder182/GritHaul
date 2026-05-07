package com.ian.grithaul.data.repository

import android.content.Context
import com.ian.grithaul.data.local.AppDatabase
import com.ian.grithaul.data.local.entities.ComplaintEntity
import com.ian.grithaul.data.local.entities.NotificationEntity
import com.ian.grithaul.data.local.entities.OnDemandRequestEntity
import com.ian.grithaul.data.local.entities.PickupConfirmationEntity
import com.ian.grithaul.data.local.entities.PickupEntity
import com.ian.grithaul.data.local.entities.StopLogEntity
import com.ian.grithaul.data.local.entities.TruckEntity
import com.ian.grithaul.data.local.entities.ZoneEntity
import com.ian.grithaul.data.remote.FirebaseRepository
import kotlinx.coroutines.flow.Flow

/**
 * GritRepository — single source of truth for all app data.
 *
 * Strategy: fetch from Firebase (remote), cache locally in Room, and
 * expose local data to the UI. Falls back to local cache when offline.
 */
class GritRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val remote = FirebaseRepository()

    // DAOs
    private val pickupDao       = db.pickupDao()
    private val complaintDao    = db.complaintDao()
    private val notificationDao = db.notificationDao()
    private val onDemandDao     = db.onDemandRequestDao()
    private val truckDao        = db.truckDao()

    // ─── Pickups ──────────────────────────────────────────────

    /** Fetch pickups for a resident from Firebase and cache them locally. */
    suspend fun syncPickupsForResident(residentId: String): Result<Unit> {
        return try {
            val result = remote.getPickupsByResident(residentId)
            result.onSuccess { pickups ->
                pickupDao.clearPickups()
                pickupDao.insertPickups(pickups)
            }
            result.map { }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Fetch pickups for a driver from Firebase and cache them locally. */
    suspend fun syncPickupsForDriver(truckId: String): Result<Unit> {
        return try {
            val result = remote.getPickupsByDriver(truckId)
            result.onSuccess { pickups ->
                pickupDao.clearPickups()
                pickupDao.insertPickups(pickups)
            }
            result.map { }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Observe pickups by status from local cache (reactive). */
    fun observePickupsByStatus(status: String): Flow<List<PickupEntity>> =
        pickupDao.getPickupsByStatus(status)

    // ─── Pickup Confirmation ──────────────────────────────────

    /** Confirm a pickup on Firebase then update the local record. */
    suspend fun confirmPickup(confirmation: PickupConfirmationEntity): Result<Unit> {
        return try {
            val result = remote.confirmPickup(confirmation)
            result.onSuccess {
                // Fetch the existing pickup and update only its status
                val existing = pickupDao.getPickupById(confirmation.pickupId)
                if (existing != null) {
                    pickupDao.updatePickup(existing.copy(status = "confirmed"))
                }
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── On-Demand Requests ───────────────────────────────────

    /** Submit an on-demand pickup request to Firebase and cache locally. */
    suspend fun submitOnDemandRequest(request: OnDemandRequestEntity): Result<Unit> {
        return try {
            val result = remote.submitOnDemandRequest(request)
            result.onSuccess {
                onDemandDao.insertRequest(request)
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Observe a resident's on-demand requests from local cache (reactive). */
    fun observeUserOnDemandRequests(userId: String): Flow<List<OnDemandRequestEntity>> =
        onDemandDao.getUserRequests(userId)

    /** Fetch all pending on-demand requests from Firebase (admin view). */
    suspend fun getPendingOnDemandRequests(): Result<List<OnDemandRequestEntity>> {
        return try {
            remote.getPendingOnDemandRequests()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Update the status of an on-demand request (admin action). */
    suspend fun updateOnDemandRequestStatus(
        requestId: String,
        status: String,
        assignedTruckId: String = ""
    ): Result<Unit> {
        return try {
            remote.updateOnDemandRequestStatus(requestId, status, assignedTruckId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Stop Logs ────────────────────────────────────────────

    /** Log a driver stop and sync with Firebase. */
    suspend fun logStop(stopLog: StopLogEntity): Result<Unit> {
        return try {
            remote.logStop(stopLog)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Complaints ───────────────────────────────────────────

    /** Submit a complaint to Firebase and save it locally. */
    suspend fun submitComplaint(complaint: ComplaintEntity): Result<Unit> {
        return try {
            val result = remote.submitComplaint(complaint)
            result.onSuccess {
                complaintDao.insertComplaint(complaint)
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Get complaints for the current resident from local cache. */
    fun getComplaintsByResident(residentId: String): List<ComplaintEntity> =
        complaintDao.getComplaintsByUser(residentId)

    /** Fetch all complaints from Firebase (admin view). */
    suspend fun getAllComplaints(): Result<List<ComplaintEntity>> {
        return try {
            remote.getAllComplaints()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Update a complaint's status on Firebase (admin action). */
    suspend fun updateComplaintStatus(complaintId: String, status: String): Result<Unit> {
        return try {
            remote.updateComplaintStatus(complaintId, status)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Trucks ───────────────────────────────────────────────

    /** Fetch trucks for a zone from Firebase and cache locally. */
    suspend fun syncTrucksByZone(zoneId: String): Result<Unit> {
        return try {
            val result = remote.getTrucksByZone(zoneId)
            result.onSuccess { trucks ->
                truckDao.insertTrucks(trucks)
            }
            result.map { }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Observe all cached trucks (reactive). */
    fun observeAllTrucks(): Flow<List<TruckEntity>> = truckDao.getAllTrucks()

    /** Observe a single truck by ID (reactive). */
    fun observeTruck(truckId: String): Flow<TruckEntity?> = truckDao.getTruck(truckId)

    /** Update a truck's current load on Firebase. */
    suspend fun updateTruckLoad(truckId: String, currentLoadTonnes: Float): Result<Unit> {
        return try {
            remote.updateTruckLoad(truckId, currentLoadTonnes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Zones ────────────────────────────────────────────────

    /** Fetch all zones from Firebase. */
    suspend fun getZones(): Result<List<ZoneEntity>> {
        return try {
            remote.getZones()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Notifications ────────────────────────────────────────

    /** Send a notification via Firebase and store it locally. */
    suspend fun sendNotification(notification: NotificationEntity): Result<Unit> {
        return try {
            val result = remote.sendNotification(notification)
            result.onSuccess {
                notificationDao.insertNotification(notification)
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Sync notifications for a user from Firebase and cache them locally. */
    suspend fun syncNotificationsForUser(userId: String): Result<Unit> {
        return try {
            val result = remote.getNotificationsByUser(userId)
            result.onSuccess { notifications ->
                notificationDao.insertNotifications(notifications)
            }
            result.map { }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Observe notifications for a specific user from local cache (reactive). */
    fun observeNotificationsForUser(userId: String): Flow<List<NotificationEntity>> =
        notificationDao.getUserNotifications(userId)

    /** Observe all unread notifications (reactive). */
    fun observeUnreadNotifications(): Flow<List<NotificationEntity>> =
        notificationDao.getUnreadNotifications()

    /** Mark a notification as read on Firebase and update local cache. */
    suspend fun markNotificationAsRead(notificationId: String): Result<Unit> {
        return try {
            val result = remote.markNotificationAsRead(notificationId)
            result.onSuccess {
                notificationDao.markAsRead(notificationId)
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

