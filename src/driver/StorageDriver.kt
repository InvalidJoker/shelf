package de.joker.shelf.driver

/**
 * Base interface for storage drivers that handle saving, loading, etc. of serialized objects.
 *
 * @author InvalidJoker
 * @since 1.0.0
 */
interface StorageDriver {
    /**
     * Save serialized data with the given key and timestamp.
     *
     * @param key The key to associate with the data.
     * @param data The serialized data to save.
     * @param timestamp The timestamp of when the data was saved.
     */
    fun save(key: String, data: String, timestamp: Long)

    /**
     * Load serialized data associated with the given key.
     *
     * @param key The key of the data to load.
     * @return The serialized data, or null if not found.
     */
    fun load(key: String): String?

    /**
     * Delete the data associated with the given key.
     *
     * @param key The key of the data to delete.
     */
    fun delete(key: String)

    /**
     * Get the timestamp of when the data associated with the given key was last saved.
     *
     * @param key The key of the data.
     * @return The timestamp, or null if the key does not exist.
     */
    fun timestamp(key: String): Long?

    /**
     * Get a list of all keys currently stored.
     *
     * @return A list of keys.
     */
    fun keys(): List<String>
}