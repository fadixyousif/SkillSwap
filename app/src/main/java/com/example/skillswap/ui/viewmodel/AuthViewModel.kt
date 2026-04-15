package com.example.skillswap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillswap.data.model.LoginRequest
import com.example.skillswap.data.model.SignupRequest
import com.example.skillswap.data.remote.AuthApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val token: String? = null,
    val userId: Int? = null,
    val errorMessage: String? = null
)

class AuthViewModel(
    private val authApiService: AuthApiService
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, password: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = authApiService.login(LoginRequest(email, password))
                if (response.success && response.token != null) {
                    val token = response.token
                    
                    // Call /auth/me to check for profile
                    val meResponse = authApiService.getMe("Bearer $token")
                    
                    if (meResponse.success && meResponse.user != null) {
                        val hasProfile = !meResponse.user.headlineRole.isNullOrBlank()
                        
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            token = token,
                            userId = meResponse.user.userId
                        )
                        onSuccess(hasProfile)
                    } else {
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            errorMessage = "Failed to fetch user profile."
                        )
                    }
                } else {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        errorMessage = response.error ?: "Login failed. Check your credentials."
                    )
                }
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error. Is the server running?"
                )
            }
        }
    }

    fun signup(fullName: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = authApiService.signup(SignupRequest(email, password, fullName))
                if (response.success) {
                    _authState.value = _authState.value.copy(isLoading = false, errorMessage = null)
                    onSuccess()
                } else {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        errorMessage = response.error ?: "Signup failed."
                    )
                }
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error. Is the server running?"
                )
            }
        }
    }

    fun clearError() {
        _authState.value = _authState.value.copy(errorMessage = null)
    }
}