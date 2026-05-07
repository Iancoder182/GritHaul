package com.ian.grithaul.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.ian.grithaul.data.local.entities.ComplaintEntity
import com.ian.grithaul.data.local.entities.NotificationEntity
import com.ian.grithaul.data.local.entities.OnDemandRequestEntity
import com.ian.grithaul.data.local.entities.PickupConfirmationEntity
import com.ian.grithaul.data.local.entities.PickupEntity
import com.ian.grithaul.data.local.entities.StopLogEntity
import com.ian.grithaul.data.local.entities.TruckEntity
import com.ian.grithaul.data.local.entities.ZoneEntity
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // ─── Zones ───────────────────────────────────────────────

    suspend fun getZones(): Result<List<ZoneEntity>> {
        return try {
            val snapshot = firestore.collection("zones").get().await()
            val zones = snapshot.toObjects(ZoneEntity::class.java)
            Result.success(zones)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Pickups ──────────────────────────────────────────────

    suspend fun getPickupsByZone(zoneId: String): Result<List<PickupEntity>> {
        return try {
            val snapshot = firestore.collection("pickups")
                .whereEqualTo("zoneId", zoneId)
                .get()
                .await()
            val pickups = snapshot.toObjects(PickupEntity::class.java)
            Result.success(pickups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPickupsByResident(residentId: String): Result<List<PickupEntity>> {
        return try {
            val snapshot = firestore.collection("pickups")
                .whereEqualTo("residentId", residentId)
                .get()
                .await()
            val pickups = snapshot.toObjects(PickupEntity::class.java)
            Result.success(pickups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPickupsByDriver(truckId: String): Result<List<PickupEntity>> {
        return try {
            val snapshot = firestore.collection("pickups")
                .whereEqualTo("truckId", truckId)
                .whereEqualTo("status", "confirmed")
                .get()
                .await()
            val pickups = snapshot.toObjects(PickupEntity::class.java)
            Result.success(pickups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Pickup Confirmations ─────────────────────────────────

    suspend fun confirmPickup(
        confirmation: PickupConfirmationEntity
    ): Result<Unit> {
        return try {
            firestore.collection("pickup_confirmations")
                .document(confirmation.id)
                .set(confirmation)
                .await()

            // Update pickup status to confirmed
            firestore.collection("pickups")
                .document(confirmation.pickupId)
                .update("status", "confirmed")
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── On Demand Requests ───────────────────────────────────

    suspend fun submitOnDemandRequest(
        request: OnDemandRequestEntity
    ): Result<Unit> {
        return try {
            firestore.collection("ondemand_requests")
                .document(request.id)
                .set(request)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPendingOnDemandRequests(): Result<List<OnDemandRequestEntity>> {
        return try {
            val snapshot = firestore.collection("ondemand_requests")
                .whereEqualTo("status", "pending")
                .get()
                .await()
            val requests = snapshot.toObjects(OnDemandRequestEntity::class.java)
            Result.success(requests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateOnDemandRequestStatus(
        requestId: String,
        status: String,
        assignedTruckId: String = ""
    ): Result<Unit> {
        return try {
            val updates = mutableMapOf<String, Any>("status" to status)
            if (assignedTruckId.isNotEmpty()) {
                updates["assignedTruckId"] = assignedTruckId
            }
            firestore.collection("ondemand_requests")
                .document(requestId)
                .update(updates)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Stop Logs ────────────────────────────────────────────

    suspend fun logStop(stopLog: StopLogEntity): Result<Unit> {
        return try {
            firestore.collection("stop_logs")
                .document(stopLog.id)
                .set(stopLog)
                .await()

            // Update pickup status
            firestore.collection("pickups")
                .document(stopLog.pickupId)
                .update("status", stopLog.action)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Complaints ───────────────────────────────────────────

    suspend fun submitComplaint(complaint: ComplaintEntity): Result<Unit> {
        return try {
            firestore.collection("complaints")
                .document(complaint.id)
                .set(complaint)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getComplaintsByResident(
        residentId: String
    ): Result<List<ComplaintEntity>> {
        return try {
            val snapshot = firestore.collection("complaints")
                .whereEqualTo("residentId", residentId)
                .get()
                .await()
            val complaints = snapshot.toObjects(ComplaintEntity::class.java)
            Result.success(complaints)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllComplaints(): Result<List<ComplaintEntity>> {
        return try {
            val snapshot = firestore.collection("complaints").get().await()
            val complaints = snapshot.toObjects(ComplaintEntity::class.java)
            Result.success(complaints)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateComplaintStatus(
        complaintId: String,
        status: String
    ): Result<Unit> {
        return try {
            firestore.collection("complaints")
                .document(complaintId)
                .update("status", status)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Trucks ───────────────────────────────────────────────

    suspend fun getTrucksByZone(zoneId: String): Result<List<TruckEntity>> {
        return try {
            val snapshot = firestore.collection("trucks")
                .whereEqualTo("zoneId", zoneId)
                .get()
                .await()
            val trucks = snapshot.toObjects(TruckEntity::class.java)
            Result.success(trucks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTruckLoad(
        truckId: String,
        currentLoadTonnes: Float
    ): Result<Unit> {
        return try {
            firestore.collection("trucks")
                .document(truckId)
                .update("currentLoadTonnes", currentLoadTonnes)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Notifications ────────────────────────────────────────

    suspend fun sendNotification(
        notification: NotificationEntity
    ): Result<Unit> {
        return try {
            firestore.collection("notifications")
                .document(notification.id)
                .set(notification)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNotificationsByUser(
        userId: String
    ): Result<List<NotificationEntity>> {
        return try {
            val snapshot = firestore.collection("notifications")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            val notifications = snapshot.toObjects(NotificationEntity::class.java)
            Result.success(notifications)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markNotificationAsRead(
        notificationId: String
    ): Result<Unit> {
        return try {
            firestore.collection("notifications")
                .document(notificationId)
                .update("isRead", true)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

