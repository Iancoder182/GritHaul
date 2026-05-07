package com.ian.grithaul.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ian.grithaul.data.local.entities.UserEntity
import com.ian.grithaul.data.remote.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    // UI States
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _userRole = MutableStateFlow("none")
    val userRole: StateFlow<String> = _userRole

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    // Check if user is already logged in
    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            _isLoggedIn.value = authRepository.isUserLoggedIn()
            if (_isLoggedIn.value) {
                fetchCurrentUser()
            }
        }
    }

    // Register new resident
    fun registerUser(
        fullName: String,
        email: String,
        phone: String,
        zone: String,
        password: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            val result = authRepository.registerUser(
                fullName, email, phone, zone, password
            )

            if (result.isSuccess) {
                _currentUser.value = result.getOrNull()
                _registerSuccess.value = true
                _isLoggedIn.value = true
                _userRole.value = "resident"
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Registration failed"
            }

            _isLoading.value = false
        }
    }

    // Login existing user
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            val result = authRepository.loginUser(email, password)

            if (result.isSuccess) {
                _currentUser.value = result.getOrNull()
                _userRole.value = _currentUser.value?.role ?: "none"
                _loginSuccess.value = true
                _isLoggedIn.value = true
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Login failed"
            }

            _isLoading.value = false
        }
    }

    // Fetch current user profile
    private fun fetchCurrentUser() {
        viewModelScope.launch {
            val result = authRepository.getCurrentUser()
            if (result.isSuccess) {
                _currentUser.value = result.getOrNull()
                _userRole.value = _currentUser.value?.role ?: "none"
            }
        }
    }

    // Logout
    fun logoutUser() {
        authRepository.logoutUser()
        _currentUser.value = null
        _isLoggedIn.value = false
        _userRole.value = "none"
        _loginSuccess.value = false
        _registerSuccess.value = false
    }

    // Clear error message
    fun clearError() {
        _errorMessage.value = ""
    }

    // Reset login success
    fun resetLoginSuccess() {
        _loginSuccess.value = false
    }

    // Reset register success
    fun resetRegisterSuccess() {
        _registerSuccess.value = false
    }
}

