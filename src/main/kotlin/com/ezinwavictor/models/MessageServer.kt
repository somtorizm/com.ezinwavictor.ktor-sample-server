package com.ezinwavictor.models

import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import java.util.concurrent.ConcurrentHashMap
class MessageServer {

    private val state = MutableStateFlow(MessageState())

    private val playerSockets = ConcurrentHashMap<String, WebSocketSession>()

    private val gameScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        gameScope.launch {
            while (true) {
                updateText()
                broadcast(state.value)
                delay(2000)
            }
        }
    }

    fun connectClient(session: WebSocketSession): String? {
        val client = UUID.randomUUID().toString()

        state.update { currentState ->
            if (currentState.connectedUsers.contains(client)) {
                return null
            }
            if (!playerSockets.containsKey(client)) {
                playerSockets[client] = session
            }

            currentState.copy(
                connectedUsers = currentState.connectedUsers + client
            )
        }

        println("Connected Users: " + state.value.connectedUsers)

        return client
    }

    private suspend fun updateText() {
        state.update { currentState ->
            currentState.copy(
                messageData = "Hello World " + UUID.randomUUID().toString()
            )
        }
    }

    fun disconnectClient(client: String) {
        playerSockets.remove(client)
        state.update { currentState ->
            currentState.copy(
                connectedUsers = currentState.connectedUsers - client
            )
        }
    }

    suspend fun broadcast(state: MessageState) {
        playerSockets.values.forEach { socket ->
            socket.send(Json.encodeToString(state))
        }
    }
}