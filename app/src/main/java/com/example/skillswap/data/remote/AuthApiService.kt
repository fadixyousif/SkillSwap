package com.example.skillswap.data.remote

import com.example.skillswap.data.model.LoginRequest
import com.example.skillswap.data.model.LoginResponse
import com.example.skillswap.data.model.MeResponse
import com.example.skillswap.data.model.ProfilesResponse
import com.example.skillswap.data.model.SaveProfileRequest
import com.example.skillswap.data.model.SaveProfileResponse
import com.example.skillswap.data.model.SaveSkillsRequest
import com.example.skillswap.data.model.SignupRequest
import com.example.skillswap.data.model.SignupResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): SignupResponse

    @GET("auth/me")
    suspend fun getMe(@Header("Authorization") token: String): MeResponse

    @GET("profiles")
    suspend fun getProfiles(@Header("Authorization") token: String): ProfilesResponse

    @POST("profiles/complete")
    suspend fun saveProfile(
        @Header("Authorization") token: String,
        @Body request: SaveProfileRequest
    ): SaveProfileResponse

    @POST("profiles/skills")
    suspend fun saveSkills(
        @Header("Authorization") token: String,
        @Body request: SaveSkillsRequest
    ): SaveProfileResponse
}