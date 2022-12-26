package com.tristansmp.elytra.lib

import java.util.*

class MemoryStore {
    private val store = mutableMapOf<String, Any>()

    fun <T> get(key: String): T? {
        return store[key] as T?
    }

    fun <T : Any> set(key: String, value: T) {
        store[key] = value
    }

    fun remove(key: String) {
        store.remove(key)
    }

    init {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                store.clear()
            }
        }, 300000, 300000)
    }
}