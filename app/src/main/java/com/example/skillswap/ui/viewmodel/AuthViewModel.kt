package com.example.skillswap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillswap.data.model.LoginRequest
import com.example.skillswap.data.model.MeUser
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
    val hasProfile: Boolean = false,
    val meUser: MeUser? = null,
    val errorMessage: String? = null
)

class AuthViewModel(
    private val authApiService: AuthApiService
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // onSuccess now passes BOTH hasProfile AND the token directly
    // so NavGraph doesn't have to read from authState (which may not have recomposed yet)
    fun login(
        email: String,
        password: String,
        onSuccess: (hasProfile: Boolean, token: String, meUser: MeUser?) -> Unit
    ) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = authApiService.login(LoginRequest(email, password))
                if (response.success && response.token != null) {
                    val meResponse = try {
                        authApiService.getMe("Bearer ${response.token}")
                    } catch (e: Exception) { null }

                    val hasProfile = meResponse?.user?.headlineRole?.isNotBlank() == true

                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        token = response.token,
                        userId = response.user?.userId,
                        hasProfile = hasProfile,
                        meUser = meResponse?.user
                    )
                    // Pass token directly — don't rely on Compose state recomposition
                    onSuccess(hasProfile, response.token, meResponse?.user)
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