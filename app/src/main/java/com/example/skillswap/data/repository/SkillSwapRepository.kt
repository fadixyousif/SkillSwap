package com.example.skillswap.data.repository

import com.example.skillswap.data.model.CreatePostRequest
import com.example.skillswap.data.model.IncomingRequestDto
import com.example.skillswap.data.model.OutgoingRequestDto
import com.example.skillswap.data.model.PostDto
import com.example.skillswap.data.model.SendSwapRequest

interface SkillSwapRepository {

    suspend fun getPosts(): List<PostDto>

    suspend fun createPost(
        token: String,
        request: CreatePostRequest
    ): PostDto

    suspend fun deletePost(
        token: String,
        postId: Int
    )

    suspend fun sendSwapRequest(
        token: String,
        request: SendSwapRequest
    )

    suspend fun getIncomingRequests(
        token: String
    ): List<IncomingRequestDto>

    suspend fun getOutgoingRequests(
        token: String
    ): List<OutgoingRequestDto>

    suspend fun acceptRequest(
        token: String,
        requestId: Int
    )

    suspend fun declineRequest(
        token: String,
        requestId: Int
    )
}