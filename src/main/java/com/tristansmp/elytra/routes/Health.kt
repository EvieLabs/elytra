package com.tristansmp.elytra.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

data class HealthResponse(val status: String)

fun Route.Health() {
    route("/health") {
        get {
            call.respond(HealthResponse("ok"))
        }
    }
}