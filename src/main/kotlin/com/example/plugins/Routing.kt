package com.example.plugins
import com.example.room.RoomController
import com.example.routes.chatSocket
import com.example.routes.getAllMessages
import io.ktor.server.application.*
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject


fun Application.configureRouting() {
   val roomController by inject<RoomController>()

    routing {
        chatSocket(roomController)
        getAllMessages(roomController)
    }
}

