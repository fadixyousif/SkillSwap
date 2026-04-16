package com.example.skillswap.data.model

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(
    val success: Boolean,
    val token: String? = null,
    val user: LoggedInUser? = null,
    val error: String? = null
)

data class LoggedInUser(val userId: Int, val email: String, val fullName: String)

data class SignupRequest(val email: String, val password: String, val fullName: String)

data class SignupResponse(
    val success: Boolean,
    val userId: Int? = null,
    val token: String? = null,
    val error: String? = null
)

// For GET /auth/me — tells us if user already has a profile set up
data class MeResponse(
    val success: Boolean,
    val user: MeUser? = null
)

data class MeUser(
    val userId: Int,
    val email: String,
    val fullName: String,
    val headlineRole: String? = null,  // null = no profile yet
    val program: String? = null,
    val college: String? = null,
    val location: String? = null,
    val bio: String? = null
)

data class ProfilesResponse(
    val success: Boolean,
    val profiles: List<ProfileDto> = emptyList()
)

data class ProfileDto(
    val userId: Int,
    val fullName: String,
    val headlineRole: String? = null,
    val program: String? = null,
    val college: String? = null,
    val location: String? = null,
    val bio: String? = null,
    val skills: List<SkillDto> = emptyList()
)

data class SkillDto(
    val skillId: Int? = null,
    val skillName: String,
    val level: String? = null,
    val type: String
)

// --- Save Profile ---
data class SaveProfileRequest(
    val headlineRole: String,
    val program: String,
    val college: String,
    val location: String,
    val bio: String
)

data class SaveProfileResponse(
    val success: Boolean,
    val message: String? = null,
    val error: String? = null
)

// --- Save Skills ---
data class SkillRequest(
    val skillName: String,
    val type: String  // "OFFER" or "NEED"
)

data class SaveSkillsRequest(
    val skills: List<SkillRequest>
)