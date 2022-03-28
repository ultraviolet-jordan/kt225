rootProject.name = "kt225"

dependencyResolutionManagement {
    repositories(RepositoryHandler::mavenCentral)

    versionCatalogs {
        create("deps") {
            version("kotlin", "1.6.10")
            version("ktor", "1.6.7")
            version("koin", "3.1.5")
            version("slf4j", "1.7.32")
            version("kotlin-inline-logger", "1.0.4")

            library("ktor-server-netty", "io.ktor", "ktor-server-netty").versionRef("ktor")
            library("koin-core", "io.insert-koin", "koin-core").versionRef("koin")
            library("koin-ktor", "io.insert-koin", "koin-ktor").versionRef("koin")
            library("slf4j-simple", "org.slf4j", "slf4j-simple").versionRef("slf4j")
            library("kotlin-inline-logger", "com.michael-bull.kotlin-inline-logger", "kotlin-inline-logger").versionRef("kotlin-inline-logger")

            bundle("ktor", listOf("ktor-server-netty"))
            bundle("koin", listOf("koin-core", "koin-ktor"))
            bundle("logger", listOf("kotlin-inline-logger", "slf4j-simple"))

            plugin("jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
        }
    }
}

include("cache")
include("application")
include("shared")
