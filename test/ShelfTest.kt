package de.joker.shelf

import de.joker.shelf.driver.FolderDriver
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Serializable
data class TestUser(val name: String, val age: Int)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShelfTest {

    private lateinit var tempDir: File
    private lateinit var shelf: Shelf

    @BeforeEach
    fun setup() {
        tempDir = createTempDir(prefix = "shelf_test_")
        shelf = shelf {
            driver(FolderDriver(tempDir))
        }
    }

    @AfterEach
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `put and get simple data`() {
        shelf["stringKey"] = "Hello"
        val value: String? = shelf["stringKey"]
        assertEquals("Hello", value)
    }

    @Test
    fun `put and get serialized object`() {
        val user = TestUser("Joker", 21)
        shelf["user"] = user

        val loaded: TestUser? = shelf["user"]
        assertEquals(user, loaded)
    }

    @Test
    fun `delete removes a key`() {
        shelf["x"] = 10
        shelf.delete("x")

        assertNull(shelf["x"])
    }

    @Test
    fun `exists works correctly`() {
        assertFalse(shelf.exists("nope"))

        shelf["counter"] = 1
        assertTrue(shelf.exists("counter"))
    }

    @Test
    fun `update transforms stored values`() {
        shelf["count"] = 1

        shelf.update<Int>("count") { (it ?: 0) + 5 }

        assertEquals(6, shelf["count"])
    }

    @Test
    fun `keys returns all keys`() {
        shelf["a"] = 1
        shelf["b"] = 2
        shelf["c"] = 3

        val keys = shelf.keys()

        assertEquals(setOf("a", "b", "c"), keys.toSet())
    }

    @Test
    fun `namespace isolates keys`() {
        val users = shelf.namespace("users")
        val system = shelf.namespace("system")

        users["42"] = TestUser("Alice", 18)
        system["version"] = "1.0.0"

        val loadedUser: TestUser? = users["42"]
        val loadedVersion: String? = system["version"]

        assertEquals("Alice", loadedUser?.name)
        assertEquals("1.0.0", loadedVersion)

        // ensure namespace separation
        assertNull(shelf["42"])
        assertNull(users["version"])
    }
}
