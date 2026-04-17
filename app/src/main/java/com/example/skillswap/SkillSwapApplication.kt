package com.example.skillswap

import android.app.Application
import com.example.skillswap.di.AppContainer
import com.example.skillswap.di.AppDataContainer

class SkillSwapApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer()
    }
}