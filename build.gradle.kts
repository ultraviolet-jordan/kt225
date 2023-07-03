import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    alias(deps.plugins.jvm)
    alias(deps.plugins.versions)
}

allprojects {
    plugins.withType<KotlinPluginWrapper> {
        dependencies {
            implementation(kotlin("stdlib"))
            implementation(deps.slf4j.simple)
        }
    }

    tasks.withType<Test> {
        dependencies {
            testImplementation(kotlin("test"))
            testImplementation(deps.slf4j.simple)
        }
        systemProperty(
            "java.library.path",
            "$rootDir/cache/src/main/resources/"
        )
    }
}

kotlin {
    jvmToolchain(17)
}
