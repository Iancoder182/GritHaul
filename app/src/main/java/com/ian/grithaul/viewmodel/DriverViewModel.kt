package com.ian.grithaul.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ian.grithaul.data.GritRepository
import com.ian.grithaul.data.local.entities.PickupEntity
import kotlinx.coroutines.launch

class DriverViewModel(private val repository: GritRepository) : ViewModel() {

    fun markPickupAsCompleted(pickup: PickupEntity) {
        viewModelScope.launch {
            val updated = pickup.copy(status = "Completed")
            repository.updatePickupStatus(updated)
        }
    }
}
