package com.tristansmp.elytra.commands

import com.tristansmp.elytra.Elytra
import khttp.post
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandLink : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player || Elytra.instance.config.config.token == null || Elytra.instance.config.config.linkAccountEndpoint == null) {
            return false
        }

        // make sure we have a code
        if (args == null || args.isEmpty()) {
            return false
        }

        val player = sender
        val uuid = player.uniqueId.toString()
        val code = args[0]
        val token = Elytra.instance.config.config.token ?: return false
        val endpoint = Elytra.instance.config.config.linkAccountEndpoint ?: return false

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

            // check the response code
            if (response.statusCode == 200) {
                player.sendMessage("§a§l[§b§lElytra§a§l] §a§lSuccessfully linked account!")
            } else {
                player.sendMessage("§a§l[§b§lElytra§a§l] §c§lFailed to link account!")
            }
        } catch (e: Exception) {
            player.sendMessage("§a§l[§b§lElytra§a§l] §c§lFailed to link account!")
        }

        return true
    }
}