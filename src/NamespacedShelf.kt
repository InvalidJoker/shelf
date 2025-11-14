package de.joker.shelf

/**
 * A namespaced view of a Shelf, prefixing all keys with a given namespace.
 *
 * @property shelf The underlying Shelf instance.
 * @property prefix The namespace prefix to use for keys.
 * @property separator The separator between the prefix and the key name. Default is ":".
 *
 * @author InvalidJoker
 * @since 1.0.0
 */
class NamespacedShelf(
    val shelf: Shelf,
    private val prefix: String,
    private val separator: String = ":"
) {
    /**
     * Constructs the full key by combining the prefix, separator, and the given name.
     *
     * @param name The name of the key.
     * @return The full namespaced key.
     */
    fun key(name: String): String = "$prefix$separator$name"

    /**
     * Retrieves the value associated with the given name, deserialized to type [T].
     *
     * @param T The type of the object to retrieve.
     * @param name The name of the key.
     * @return The deserialized object, or null if not found.
     */
    inline operator fun <reified T> get(name: String): T? =
        shelf[key(name)]

    /**
     * Stores the given value under the specified name, serializing it to type [T].
     *
     * @param T The type of the object to store.
     * @param name The name of the key.
     * @param value The object to serialize and store.
     */
    inline operator fun <reified T> set(name: String, value: T) {
        shelf[key(name)] = value
    }

    /**
     * Retrieves the value associated with the given name, deserialized to type [T],
     * or returns the provided default value if not found.
     *
     * @param T The type of the object to retrieve.
     * @param name The name of the key.
     * @param default The default value to return if the object is not found.
     * @return The deserialized object, or the default value.
     */
    inline fun <reified T> get(name: String, default: T): T =
        shelf.get(key(name), default)

    /**
     * Updates the value associated with the given name by applying the provided transform function.
     * If the object does not exist, null is passed to the transform function.
     *
     * @param T The type of the object to update.
     * @param name The name of the key.
     * @param transform The function to apply to the current object to produce the updated object.
     * @return The updated object.
     */
    inline fun <reified T> update(name: String, transform: (T?) -> T): T =
        shelf.update(key(name), transform)

    /**
     * Deletes the object associated with the given name.
     *
     * @param name The name of the key.
     */
    fun delete(name: String) = shelf.delete(key(name))

    /**
     * Checks if an object associated with the given name exists.
     *
     * @param name The name of the key.
     * @return True if the object exists, false otherwise.
     */
    fun exists(name: String): Boolean = shelf.exists(key(name))
}
