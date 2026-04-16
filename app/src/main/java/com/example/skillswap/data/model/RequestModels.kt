package com.example.skillswap.data.model

data class SendSwapRequest(
    val toUserId: Int,
    val requestedSkill: String,
    val offeredSkill: String,
    val message: String? = null
)

data class SwapResponse(
    val success: Boolean,
    val message: String,
    val requestId: Int? = null
)

data class IncomingRequestDto(
    val requestId: Int,
    val fromUserId: Int,
    val toUserId: Int,
    val requestedSkill: String,
    val offeredSkill: String,
    val message: String?,
    val status: String,
    val fromUserName: String,
    val profileImageUri: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class OutgoingRequestDto(
    val requestId: Int,
    val fromUserId: Int,
    val toUserId: Int,
    val requestedSkill: String,
    val offeredSkill: String,
    val message: String?,
    val status: String,
    val toUserName: String,
    val profileImageUri: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class IncomingRequestsResponse(
    val success: Boolean,
    val requests: List<IncomingRequestDto> = emptyList()
)

data class OutgoingRequestsResponse(
    val success: Boolean,
    val requests: List<OutgoingRequestDto> = emptyList()
)

data class RequestActionResponse(
    val success: Boolean,
    val message: String
)