package com.ian.grithaul.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ian.grithaul.data.local.entities.ComplaintEntity
import com.ian.grithaul.data.local.entities.NotificationEntity
import com.ian.grithaul.data.local.entities.PickupEntity
import com.ian.grithaul.data.local.entities.StopLogEntity
import com.ian.grithaul.data.local.entities.TruckEntity
import com.ian.grithaul.data.remote.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class DriverViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    // UI States
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _successMessage = MutableStateFlow("")
    val successMessage: StateFlow<String> = _successMessage

    private val _pickups = MutableStateFlow<List<PickupEntity>>(emptyList())
    val pickups: StateFlow<List<PickupEntity>> = _pickups

    private val _truck = MutableStateFlow<TruckEntity?>(null)
    val truck: StateFlow<TruckEntity?> = _truck

    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications

    private val _completedCount = MutableStateFlow(0)
    val completedCount: StateFlow<Int> = _completedCount

    private val _totalWeight = MutableStateFlow(0f)
    val totalWeight: StateFlow<Float> = _totalWeight

    private val _isNearCapacity = MutableStateFlow(false)
    val isNearCapacity: StateFlow<Boolean> = _isNearCapacity

    // Fetch route for driver
    fun fetchRoute(truckId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = firebaseRepository.getPickupsByDriver(truckId)
            if (result.isSuccess) {
                _pickups.value = result.getOrNull() ?: emptyList()
                _completedCount.value = _pickups.value
                    .count { it.status == "collected" }
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Failed to fetch route"
            }
            _isLoading.value = false
        }
    }

    // Fetch truck info
    fun fetchTruck(zoneId: String) {
        viewModelScope.launch {
            val result = firebaseRepository.getTrucksByZone(zoneId)
            if (result.isSuccess) {
                _truck.value = result.getOrNull()?.firstOrNull()
            }
        }
    }

    // Mark stop as collected
    fun markCollected(
        pickupId: String,
        driverId: String,
        truckId: String,
        weightKg: Float
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            val stopLog = StopLogEntity(
                id = UUID.randomUUID().toString(),
                pickupId = pickupId,
                driverId = driverId,
                truckId = truckId,
                action = "collected",
                weightKg = weightKg,
                timestamp = System.currentTimeMillis()
            )

            val result = firebaseRepository.logStop(stopLog)

            if (result.isSuccess) {
                _successMessage.value = "Stop marked as collected"
                _totalWeight.value += weightKg
                _completedCount.value += 1

                // Check if near capacity
                val capacity = _truck.value?.capacityTonnes ?: 8f
                val currentLoadTonnes = _totalWeight.value / 1000f
                _isNearCapacity.value = currentLoadTonnes >= capacity * 0.85f

                // Update truck load in Firestore
                firebaseRepository.updateTruckLoad(truckId, currentLoadTonnes)

                // Refresh route
                fetchRoute(truckId)

                // Send notification to resident
                val notification = NotificationEntity(
                    id = UUID.randomUUID().toString(),
                    userId = pickupId,
                    title = "Pickup Completed",
                    message = "Your waste has been collected successfully.",
                    type = "pickup_complete",
                    isRead = false
                )
                firebaseRepository.sendNotification(notification)

            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Failed to mark stop as collected"
            }

            _isLoading.value = false
        }
    }

    // Mark stop as skipped
    fun markSkipped(
        pickupId: String,
        driverId: String,
        truckId: String,
        residentId: String,
        zoneId: String,
        reason: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            val stopLog = StopLogEntity(
                id = UUID.randomUUID().toString(),
                pickupId = pickupId,
                driverId = driverId,
                truckId = truckId,
                action = "skipped",
                reason = reason,
                timestamp = System.currentTimeMillis()
            )

            val result = firebaseRepository.logStop(stopLog)

            if (result.isSuccess) {
                _successMessage.value = "Stop marked as skipped"

                // Auto generate complaint
                val complaint = ComplaintEntity(
                    id = UUID.randomUUID().toString(),
                    residentId = residentId,
                    pickupId = pickupId,
                    zoneId = zoneId,
                    issueType = "Missed Pickup",
                    description = "Pickup was skipped by driver. Reason: $reason",
                    status = "pending"
                )
                firebaseRepository.submitComplaint(complaint)

                // Send notification to resident
                val notification = NotificationEntity(
                    id = UUID.randomUUID().toString(),
                    userId = residentId,
                    title = "Pickup Missed",
                    message = "Unfortunately your pickup was skipped. A complaint has been automatically raised.",
                    type = "pickup_missed",
                    isRead = false
                )
                firebaseRepository.sendNotification(notification)

                fetchRoute(truckId)

            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Failed to mark stop as skipped"
            }

            _isLoading.value = false
        }
    }

    // Fetch notifications
    fun fetchNotifications(userId: String) {
        viewModelScope.launch {
            val result = firebaseRepository.getNotificationsByUser(userId)
            if (result.isSuccess) {
                _notifications.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    // Clear messages
    fun clearError() {
        _errorMessage.value = ""
    }

    fun clearSuccess() {
        _successMessage.value = ""
    }
}

