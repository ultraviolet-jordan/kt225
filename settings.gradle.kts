rootProject.name = "kt225"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://repo.openrs2.org/repository/openrs2-snapshots")
    }

    versionCatalogs {
        create("deps") {
            // Dependency versions.
            version("kotlin", "1.8.20")
            version("ktor", "2.3.1")
            version("slf4j", "2.0.7")
            version("cryptography", "1.2.0-SNAPSHOT")
            version("versions", "0.47.0")
            version("guice", "1.6.0")
            version("openrs2", "0.1.0-SNAPSHOT")

            // Dependency plugins
            plugin("jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("versions", "com.github.ben-manes.versions").versionRef("versions")

            // Dependency libraries
            // Ktor Dependencies
            library("ktor-server-core", "io.ktor", "ktor-server-core").versionRef("ktor")
            library("ktor-server-netty", "io.ktor", "ktor-server-netty").versionRef("ktor")
            library("ktor-server-call-logging", "io.ktor", "ktor-server-call-logging").versionRef("ktor")

            // Guice
            library("guice", "dev.misfitlabs.kotlinguice4", "kotlin-guice").versionRef("guice")

            // Misc Dependencies
            library("slf4j-simple", "org.slf4j", "slf4j-simple").versionRef("slf4j")
            library("cryptography", "com.runetopic.cryptography", "cryptography").versionRef("cryptography")
            library("openrs2-compress", "org.openrs2", "openrs2-compress").versionRef("openrs2")

            // Dependency bundles
            listOf(
                "ktor-server-core",
                "ktor-server-netty",
                "ktor-server-call-logging"
            ).also { bundle("ktor", it) }
        }
    }
}

include("cache")
include("game")
include("common")
include("http")
include("cache-225")
include("packet")
include("network")
