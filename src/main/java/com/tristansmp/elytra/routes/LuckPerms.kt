package com.tristansmp.elytra.routes

import com.tristansmp.elytra.Elytra
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.future.await
import net.luckperms.api.node.Node
import java.util.*

data class LuckPermsPermissionsPOST(
    val permission: String,
)

fun Route.LuckPerms() {
    route("/luckperms") {

        delete("/users/{target}/permissions/{permission}") {
            val lp = Elytra.instance.lp

            if (lp == null) {
                call.response.status(HttpStatusCode.FailedDependency)
                call.respond(mapOf("status" to "error", "message" to "LuckPerms not found"))
                return@delete
            }

            val permission = call.parameters["permission"]!!

            val lpUser = lp.userManager.loadUser(UUID.fromString(call.parameters["target"]!!)).await()

            if (lpUser == null) {
                call.response.status(HttpStatusCode.NotFound)
                call.respond(mapOf("status" to "error", "message" to "LuckPerms user not found"))
                return@delete
            }

            lpUser.data().remove(
                Node.builder(permission).build()
            )

            lp.userManager.saveUser(lpUser)

            call.respond(mapOf("status" to "ok"))
        }

        post("/users/{target}/permissions") {
            val req = call.receive<LuckPermsPermissionsPOST>()
            val lp = Elytra.instance.lp

            if (lp == null) {
                call.response.status(HttpStatusCode.FailedDependency)
                call.respond(mapOf("status" to "error", "message" to "LuckPerms not found"))
                return@post
            }

            val lpUser = lp.userManager.loadUser(UUID.fromString(call.parameters["target"]!!)).await()

            if (lpUser == null) {
                call.response.status(HttpStatusCode.NotFound)
                call.respond(mapOf("status" to "error", "message" to "LuckPerms user not found"))
                return@post
            }

            lpUser.data().add(
                Node.builder(req.permission).build()
            )

            lp.userManager.saveUser(lpUser)

            call.respond(mapOf("status" to "ok"))
        }

    }
}

