package com.example.skillswap.di

import com.example.skillswap.data.remote.AuthApiService
import com.example.skillswap.data.remote.PostsApiService
import com.example.skillswap.data.remote.RequestsApiService
import com.example.skillswap.data.repository.SkillSwapRepository

interface AppContainer {
    val authApiService: AuthApiService
    val postsApiService: PostsApiService
    val requestsApiService: RequestsApiService
    val skillSwapRepository: SkillSwapRepository
}