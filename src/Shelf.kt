package de.joker.shelf

import de.joker.shelf.driver.StorageDriver
import kotlinx.serialization.json.Json

class Shelf internal constructor(
    val driver: StorageDriver,
    val json: Json = Json
) {
    inline operator fun <reified T> get(key: String): T? {
        val raw = driver.load(key) ?: return null
        return runCatching { json.decodeFromString<T>(raw) }.getOrNull()
    }

    inline fun <reified T> get(key: String, default: T): T =
        get<T>(key) ?: default

    inline fun <reified T> put(key: String, value: T) {
        val raw = json.encodeToString(value)
        driver.save(key, raw, System.currentTimeMillis())
    }

    fun delete(key: String) {
        driver.delete(key)
    }

    fun exists(key: String): Boolean =
        driver.timestamp(key) != null

    fun timestamp(key: String): Long? =
        driver.timestamp(key)

    fun keys(): List<String> = driver.keys()

    inline fun <reified T> update(key: String, transform: (T?) -> T): T {
        val current = get<T>(key)
        val updated = transform(current)
        put(key, updated)
        return updated
    }

    inline operator fun <reified T> set(key: String, value: T) {
        put(key, value)
    }

    fun namespace(prefix: String, separator: String = ":"): NamespacedShelf =
        NamespacedShelf(this, prefix, separator)
}
