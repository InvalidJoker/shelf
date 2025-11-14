package de.joker.shelf

import de.joker.shelf.driver.FolderDriver
import de.joker.shelf.driver.StorageDriver
import kotlinx.serialization.json.Json
import java.io.File

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

fun shelf(block: ShelfBuilder.() -> Unit = {}): Shelf =
    ShelfBuilder().apply(block).build()