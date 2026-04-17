package com.example.skillswap.data.model

// The backend returns flat fields: authorName, authorRole, userId
// We map them into a nested author object using a custom deserializer approach
// Actually simplest fix: match the backend's flat structure exactly

data class PostDto(
    val postId: Int = 0,
    val id: Int = 0,               // fallback
    val userId: Int = 0,
    val content: String = "",
    val offeredSkill: String = "",
    val neededSkill: String = "",
    val createdAt: String? = null,
    val authorName: String = "",   // backend returns this flat
    val authorRole: String? = null, // backend returns this flat
    val profileImageUri: String? = null
) {
    // Helper to get the real id (backend uses postId)
    val realId: Int get() = if (postId != 0) postId else id

    // Build a fake author object for UI compatibility
    val author: PostAuthorDto get() = PostAuthorDto(
        id = userId,
        fullName = authorName,
        role = authorRole
    )
}

data class PostAuthorDto(
    val id: Int,
    val fullName: String,
    val role: String?
)

data class CreatePostRequest(
    val content: String,
    val offeredSkill: String,
    val neededSkill: String
)

data class PostsResponse(
    val success: Boolean,
    val posts: List<PostDto> = emptyList()
)

data class CreatePostResponse(
    val success: Boolean,
    val message: String? = null,
    val postId: Int? = null,
    val data: PostDto? = null
)

data class DeletePostResponse(
    val success: Boolean,
    val message: String? = null
)

data class ApiErrorResponse(
    val success: Boolean,
    val error: String
)