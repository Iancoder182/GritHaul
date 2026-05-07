package com.ian.grithaul.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ian.grithaul.data.local.entities.ComplaintEntity
import com.ian.grithaul.data.local.entities.NotificationEntity
import com.ian.grithaul.data.local.entities.OnDemandRequestEntity
import com.ian.grithaul.data.local.entities.PickupConfirmationEntity
import com.ian.grithaul.data.local.entities.PickupEntity
import com.ian.grithaul.data.remote.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ResidentViewModel : ViewModel() {

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

    private val _nextPickup = MutableStateFlow<PickupEntity?>(null)
    val nextPickup: StateFlow<PickupEntity?> = _nextPickup

    private val _complaints = MutableStateFlow<List<ComplaintEntity>>(emptyList())
    val complaints: StateFlow<List<ComplaintEntity>> = _complaints

    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications

    private val _onDemandRequests = MutableStateFlow<List<OnDemandRequestEntity>>(emptyList())
    val onDemandRequests: StateFlow<List<OnDemandRequestEntity>> = _onDemandRequests

    private val _confirmationSuccess = MutableStateFlow(false)
    val confirmationSuccess: StateFlow<Boolean> = _confirmationSuccess

    private val _requestSuccess = MutableStateFlow(false)
    val requestSuccess: StateFlow<Boolean> = _requestSuccess

    private val _complaintSuccess = MutableStateFlow(false)
    val complaintSuccess: StateFlow<Boolean> = _complaintSuccess

    // Fetch pickups for resident
    fun fetchPickups(residentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = firebaseRepository.getPickupsByResident(residentId)
            if (result.isSuccess) {
                _pickups.value = result.getOrNull() ?: emptyList()
                _nextPickup.value = _pickups.value
                    .filter { it.status == "scheduled" || it.status == "confirmed" }
                    .minByOrNull { it.scheduledDate }
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Failed to fetch pickups"
            }
            _isLoading.value = false
        }
    }

    // Confirm a scheduled pickup
    fun confirmPickup(
        pickupId: String,
        residentId: String,
        wasteType: String,
        volume: String,
        notes: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            val confirmation = PickupConfirmationEntity(
                id = UUID.randomUUID().toString(),
                pickupId = pickupId,
                residentId = residentId,
                wasteType = wasteType,
                volume = volume,
                notes = notes,
                status = "confirmed"
            )

            val result = firebaseRepository.confirmPickup(confirmation)

            if (result.isSuccess) {
                _confirmationSuccess.value = true
                _successMessage.value = "Pickup confirmed successfully"
                fetchPickups(residentId)
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Failed to confirm pickup"
            }

            _isLoading.value = false
        }
    }

    // Submit on demand request
    fun submitOnDemandRequest(
        residentId: String,
        zoneId: String,
        wasteType: String,
        volume: String,
        preferredDate: String,
        timeWindow: String,
        notes: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            val request = OnDemandRequestEntity(
                id = UUID.randomUUID().toString(),
                residentId = residentId,
                zoneId = zoneId,
                wasteType = wasteType,
                volume = volume,
                preferredDate = preferredDate,
                timeWindow = timeWindow,
                notes = notes,
                status = "pending"
            )

            val result = firebaseRepository.submitOnDemandRequest(request)

            if (result.isSuccess) {
                _requestSuccess.value = true
                _successMessage.value = "Request submitted successfully"
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Failed to submit request"
            }

            _isLoading.value = false
        }
    }

    // Submit complaint
    fun submitComplaint(
        residentId: String,
        pickupId: String,
        zoneId: String,
        issueType: String,
        description: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            val complaint = ComplaintEntity(
                id = UUID.randomUUID().toString(),
                residentId = residentId,
                pickupId = pickupId,
                zoneId = zoneId,
                issueType = issueType,
                description = description,
                status = "pending"
            )

            val result = firebaseRepository.submitComplaint(complaint)

            if (result.isSuccess) {
                _complaintSuccess.value = true
                _successMessage.value = "Complaint submitted successfully"
                fetchComplaints(residentId)
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Failed to submit complaint"
            }

            _isLoading.value = false
        }
    }

    // Fetch complaints for resident
    fun fetchComplaints(residentId: String) {
        viewModelScope.launch {
            val result = firebaseRepository.getComplaintsByResident(residentId)
            if (result.isSuccess) {
                _complaints.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    // Fetch notifications for resident
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
        _confirmationSuccess.value = false
        _requestSuccess.value = false
        _complaintSuccess.value = false
    }
}

