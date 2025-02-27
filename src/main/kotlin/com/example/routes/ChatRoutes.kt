package com.example.routes

import com.example.room.MemberAlreadyExistException
import com.example.room.RoomController
import com.example.sessions.ChatSession
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach

fun Route.chatSocket(roomController: RoomController){
    webSocket("/chat-socket"){
        val session = call.sessions.get<ChatSession>()
        if(session == null){
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
            return@webSocket
        }
        try {
            roomController.onJoin(
                username = session.username,
                sessionId = session.sessionId,
                socket =  this
            )
            incoming.consumeEach { frame ->
                if (frame is Frame.Text){
                    roomController.sendMessage(
                        senderUsername = session.username,
                        message = frame.readText()
                    )
                }
            }
        } catch (e: MemberAlreadyExistException){
            call.respond(HttpStatusCode.Conflict)
        } catch (e: Exception){
            e.printStackTrace()
        }finally {
            roomController.tryDisconnect(session.username)
            close(CloseReason(CloseReason.Codes.NORMAL, "Disconnecting."))
        }
    }
}

fun Route.getAllMessages(roomController: RoomController){
    get("/messages"){
        call.respond(
            HttpStatusCode.OK,
            roomController.getAllMessages()
        )
    }
}