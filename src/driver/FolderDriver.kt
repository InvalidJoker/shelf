package de.joker.shelf.driver

import java.io.File

/**
 * A driver that stores *multiple* serialized objects inside a folder.
 * Each object = one file, name is determined by key.
 */
class FolderDriver(
    private val folder: File
): StorageDriver {
    init { folder.mkdirs() }

    val String.fileName: String
        get() = "${this.replace(Regex("[^a-zA-Z0-9._-]"), "_")}.shelf"

    override fun save(key: String, data: String, timestamp: Long) {
        val file = File(folder, key.fileName)
        file.parentFile?.mkdirs()
        file.writeText(data)
        file.setLastModified(timestamp)
    }

    override fun load(key: String): String? {
        val file = File(folder, key.fileName)
        if (!file.exists()) return null
        return file.readText()
    }

    override fun delete(key: String) {
        val file = File(folder, key.fileName)
        if (file.exists()) {
            file.delete()
        }
    }

    override fun timestamp(key: String): Long? {
        val file = File(folder, key.fileName)
        if (!file.exists()) return null
        return file.lastModified()
    }

    override fun keys(): List<String> {
        return folder.listFiles()?.mapNotNull { file ->
            val name = file.name
            if (name.endsWith(".shelf")) {
                name.removeSuffix(".shelf").replace(Regex("_"), " ")
            } else {
                null
            }
        } ?: emptyList()
    }
}