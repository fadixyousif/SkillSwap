package com.example.skillswap.data.remote

import com.example.skillswap.data.model.CreatePostRequest
import com.example.skillswap.data.model.CreatePostResponse
import com.example.skillswap.data.model.DeletePostResponse
import com.example.skillswap.data.model.PostsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PostsApiService {

    @GET("posts")
    suspend fun getPosts(): PostsResponse

    @POST("posts/create")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body request: CreatePostRequest
    ): CreatePostResponse

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Header("Authorization") token: String,
        @Path("id") postId: Int
    ): DeletePostResponse
}