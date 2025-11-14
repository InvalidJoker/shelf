package de.joker.shelf

import de.joker.shelf.driver.StorageDriver
import kotlinx.serialization.json.Json

/**
 * Main class for managing storage of serialized objects using a [StorageDriver].
 *
 * @property driver The storage driver used for saving and loading data.
 * @property json The JSON configuration used for serialization and deserialization.
 *
 * @author InvalidJoker
 * @since 1.0.0
 */
class Shelf internal constructor(
    val driver: StorageDriver,
    val json: Json = Json
) {

    /**
     * Retrieve and deserialize an object of type [T] associated with the given [key].
     *
     * @param T The type of the object to retrieve.
     * @param key The key associated with the object.
     * @return The deserialized object, or null if not found or deserialization fails.
     */
    inline operator fun <reified T> get(key: String): T? {
        val raw = driver.load(key) ?: return null
        return runCatching { json.decodeFromString<T>(raw) }.getOrNull()
    }

    /**
     * Retrieve and deserialize an object of type [T] associated with the given [key],
     * or return a [default] value if not found.
     *
     * @param T The type of the object to retrieve.
     * @param key The key associated with the object.
     * @param default The default value to return if the object is not found.
     * @return The deserialized object, or the [default] value.
     */
    inline fun <reified T> get(key: String, default: T): T =
        get<T>(key) ?: default

    /**
     * Serialize and store an object of type [T] with the given [key].
     *
     * @param T The type of the object to store.
     * @param key The key to associate with the object.
     * @param value The object to serialize and store.
     */
    inline fun <reified T> put(key: String, value: T) {
        val raw = json.encodeToString(value)
        driver.save(key, raw, System.currentTimeMillis())
    }

    /**
     * Delete the object associated with the given [key].
     *
     * @param key The key of the object to delete.
     */
    fun delete(key: String) {
        driver.delete(key)
    }

    /**
     * Check if an object associated with the given [key] exists.
     *
     * @param key The key to check for existence.
     * @return True if the object exists, false otherwise.
     */
    fun exists(key: String): Boolean =
        driver.timestamp(key) != null

    /**
     * Get the timestamp of when the object associated with the given [key] was last saved.
     *
     * @param key The key of the object.
     * @return The timestamp, or null if the key does not exist.
     */
    fun timestamp(key: String): Long? =
        driver.timestamp(key)

    /**
     * Get a list of all keys currently stored.
     *
     * @return A list of keys.
     */
    fun keys(): List<String> = driver.keys()

    /**
     * Update the object associated with the given [key] by applying a [transform] function.
     * If the object does not exist, null is passed to the [transform] function.
     *
     * @param T The type of the object to update.
     * @param key The key associated with the object.
     * @param transform The function to apply to the current object to produce the updated object.
     * @return The updated object.
     */
    inline fun <reified T> update(key: String, transform: (T?) -> T): T {
        val current = get<T>(key)
        val updated = transform(current)
        put(key, updated)
        return updated
    }

    /**
     * Operator function to set the object associated with the given [key].
     *
     * @param T The type of the object to set.
     * @param key The key to associate with the object.
     * @param value The object to serialize and store.
     */
    inline operator fun <reified T> set(key: String, value: T) {
        put(key, value)
    }

    /**
     * Create a [NamespacedShelf] with the given [prefix] and optional [separator].
     * All keys accessed through the returned [NamespacedShelf] will be prefixed accordingly.
     *
     * @param prefix The prefix to use for namespacing keys.
     * @param separator The separator between the prefix and the key (default is ":").
     * @return A [NamespacedShelf] instance.
     */
    fun namespace(prefix: String, separator: String = ":"): NamespacedShelf =
        NamespacedShelf(this, prefix, separator)
}
