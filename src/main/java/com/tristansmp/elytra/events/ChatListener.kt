package com.tristansmp.elytra.events

import com.tristansmp.elytra.Elytra
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.TextComponent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener


class ChatListener : Listener {
    @EventHandler
    fun onPlayerChat(event: AsyncChatEvent) {
        val needsCollection = Elytra.instance.mstore.get<Boolean>("cc:${event.player.uniqueId}:needs_collection")

        if (needsCollection == null || !needsCollection) {
            return
        }

        event.isCancelled = true

        try {
            val message = event.message() as TextComponent

            Elytra.instance.mstore.set("cc:${event.player.uniqueId}:results", message.content())

            event.player.sendMessage("§d[Elytra] §aMessage collected.")

            Elytra.instance.mstore.remove("cc:${event.player.uniqueId}:needs_collection")
        } catch (e: ClassCastException) {
            event.player.sendMessage("§d[Elytra] §cMessage could not be collected.")
        }
    }
}
