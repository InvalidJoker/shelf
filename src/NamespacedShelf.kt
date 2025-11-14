package de.joker.shelf

class NamespacedShelf(
    val shelf: Shelf,
    private val prefix: String,
    private val separator: String = ":"
) {
    fun key(name: String): String = "$prefix$separator$name"

    inline operator fun <reified T> get(name: String): T? =
        shelf[key(name)]

    inline operator fun <reified T> set(name: String, value: T) {
        shelf[key(name)] = value
    }

    inline fun <reified T> get(name: String, default: T): T =
        shelf.get(key(name), default)

    inline fun <reified T> update(name: String, transform: (T?) -> T): T =
        shelf.update(key(name), transform)

    fun delete(name: String) = shelf.delete(key(name))
    fun exists(name: String): Boolean = shelf.exists(key(name))
}
