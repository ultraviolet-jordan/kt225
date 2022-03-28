import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
// https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(deps.plugins.jvm)
}

group = "org.example"
version = "1.0-SNAPSHOT"

allprojects {
    plugins.withType<KotlinPluginWrapper> {
        dependencies {
            implementation(kotlin("stdlib"))
            implementation(deps.bundles.ktor)
            implementation(deps.bundles.koin)
        }
    }

    with(tasks) {
        withType<Test> {
            dependencies {
                testImplementation(kotlin("test"))
            }
        }
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.freeCompilerArgs = listOf(
                "-Xopt-in=kotlin.ExperimentalUnsignedTypes"
            )
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
