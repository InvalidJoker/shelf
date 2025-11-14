package de.joker.shelf.driver

/**
 * A driver that stores multiple serialized objects in memory.
 * Useful for testing or ephemeral data.
 *
 * @author InvalidJoker
 * @since 1.0.0
 */
class MemoryDriver : StorageDriver {
    private data class Entry(
        val data: String,
        val timestamp: Long
    )

    private val store = mutableMapOf<String, Entry>()

    override fun save(key: String, data: String, timestamp: Long) {
        store[key] = Entry(data, timestamp)
    }

    override fun load(key: String): String? {
        return store[key]?.data
    }

    override fun delete(key: String) {
        store.remove(key)
    }

    override fun timestamp(key: String): Long? {
        return store[key]?.timestamp
    }

    override fun keys(): List<String> {
        return store.keys.toList()
    }
}
