package com.example.skillswap.data.repository

import com.example.skillswap.data.model.CreatePostRequest
import com.example.skillswap.data.model.IncomingRequestDto
import com.example.skillswap.data.model.OutgoingRequestDto
import com.example.skillswap.data.model.PostDto
import com.example.skillswap.data.model.SendSwapRequest
import com.example.skillswap.data.remote.PostsApiService
import com.example.skillswap.data.remote.RequestsApiService

class OfflineSkillSwapRepository(
    private val postsApiService: PostsApiService,
    private val requestsApiService: RequestsApiService
) : SkillSwapRepository {

    override suspend fun getPosts(): List<PostDto> {
        return postsApiService.getPosts().posts
    }

    override suspend fun createPost(
        token: String,
        request: CreatePostRequest
    ): PostDto {
        val response = postsApiService.createPost(
            token = "Bearer $token",
            request = request
        )

        return requireNotNull(response.data) {
            "Create post response data was null."
        }
    }

    override suspend fun deletePost(
        token: String,
        postId: Int
    ) {
        postsApiService.deletePost(
            token = "Bearer $token",
            postId = postId
        )
    }

    override suspend fun sendSwapRequest(
        token: String,
        request: SendSwapRequest
    ) {
        requestsApiService.sendSwapRequest(
            token = "Bearer $token",
            request = request
        )
    }

    override suspend fun getIncomingRequests(
        token: String
    ): List<IncomingRequestDto> {
        return requestsApiService.getIncomingRequests(
            token = "Bearer $token"
        ).requests
    }

    override suspend fun getOutgoingRequests(
        token: String
    ): List<OutgoingRequestDto> {
        return requestsApiService.getOutgoingRequests(
            token = "Bearer $token"
        ).requests
    }

    override suspend fun acceptRequest(
        token: String,
        requestId: Int
    ) {
        requestsApiService.acceptRequest(
            token = "Bearer $token",
            requestId = requestId
        )
    }

    override suspend fun declineRequest(
        token: String,
        requestId: Int
    ) {
        requestsApiService.declineRequest(
            token = "Bearer $token",
            requestId = requestId
        )
    }
}