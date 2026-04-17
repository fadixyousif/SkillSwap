package com.example.skillswap

import com.example.skillswap.data.model.LoginRequest
import com.example.skillswap.data.model.LoginResponse
import com.example.skillswap.data.model.MeResponse
import com.example.skillswap.data.model.ProfilesResponse
import com.example.skillswap.data.model.SignupRequest
import com.example.skillswap.data.model.SignupResponse
import com.example.skillswap.data.remote.AuthApiService
import com.example.skillswap.ui.viewmodel.AuthViewModel

/**
 * A fake AuthApiService used only in UI tests.
 * Never calls the real network.
 */
class FakeAuthApiService : AuthApiService {
    override suspend fun login(request: LoginRequest): LoginResponse =
        LoginResponse(success = false, error = "Test mode")

    override suspend fun signup(request: SignupRequest): SignupResponse =
        SignupResponse(success = false, error = "Test mode")

    override suspend fun getMe(token: String): MeResponse =
        MeResponse(success = false, user = null)

    override suspend fun getProfiles(token: String): ProfilesResponse =
        ProfilesResponse(success = true, profiles = emptyList())
}

/**
 * AuthViewModel backed by the fake service — safe for UI tests.
 */
fun FakeAuthViewModelForTest() = AuthViewModel(FakeAuthApiService())