package com.ezinwavictor

import com.ezinwavictor.models.MessageServer
import com.ezinwavictor.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val game = MessageServer()
    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureRouting(game)
}
