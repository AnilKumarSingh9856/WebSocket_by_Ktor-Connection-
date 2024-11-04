package com.example.sessions

import kotlinx.serialization.Serializable

@Serializable
data class ChatSession(
    val username: String,
    val sessionId: String
)
