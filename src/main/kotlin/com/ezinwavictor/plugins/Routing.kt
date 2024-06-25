package com.ezinwavictor.plugins

import com.ezinwavictor.models.TicTakGame
import com.ezinwavictor.socket
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(game: TicTakGame) {
    routing {
        socket(game)
    }
}
