package com.example.skillswap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillswap.data.model.ProfileDto
import com.example.skillswap.data.remote.AuthApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DiscoverState(
    val isLoading: Boolean = false,
    val profiles: List<ProfileDto> = emptyList(),
    val errorMessage: String? = null
)

class DiscoverViewModel(
    private val authApiService: AuthApiService
) : ViewModel() {

    private val _discoverState = MutableStateFlow(DiscoverState())
    val discoverState: StateFlow<DiscoverState> = _discoverState.asStateFlow()

    fun loadProfiles(token: String) {
        viewModelScope.launch {
            _discoverState.value = _discoverState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = authApiService.getProfiles("Bearer $token")
                if (response.success) {
                    _discoverState.value = _discoverState.value.copy(
                        isLoading = false,
                        profiles = response.profiles
                    )
                } else {
                    _discoverState.value = _discoverState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to load profiles"
                    )
                }
            } catch (e: Exception) {
                _discoverState.value = _discoverState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error: ${e.message}"
                )
            }
        }
    }
}