package com.example.skillswap.data.model

data class User(
    val id: Int = 0,
    val name: String,
    val role: String,
    val program: String,
    val college: String,
    val location: String,
    val bio: String,
    val offeredSkills: List<String>,
    val neededSkills: List<String>
)