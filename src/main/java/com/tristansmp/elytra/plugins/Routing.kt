package com.tristansmp.elytra.plugins

import com.tristansmp.elytra.Elytra
import com.tristansmp.elytra.routes.Health
import com.tristansmp.elytra.routes.Player
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    routing {
        intercept(Plugins) {
            if (Elytra.instance.config.config.token != null && Elytra.instance.config.config.token != call.request.headers["Authorization"]) {
                call.respond(HttpStatusCode.Unauthorized)
                return@intercept finish()
            }

            return@intercept proceed()
        }

        get("/") {
            call.respondRedirect("https://github.com/tristansmp/elytra")
        }

        Health()
        Player()
    }
}
