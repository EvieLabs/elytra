package com.tristansmp.elytra

import com.tristansmp.elytra.commands.CommandLink
import com.tristansmp.elytra.events.ChatListener
import com.tristansmp.elytra.lib.ConfigManager
import com.tristansmp.elytra.lib.MemoryStore
import com.tristansmp.elytra.plugins.configureHTTP
import com.tristansmp.elytra.plugins.configureRouting
import com.tristansmp.elytra.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class Elytra : JavaPlugin() {

    companion object {
        lateinit var instance: Elytra
    }

    lateinit var config: ConfigManager
    lateinit var mstore: MemoryStore
    var lp: LuckPerms? = null

    override fun onEnable() {
        Thread {
            embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
                .start(wait = true)
        }.start()

        instance = this

        config = ConfigManager()
        mstore = MemoryStore()

        val provider = Bukkit.getServicesManager().getRegistration(
            LuckPerms::class.java
        )

        if (provider != null) {
            lp = provider.provider
        }

        server.pluginManager.registerEvents(ChatListener(), this)
        this.getCommand("elink")?.setExecutor(CommandLink())
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
