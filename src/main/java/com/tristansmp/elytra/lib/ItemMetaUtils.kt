package com.tristansmp.elytra.lib

import org.bukkit.inventory.meta.ItemMeta

fun ItemMeta.getName(): String? {
    val custom = getDisplayName()

    if (custom != "") {
        return custom
    }

    return null
}