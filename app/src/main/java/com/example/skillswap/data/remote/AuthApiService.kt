package com.example.skillswap.data.remote

import com.example.skillswap.data.model.LoginRequest
import com.example.skillswap.data.model.LoginResponse
import com.example.skillswap.data.model.MeResponse
import com.example.skillswap.data.model.ProfilesResponse
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
}