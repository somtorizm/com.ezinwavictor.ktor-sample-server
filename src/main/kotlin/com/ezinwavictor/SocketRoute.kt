package com.ezinwavictor

import com.ezinwavictor.models.MessageServer
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.socket(game: MessageServer) {
   route("/play") {
       webSocket {
          val player = game.connectClient(this) ?: return@webSocket
           try {
              incoming.consumeEach { frame ->  
                  if (frame is Frame.Text) {

                  }
              }
           } catch (e: Exception) {
               e.printStackTrace()
           } finally {
              game.disconnectClient(player)
           }
       }
   }
}
