rootProject.name = "kt225"

dependencyResolutionManagement {
    repositories(RepositoryHandler::mavenCentral)

    versionCatalogs {
        create("deps") {
            version("kotlin", "1.6.10")
            version("ktor", "1.6.7")
            version("koin", "3.1.5")

            library("ktor-server-netty", "io.ktor", "ktor-server-netty").versionRef("ktor")
            library("koin-core", "io.insert-koin", "koin-core").versionRef("koin")
            library("koin-ktor", "io.insert-koin", "koin-ktor").versionRef("koin")

            bundle("ktor", listOf("ktor-server-netty"))
            bundle("koin", listOf("koin-core", "koin-ktor"))

            plugin("jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
        }
    }
}

include("cache")
include("application")
include("shared")
