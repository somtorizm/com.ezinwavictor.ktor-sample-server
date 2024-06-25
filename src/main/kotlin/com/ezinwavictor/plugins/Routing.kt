package com.ezinwavictor.plugins

import com.ezinwavictor.models.MessageServer
import com.ezinwavictor.socket
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(game: MessageServer) {
    routing {
        socket(game)
    }
}
