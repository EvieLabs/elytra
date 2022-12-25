package com.tristansmp.elytra.lib

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class SerializeUtils {
    companion object {
        fun itemStackToBase64(item: ItemStack): String {
            try {
                val outputStream = ByteArrayOutputStream()
                val dataOutput = BukkitObjectOutputStream(outputStream)
                dataOutput.writeObject(item)
                dataOutput.close()
                return Base64Coder.encodeLines(outputStream.toByteArray())
            } catch (e: Exception) {
                throw IllegalStateException("Unable to save item stack.", e)
            }
        }

        fun itemStackFromBase64(data: String): ItemStack {
            try {
                val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(data))
                val dataInput = BukkitObjectInputStream(inputStream)
                val item = dataInput.readObject() as ItemStack
                dataInput.close()
                return item
            } catch (e: Exception) {
                throw IllegalStateException("Unable to load item stack.", e)
            }
        }
    }
}