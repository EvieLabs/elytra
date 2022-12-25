package com.tristansmp.elytra

import com.tristansmp.elytra.plugins.configureHTTP
import com.tristansmp.elytra.plugins.configureRouting
import com.tristansmp.elytra.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.bukkit.plugin.java.JavaPlugin

class Elytra : JavaPlugin() {


    override fun onEnable() {
        Thread {
            embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
                .start(wait = true)
        }.start()

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureRouting()
}
