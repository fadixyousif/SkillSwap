package com.example.skillswap.di

import com.example.skillswap.data.remote.AuthApiService
import com.example.skillswap.data.remote.PostsApiService
import com.example.skillswap.data.remote.RequestsApiService
import com.example.skillswap.data.repository.OfflineSkillSwapRepository
import com.example.skillswap.data.repository.SkillSwapRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppDataContainer : AppContainer {

    // Use 10.0.2.2 for Android Emulator (maps to your PC's localhost)
    // If using a real device, replace with your PC's local IP e.g. "http://192.168.1.x:5000/"
    private val baseUrl = "http://10.0.2.2:5000/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    override val postsApiService: PostsApiService by lazy {
        retrofit.create(PostsApiService::class.java)
    }

    override val requestsApiService: RequestsApiService by lazy {
        retrofit.create(RequestsApiService::class.java)
    }

    override val skillSwapRepository: SkillSwapRepository by lazy {
        OfflineSkillSwapRepository(
            postsApiService = postsApiService,
            requestsApiService = requestsApiService
        )
    }
}