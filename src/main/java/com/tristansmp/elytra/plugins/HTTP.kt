package com.tristansmp.elytra.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.defaultheaders.*
import org.bukkit.Bukkit

fun Application.configureHTTP() {
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
        header("X-Minecraft-Version", Bukkit.getMinecraftVersion())
        header("X-Bukkit-Version", Bukkit.getVersion())
    }

}
