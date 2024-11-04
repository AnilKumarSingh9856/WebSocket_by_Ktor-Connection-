package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlin.time.Duration.Companion.seconds

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = 15.seconds // Use Kotlin's Duration
        timeout = 15.seconds     // Use Kotlin's Duration
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}
