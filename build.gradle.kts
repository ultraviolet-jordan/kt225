import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.UsesKotlinJavaToolchain

@Suppress("DSL_SCOPE_VIOLATION")
// https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(deps.plugins.jvm)
    alias(deps.plugins.versions)
}

group = "org.example"
version = "1.0-SNAPSHOT"

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
            "$rootDir/common/src/main/resources/"
        )
    }

    tasks.withType<KotlinCompile> {
        kotlin {
            jvmToolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }

    tasks.withType<UsesKotlinJavaToolchain>().configureEach {
        kotlinJavaToolchain.toolchain.use(
            project.extensions.getByType<JavaToolchainService>().launcherFor {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        )
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
