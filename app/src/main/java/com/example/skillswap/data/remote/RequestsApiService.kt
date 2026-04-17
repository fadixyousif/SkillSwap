package com.example.skillswap.data.remote

import com.example.skillswap.data.model.IncomingRequestsResponse
import com.example.skillswap.data.model.OutgoingRequestsResponse
import com.example.skillswap.data.model.RequestActionResponse
import com.example.skillswap.data.model.SendSwapRequest
import com.example.skillswap.data.model.SwapResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RequestsApiService {

    @POST("requests/send")
    suspend fun sendSwapRequest(
        @Header("Authorization") token: String,
        @Body request: SendSwapRequest
    ): SwapResponse

    @GET("requests/incoming")
    suspend fun getIncomingRequests(
        @Header("Authorization") token: String
    ): IncomingRequestsResponse

    @GET("requests/outgoing")
    suspend fun getOutgoingRequests(
        @Header("Authorization") token: String
    ): OutgoingRequestsResponse

    @PATCH("requests/{requestId}/accept")
    suspend fun acceptRequest(
        @Header("Authorization") token: String,
        @Path("requestId") requestId: Int
    ): RequestActionResponse

    @PATCH("requests/{requestId}/decline")
    suspend fun declineRequest(
        @Header("Authorization") token: String,
        @Path("requestId") requestId: Int
    ): RequestActionResponse
}