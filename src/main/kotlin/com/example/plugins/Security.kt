package com.example.plugins

import com.example.sessions.ChatSession
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.generateNonce
import kotlinx.serialization.*

fun Application.configureSecurity() {
    install(Sessions){
        cookie<ChatSession>("SESSION")
    }

    intercept(ApplicationCallPipeline.Plugins){
        if (call.sessions.get<ChatSession>() == null){
            val username = call.parameters["username"] ?: "Guest"
            call.sessions.set(ChatSession(username, generateNonce()))
        }
    }

}
