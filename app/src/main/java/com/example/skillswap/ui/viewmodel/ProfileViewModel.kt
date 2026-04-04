package com.example.skillswap.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    fun saveBasicProfile(
        name: String,
        role: String,
        program: String,
        college: String,
        location: String,
        bio: String
    ) {
        _profile.value = _profile.value.copy(
            name = name,
            role = role,
            program = program,
            college = college,
            location = location,
            bio = bio
        )
    }

    fun saveSkills(
        offeredSkills: List<String>,
        neededSkills: List<String>
    ) {
        _profile.value = _profile.value.copy(
            offeredSkills = offeredSkills,
            neededSkills = neededSkills
        )
    }
}