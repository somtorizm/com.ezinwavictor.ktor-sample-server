package com.ezinwavictor

import com.ezinwavictor.models.MakeTurn
import com.ezinwavictor.models.TicTakGame
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json

fun Route.socket(game: TicTakGame) {
   route("/play") {
       webSocket {
          val player = game.connectPlayer(this) ?: return@webSocket

           try {
              incoming.consumeEach { frame ->  
                  if (frame is Frame.Text) {
                      val action = extractAction(frame.readText())
                      game.finishTurn(player, action.x, action.y)
                  }
              }
           } catch (e: Exception) {
               e.printStackTrace()
           } finally {
              game.disconnectPlayer(player)
           }
       }
   }
}

private fun extractAction(message: String): MakeTurn {
  //make_turn#{...}
    val type = message.substringBefore("#")
    val body = message.substringAfter("#")

    return  if (type == "make_turn") {
        Json.decodeFromString(body)
    } else MakeTurn(-1, -1)
}