package com.tristansmp.elytra.routes

import com.tristansmp.elytra.Elytra
import com.tristansmp.elytra.lib.SerializeUtils
import com.tristansmp.elytra.lib.getName
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.ShulkerBox
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta
import java.util.*


data class InventoryPOST(
    val b64: String
)

data class LocationPOST(
    val world: String?,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
)

data class ChatPOST(
    val message: String
)

fun Player.toJson(): Map<String, Any?> {
    return mapOf(
        "name" to name,
        "uuid" to uniqueId.toString(),
        "op" to isOp,
        "banned" to isBanned,
        "whitelisted" to isWhitelisted,
        "ip" to address?.address?.hostAddress,
        "location" to mapOf(
            "world" to location.world?.name,
            "x" to location.x,
            "y" to location.y,
            "z" to location.z,
            "yaw" to location.yaw,
            "pitch" to location.pitch
        ),
        "inventory" to mapOf(
            "contents" to inventory.contents.map { it?.toJson() },
            "armorContents" to inventory.armorContents.map { it?.toJson() }
        ),
        "permissions" to effectivePermissions.map { it.permission }
    )
}

fun ItemStack.toJson(): Map<String, Any?> {

    var itemLore: List<String> = listOf()

    if (itemMeta.hasLore()) {
        itemLore = itemMeta.lore()!!.map {
            LegacyComponentSerializer.legacyAmpersand().serialize(it)
        }
    }

    var itemsInside = listOf<Map<String, Any?>?>()

    if (type == Material.SHULKER_BOX) {
        val box = (itemMeta as BlockStateMeta).blockState as ShulkerBox

        itemsInside = box.inventory.contents.map { it?.toJson() }
    }

    return mapOf(
        "name" to itemMeta.getName(),
        "id" to type.name,
        "type" to if (type.isBlock) "block" else "item",
        "amount" to amount,
        "durability" to durability,
        "enchantments" to enchantments.map { it.key.name to it.value }.toMap(),
        "b64" to SerializeUtils.itemStackToBase64(this),
        "lore" to itemLore,
        "itemsInside" to itemsInside
    )

}

fun Route.Player() {
    route("/players") {
        var player: Player? = null

        get("/{target}") {
            call.respond(player!!.toJson())
        }

        get {
            call.respond(Bukkit.getOnlinePlayers().map { it.toJson() })
        }

        post("/{target}/inventory") {
            val req = call.receive<InventoryPOST>()

            val item = SerializeUtils.itemStackFromBase64(req.b64)

            player!!.inventory.addItem(item)

            call.respond(player!!.toJson())
        }

        delete("/{target}/inventory/{slot}") {
            val slot = call.parameters["slot"]!!.toInt()

            player!!.inventory.setItem(slot, null)

            call.respond(player!!.toJson())
        }

        post("/{target}/location") {
            val req = call.receive<LocationPOST>()

            val world = req.world?.let { Bukkit.getWorld(it) } ?: player!!.world

            Bukkit.getScheduler().callSyncMethod(Bukkit.getPluginManager().getPlugin("elytra")!!) {
                player!!.teleport(
                    org.bukkit.Location(
                        world,
                        req.x,
                        req.y,
                        req.z,
                        req.yaw,
                        req.pitch
                    )
                )
            }

            call.respond(player!!.toJson())
        }

        post("/{target}/chat") {
            val req = call.receive<ChatPOST>()

            player!!.sendMessage(req.message)

            call.respond(player!!.toJson())
        }

        post("/{target}/chat/collector") {
            val existing = Elytra.instance.mstore.get<Boolean>("cc:${player!!.uniqueId}:needs_collection")

            if (existing != null && existing) {
                call.respond(mapOf("status" to "collector-exists"))
                return@post
            }

            Elytra.instance.mstore.set("cc:${player!!.uniqueId}:needs_collection", true)

            call.respond(mapOf("status" to "collector-created"))
        }

        get("/{target}/chat/collector") {
            val status = Elytra.instance.mstore.get<Boolean>("cc:${player!!.uniqueId}:needs_collection")
            val result = Elytra.instance.mstore.get<String>("cc:${player!!.uniqueId}:results")

            when {
                result != null -> {
                    Elytra.instance.mstore.remove("cc:${player!!.uniqueId}:results")
                    call.respond(mapOf("status" to "collected", "result" to result))
                }

                status == null -> call.respond(mapOf("status" to "no-collector"))
                status -> call.respond(mapOf("status" to "waiting"))
            }
        }

        intercept(Plugins) {
            val target = call.parameters["target"] ?: return@intercept proceed()

            var uuid: UUID? = null

            try {
                uuid = UUID.fromString(target)
            } catch (e: IllegalArgumentException) {
            }

            player = if (uuid != null) {
                Bukkit.getPlayer(uuid)
            } else {
                Bukkit.getPlayer(target)
            }

            if (player == null) {
                call.response.status(HttpStatusCode.NotFound)
                call.respond(player?.toJson() ?: mapOf("error" to "Player not found"))
                return@intercept finish()
            }

            return@intercept proceed()
        }
    }
}

