package com.ezinwavictor.models

import kotlinx.serialization.Serializable

@Serializable
data class MessageState(
    val messageData: String? = "",
    val connectedUsers: Set<String> = emptySet())
