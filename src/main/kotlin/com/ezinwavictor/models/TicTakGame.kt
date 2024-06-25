package com.ezinwavictor.models

import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class TicTakGame {

    private val state = MutableStateFlow(AppState())

    private val playerSockets = ConcurrentHashMap<Char, WebSocketSession>()

    private val gameScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var delayGameJob: Job? = null

    init {
        state.onEach(::broadcast).launchIn(gameScope)
    }

    fun connectPlayer(session: WebSocketSession): Char? {
        val isPlayerX = state.value.connectedPlayer.any{ it == 'X'}
        val player = if (isPlayerX) 'O' else 'X'

        state.update {
            if (state.value.connectedPlayer.contains(player)) {
                return null
            }
            if (!playerSockets.containsKey(player)) {
                playerSockets[player] = session
            }

            it.copy(
                connectedPlayer = it.connectedPlayer + player
            )
        }

        return player
    }

    fun disconnectPlayer(player: Char) {
        playerSockets.remove(player)
        state.update {
            it.copy(
                connectedPlayer = it.connectedPlayer - player
            )
        }
    }

    suspend fun broadcast(state: AppState) {
        playerSockets.values.forEach { socket ->
           socket.send(
               Json.encodeToString(state)
           )
        }
    }

    fun finishTurn(player: Char?, x: Int, y: Int) {
        if (state.value.field[x][y] != null || state.value.winningPlayer != null) {
            return
        }

        if (state.value.playerAtTurn != player) {
           return
        }

        val currentPlayer = state.value.playerAtTurn
        state.update {
            val newField = it.field.also { field ->
                field[y][x] = currentPlayer
            }

            val isBoardFull = newField.all { it.all { it != null } }
            if (isBoardFull) {
                startNewGameRoundDelay()
            }

            it.copy(
                playerAtTurn = if (currentPlayer == 'X') 'O' else 'X',
                field = newField,
                isBoardFull = isBoardFull,
                winningPlayer = getWinningPlayer()?.also {
                    startNewGameRoundDelay()
                }
            )
        }
    }

    private fun getWinningPlayer(): Char? {
      return 'X'
    }

    private fun startNewGameRoundDelay() {
      delayGameJob?.cancel()
        delayGameJob = gameScope.launch {
            delay(5000L)

            state.update {
                it.copy(
                    playerAtTurn = 'X',
                    field = AppState.emptyField(),
                    winningPlayer = null,
                    isBoardFull = false,
                )
            }
        }
    }
}