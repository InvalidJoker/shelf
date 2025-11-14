import java.util.Calendar

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.21"

    `java-library`
    `maven-publish`
    signing
}

group = "de.joker"

// Taken from https://github.com/TheFruxz/Stacked/blob/develop/build.gradle.kts
val publishVersion: String? = System.getenv("GH_RELEASE_VERSION")
val calendar: Calendar = Calendar.getInstance()
version = publishVersion ?: "${calendar[Calendar.YEAR]}.${calendar[Calendar.MONTH] + 1}-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
    test {
        kotlin.srcDir("test")
    }
}


tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components.findByName("java"))

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            groupId = "dev.invalidjoker"
            artifactId = project.name
            version = project.version.toString()

            pom {
                name.set(project.name)
                description.set("Key/value object store for Kotlin. Persist any serializable object.")
                url.set("https://github.com/InvalidJoker/shelf")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("invalidjoker")
                        name.set("InvalidJoker")
                    }
                }
            }
        }
    }

    repositories {
        val repoUrl = if (project.version.toString().endsWith("SNAPSHOT")) {
            "https://central.sonatype.com/repository/maven-snapshots/"
        } else {
            "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/"
        }
        maven {
            name = "sonatype"
            url = uri(repoUrl)
            credentials {
                username = findProperty("sonatypeUsername") as String?
                password = findProperty("sonatypePassword") as String?
            }
        }
    }
}

signing {
    sign(publishing.publications)
}