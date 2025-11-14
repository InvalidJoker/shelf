package de.joker.shelf.driver

/**
 * Base interface for serialization-only storage drivers.
 */
interface StorageDriver {
    fun save(key: String, data: String, timestamp: Long)
    fun load(key: String): String?
    fun delete(key: String)

    fun timestamp(key: String): Long?

    fun keys(): List<String>
}