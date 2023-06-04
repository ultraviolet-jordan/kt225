rootProject.name = "kt225"

dependencyResolutionManagement {
    repositories(RepositoryHandler::mavenCentral)
    repositories { maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") } }

    versionCatalogs {
        create("deps") {
            // Dependency versions.
            version("kotlin", "1.8.20")
            version("ktor", "2.3.1")
            version("slf4j", "2.0.7")
            version("cryptography", "1.2.0-SNAPSHOT")
            version("versions", "0.46.0")
            version("guice", "1.6.0")
            version("apache-commons", "1.23.0")

            // Dependency plugins
            plugin("jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("versions", "com.github.ben-manes.versions").versionRef("versions")

            // Dependency libraries
            // Ktor Dependencies
            library("ktor-server-core", "io.ktor", "ktor-server-core").versionRef("ktor")
            library("ktor-server-netty", "io.ktor", "ktor-server-netty").versionRef("ktor")

            // Guice
            library("guice", "dev.misfitlabs.kotlinguice4", "kotlin-guice").versionRef("guice")

            // Misc Dependencies
            library("slf4j-simple", "org.slf4j", "slf4j-simple").versionRef("slf4j")
            library("cryptography", "com.runetopic.cryptography", "cryptography").versionRef("cryptography")
            library("apache-commons", "org.apache.commons", "commons-compress").versionRef("apache-commons")

            // Dependency bundles
            listOf(
                "ktor-server-core",
                "ktor-server-netty"
            ).also { bundle("ktor", it) }
        }
    }
}

include("cache")
include("game")
include("common")
include("http")
include("cache-225")
