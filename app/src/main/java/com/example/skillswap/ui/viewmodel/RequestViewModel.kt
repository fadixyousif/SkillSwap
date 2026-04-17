package com.example.skillswap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillswap.data.model.IncomingRequestDto
import com.example.skillswap.data.model.OutgoingRequestDto
import com.example.skillswap.data.model.SendSwapRequest
import com.example.skillswap.data.repository.SkillSwapRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RequestActionState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val incomingRequests: List<IncomingRequestDto> = emptyList(),
    val outgoingRequests: List<OutgoingRequestDto> = emptyList()  // ← added
)

class RequestViewModel(
    private val repository: SkillSwapRepository
) : ViewModel() {

    private val _requestState = MutableStateFlow(RequestActionState())
    val requestState: StateFlow<RequestActionState> = _requestState.asStateFlow()

    fun sendSwapRequest(token: String, toUserId: Int, offeredSkill: String, requestedSkill: String) {
        viewModelScope.launch {
            _requestState.value = _requestState.value.copy(
                isLoading = true, successMessage = null, errorMessage = null)
            try {
                repository.sendSwapRequest(
                    token = token,
                    request = SendSwapRequest(
                        toUserId = toUserId,
                        offeredSkill = offeredSkill,
                        requestedSkill = requestedSkill
                    )
                )
                _requestState.value = _requestState.value.copy(
                    isLoading = false,
                    successMessage = "Swap request sent successfully!")
            } catch (e: Exception) {
                _requestState.value = _requestState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to send swap request.")
            }
        }
    }

    fun loadIncomingRequests(token: String) {
        viewModelScope.launch {
            _requestState.value = _requestState.value.copy(isLoading = true, errorMessage = null)
            try {
                val requests = repository.getIncomingRequests(token)
                _requestState.value = _requestState.value.copy(
                    isLoading = false, incomingRequests = requests)
            } catch (e: Exception) {
                _requestState.value = _requestState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load requests.")
            }
        }
    }

    fun loadOutgoingRequests(token: String) {
        viewModelScope.launch {
            try {
                val requests = repository.getOutgoingRequests(token)
                _requestState.value = _requestState.value.copy(outgoingRequests = requests)
            } catch (e: Exception) {
                // silently fail for outgoing — don't override incoming error
            }
        }
    }

    fun acceptRequest(token: String, requestId: Int) {
        viewModelScope.launch {
            try {
                repository.acceptRequest(token, requestId)
                loadIncomingRequests(token)
            } catch (e: Exception) {
                _requestState.value = _requestState.value.copy(
                    errorMessage = e.message ?: "Failed to accept request.")
            }
        }
    }

    fun declineRequest(token: String, requestId: Int) {
        viewModelScope.launch {
            try {
                repository.declineRequest(token, requestId)
                loadIncomingRequests(token)
            } catch (e: Exception) {
                _requestState.value = _requestState.value.copy(
                    errorMessage = e.message ?: "Failed to decline request.")
            }
        }
    }

    fun clearMessages() {
        _requestState.value = _requestState.value.copy(successMessage = null, errorMessage = null)
    }
}