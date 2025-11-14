# Shelf
> A lightweight **Kotlinx Serialization–based key-value store** with namespaces, DSL, smart operators, delegates, and pluggable drivers.

## Installation
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.invalidjoker:shelf:<latest-version>")
}
```

## Basic Usage
```kotlin
import de.joker.shelf.shelf
import kotlinx.serialization.Serializable

fun main() {
    // Create a Shelf that stores data in ./data/
    val shelf = shelf()

    // Store a simple value
    shelf["message"] = "Hello Shelf!"

    // Retrieve it
    val message: String? = shelf["message"]
    println(message) // -> Hello Shelf!

    // Store a serialized object
    @Serializable
    data class User(val name: String, val age: Int)

    shelf["user:42"] = User("Alice", 22)

    // Load it back
    val user: User? = shelf["user:42"]
    println(user) // -> User(name=Alice, age=22)
}
```