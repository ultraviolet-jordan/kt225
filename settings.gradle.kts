@file:Suppress("UnstableApiUsage")

rootProject.name = "kt225"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    versionCatalogs {
        create("deps") {
            // Dependency versions.
            version("kotlin", "1.9.0-RC")
            version("ktor", "2.3.2")
            version("slf4j", "2.0.7")
            version("versions", "0.47.0")
            version("guice", "3.0.0")
            version("jnr", "2.2.14")
            version("commons-compress", "1.23.0")
            version("pathfinder", "2.4.3")

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
            library("jnr", "com.github.jnr", "jnr-ffi").versionRef("jnr")
            library("commons-compress", "org.apache.commons", "commons-compress").versionRef("commons-compress")
            library("pathfinder", "com.github.blurite", "pathfinder").versionRef("pathfinder")

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
include("cache-225")
include("common")
include("game")
include("http")
include("network")
include("packet")
