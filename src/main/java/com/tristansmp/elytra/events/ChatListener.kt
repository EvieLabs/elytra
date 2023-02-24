package com.tristansmp.elytra.events

import com.tristansmp.elytra.Elytra
import io.papermc.paper.event.player.AsyncChatEvent
import khttp.post
import net.kyori.adventure.text.TextComponent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class ChatListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerChat(event: AsyncChatEvent) {
        try {
            val message = event.message() as TextComponent


            if (!message.content().startsWith("~")) {
                return
            }


            if (message.content()
                    .startsWith("~link") && Elytra.instance.config.config.token != null && Elytra.instance.config.config.linkAccountEndpoint != null
            ) {
                val player = event.player
                val uuid = player.uniqueId.toString()
                val code = message.content().split(" ")[1]
                val token = Elytra.instance.config.config.token ?: return
                val endpoint = Elytra.instance.config.config.linkAccountEndpoint ?: return

                event.isCancelled = true

                player.sendMessage("§a§l[§b§lElytra§a§l] §a§lLinking account...")

                try {
                    val response = post(
                        url = endpoint,
                        data = mapOf(
                            "uuid" to uuid,
                            "code" to code
                        ),
                        headers = mapOf(
                            "Authorization" to token
                        )
                    )

                    if (response.statusCode == 200) {
                        player.sendMessage("§a§l[§b§lElytra§a§l] §a§lSuccessfully linked account!")
                    } else {
                        player.sendMessage("§a§l[§b§lElytra§a§l] §c§lFailed to link account!")
                    }
                } catch (e: Exception) {
                    player.sendMessage("§a§l[§b§lElytra§a§l] §c§lFailed to link account!")
                }

                return;

            }

            val content = message.content().substring(1)

            val needsCollection = Elytra.instance.mstore.get<Boolean>("cc:${event.player.uniqueId}:needs_collection")

            if (needsCollection == null || !needsCollection) {
                return
            }

            event.isCancelled = true

            Elytra.instance.mstore.set("cc:${event.player.uniqueId}:results", content)

            event.player.sendMessage("§d[Elytra] §aMessage collected.")

            Elytra.instance.mstore.remove("cc:${event.player.uniqueId}:needs_collection")
        } catch (e: ClassCastException) {
            event.player.sendMessage("§d[Elytra] §cMessage could not be collected.")
        }
    }
}
