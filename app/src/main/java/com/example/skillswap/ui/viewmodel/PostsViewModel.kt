package com.example.skillswap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillswap.data.model.CreatePostRequest
import com.example.skillswap.data.model.PostDto
import com.example.skillswap.data.repository.SkillSwapRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PostsUiState(
    val isLoading: Boolean = false,
    val posts: List<PostDto> = emptyList(),
    val errorMessage: String? = null
)

class PostsViewModel(
    private val repository: SkillSwapRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostsUiState())
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val posts = repository.getPosts()
                _uiState.value = PostsUiState(
                    isLoading = false,
                    posts = posts,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = PostsUiState(
                    isLoading = false,
                    posts = emptyList(),
                    errorMessage = e.message ?: "Failed to load posts."
                )
            }
        }
    }

    fun createPost(
        token: String,
        content: String,
        offeredSkill: String,
        neededSkill: String
    ) {
        viewModelScope.launch {
            try {
                repository.createPost(
                    token = token,
                    request = CreatePostRequest(
                        content = content,
                        offeredSkill = offeredSkill,
                        neededSkill = neededSkill
                    )
                )
                loadPosts()
            } catch (_: Exception) {
            }
        }
    }

    fun deletePost(
        token: String,
        postId: Int
    ) {
        viewModelScope.launch {
            try {
                repository.deletePost(
                    token = token,
                    postId = postId
                )
                loadPosts()
            } catch (_: Exception) {
            }
        }
    }
}