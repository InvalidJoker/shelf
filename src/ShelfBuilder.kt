package de.joker.shelf

import de.joker.shelf.driver.FolderDriver
import de.joker.shelf.driver.StorageDriver
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Builder class for constructing a [Shelf] instance with custom configuration.
 *
 * @author InvalidJoker
 * @since 1.0.0
 */
class ShelfBuilder {
    private var driver: StorageDriver? = null
    private var json: Json = Json

    fun json(config: Json) {
        this.json = config
    }

    fun driver(driver: StorageDriver) {
        this.driver = driver
    }

    fun build(): Shelf = Shelf(driver ?: FolderDriver(File("data")), json)
}

/**
 * DSL function to create a [Shelf] instance using [ShelfBuilder].
 *
 * @param block The configuration block for the [ShelfBuilder].
 * @return The constructed [Shelf] instance.
 *
 * @author InvalidJoker
 * @since 1.0.0
 */
fun shelf(block: ShelfBuilder.() -> Unit = {}): Shelf =
    ShelfBuilder().apply(block).build()