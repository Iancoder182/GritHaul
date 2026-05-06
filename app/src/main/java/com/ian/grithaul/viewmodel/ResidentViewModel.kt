package com.ian.grithaul.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ian.grithaul.data.GritRepository
import com.ian.grithaul.data.local.entities.OnDemandRequestEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ResidentViewModel(private val repository: GritRepository) : ViewModel() {

    // Expose the list of requests to the UI as a StateFlow
    val requestHistory = repository.getAllRequests()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun submitPickupRequest(address: String, wasteType: String) {
        viewModelScope.launch {
            val newRequest = OnDemandRequestEntity(
                address = address,
                wasteType = wasteType,
                status = "Pending",
                timestamp = System.currentTimeMillis()
            )
            repository.insertRequest(newRequest)
        }
    }
}


