package com.example.skillswap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillswap.data.model.SaveProfileRequest
import com.example.skillswap.data.model.SaveSkillsRequest
import com.example.skillswap.data.model.SkillRequest
import com.example.skillswap.data.remote.AuthApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UserProfile(
    val name: String = "",
    val role: String = "",
    val program: String = "",
    val college: String = "",
    val location: String = "",
    val bio: String = "",
    val offeredSkills: List<String> = emptyList(),
    val neededSkills: List<String> = emptyList()
)

class ProfileViewModel : ViewModel() {

    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    // Store auth token so we can save to backend
    private var authToken: String = ""
    private var authApiService: AuthApiService? = null

    fun setAuth(token: String, apiService: AuthApiService) {
        authToken = token
        authApiService = apiService
    }

    fun saveBasicProfile(
        name: String,
        role: String,
        program: String,
        college: String,
        location: String,
        bio: String
    ) {
        _profile.value = _profile.value.copy(
            name = name, role = role, program = program,
            college = college, location = location, bio = bio
        )

        // Save to backend if we have a token
        if (authToken.isNotEmpty() && authApiService != null) {
            viewModelScope.launch {
                try {
                    authApiService!!.saveProfile(
                        token = "Bearer $authToken",
                        request = SaveProfileRequest(
                            headlineRole = role,
                            program = program,
                            college = college,
                            location = location,
                            bio = bio
                        )
                    )
                } catch (e: Exception) {
                    // silently fail — profile is still saved locally
                }
            }
        }
    }

    fun saveSkills(offeredSkills: List<String>, neededSkills: List<String>) {
        _profile.value = _profile.value.copy(
            offeredSkills = offeredSkills,
            neededSkills = neededSkills
        )

        // Save to backend if we have a token
        if (authToken.isNotEmpty() && authApiService != null) {
            viewModelScope.launch {
                try {
                    val skillsList = offeredSkills.map { SkillRequest(it, "OFFER") } +
                            neededSkills.map { SkillRequest(it, "NEED") }
                    authApiService!!.saveSkills(
                        token = "Bearer $authToken",
                        request = SaveSkillsRequest(skills = skillsList)
                    )
                } catch (e: Exception) {
                    // silently fail — skills are still saved locally
                }
            }
        }
    }

    fun loadFromBackend(
        fullName: String,
        headlineRole: String?,
        program: String?,
        college: String?,
        location: String?,
        bio: String?,
        offeredSkills: List<String>,
        neededSkills: List<String>
    ) {
        _profile.value = UserProfile(
            name = fullName,
            role = headlineRole ?: "",
            program = program ?: "",
            college = college ?: "",
            location = location ?: "",
            bio = bio ?: "",
            offeredSkills = offeredSkills,
            neededSkills = neededSkills
        )
    }
}