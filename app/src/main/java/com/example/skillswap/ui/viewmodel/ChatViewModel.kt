package com.example.skillswap.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ChatMessage(
    val id: Int,
    val text: String,
    val isFromMe: Boolean,
    val time: String
)

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow(
        listOf(
            ChatMessage(
                id = 1,
                text = "Hey, I saw your profile and I’d be interested in exchanging frontend help for design support.",
                isFromMe = false,
                time = "2:10 PM"
            ),
            ChatMessage(
                id = 2,
                text = "That sounds good. I can help with design and branding.",
                isFromMe = true,
                time = "2:12 PM"
            ),
            ChatMessage(
                id = 3,
                text = "Perfect. I’m mainly looking for UI feedback and a logo refresh.",
                isFromMe = false,
                time = "2:14 PM"
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val updated = _messages.value.toMutableList()
        updated.add(
            ChatMessage(
                id = updated.size + 1,
                text = text.trim(),
                isFromMe = true,
                time = "Now"
            )
        )
        _messages.value = updated
    }
}