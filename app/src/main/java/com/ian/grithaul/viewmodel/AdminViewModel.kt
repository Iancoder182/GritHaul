package com.ian.grithaul.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ian.grithaul.data.local.entities.ComplaintEntity
import com.ian.grithaul.data.local.entities.NotificationEntity
import com.ian.grithaul.data.local.entities.OnDemandRequestEntity
import com.ian.grithaul.data.local.entities.PickupEntity
import com.ian.grithaul.data.local.entities.ZoneEntity
import com.ian.grithaul.data.remote.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AdminViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    // UI States
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _successMessage = MutableStateFlow("")
    val successMessage: StateFlow<String> = _successMessage

    private val _zones = MutableStateFlow<List<ZoneEntity>>(emptyList())
    val zones: StateFlow<List<ZoneEntity>> = _zones

    private val _allPickups = MutableStateFlow<List<PickupEntity>>(emptyList())
    val allPickups: StateFlow<List<PickupEntity>> = _allPickups

    private val _pendingRequests = MutableStateFlow<List<OnDemandRequestEntity>>(emptyList())
    val pendingRequests: StateFlow<List<OnDemandRequestEntity>> = _pendingRequests

    private val _allComplaints = MutableStateFlow<List<ComplaintEntity>>(emptyList())
    val allComplaints: StateFlow<List<ComplaintEntity>> = _allComplaints

    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications

    // Computed stats
    val totalPickups: Int get() = _allPickups.value.size
    val completedPickups: Int get() = _allPickups.value.count { it.status == "collected" }
    val openComplaints: Int get() = _allComplaints.value.count { it.status == "pending" }
    val completionRate: Float
        get() = if (totalPickups == 0) 0f
        else completedPickups.toFloat() / totalPickups.toFloat() * 100f

    // Fetch all data for admin dashboard
    fun fetchDashboardData() {
        viewModelScope.launch {
            _isLoading.value = true
            fetchZones()
            fetchPendingRequests()
            fetchAllComplaints()
            _isLoading.value = false
        }
    }

    // Fetch all zones
    private fun fetchZones() {
        viewModelScope.launch {
            val result = firebaseRepository.getZones()
            if (result.isSuccess) {
                _zones.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    // Fetch pickups for a zone
    fun fetchPickupsByZone(zoneId: String) {
        viewModelScope.launch {
            val result = firebaseRepository.getPickupsByZone(zoneId)
            if (result.isSuccess) {
                _allPickups.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    // Fetch pending on demand requests
    fun fetchPendingRequests() {
        viewModelScope.launch {
            val result = firebaseRepository.getPendingOnDemandRequests()
            if (result.isSuccess) {
                _pendingRequests.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    // Approve on demand request
    fun approveRequest(requestId: String, assignedTruckId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            val result = firebaseRepository.updateOnDemandRequestStatus(
                requestId = requestId,
                status = "approved",
                assignedTruckId = assignedTruckId
            )

            if (result.isSuccess) {
                _successMessage.value = "Request approved successfully"

                // Send notification to resident
                val notification = NotificationEntity(
                    id = UUID.randomUUID().toString(),
                    userId = requestId,
                    title = "Request Approved",
                    message = "Your on-demand pickup request has been approved and a truck has been assigned.",
                    type = "request_approved",
                    isRead = false
                )
                firebaseRepository.sendNotification(notification)

                fetchPendingRequests()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Failed to approve request"
            }

            _isLoading.value = false
        }
    }

    // Reject on demand request
    fun rejectRequest(requestId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            val result = firebaseRepository.updateOnDemandRequestStatus(
                requestId = requestId,
                status = "rejected"
            )

            if (result.isSuccess) {
                _successMessage.value = "Request rejected"

                // Send notification to resident
                val notification = NotificationEntity(
                    id = UUID.randomUUID().toString(),
                    userId = requestId,
                    title = "Request Rejected",
                    message = "Unfortunately your on-demand pickup request has been rejected.",
                    type = "request_rejected",
                    isRead = false
                )
                firebaseRepository.sendNotification(notification)

                fetchPendingRequests()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Failed to reject request"
            }

            _isLoading.value = false
        }
    }

    // Fetch all complaints
    fun fetchAllComplaints() {
        viewModelScope.launch {
            val result = firebaseRepository.getAllComplaints()
            if (result.isSuccess) {
                _allComplaints.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    // Resolve complaint
    fun resolveComplaint(complaintId: String, residentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            val result = firebaseRepository.updateComplaintStatus(
                complaintId = complaintId,
                status = "resolved"
            )

            if (result.isSuccess) {
                _successMessage.value = "Complaint resolved"

                // Send notification to resident
                val notification = NotificationEntity(
                    id = UUID.randomUUID().toString(),
                    userId = residentId,
                    title = "Complaint Resolved",
                    message = "Your complaint has been reviewed and resolved by the admin.",
                    type = "complaint_resolved",
                    isRead = false
                )
                firebaseRepository.sendNotification(notification)

                fetchAllComplaints()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Failed to resolve complaint"
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

