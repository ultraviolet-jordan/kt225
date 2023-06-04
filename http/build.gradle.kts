@Suppress("DSL_SCOPE_VIOLATION")
// https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    application
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(deps.bundles.ktor)
    implementation(deps.slf4j.simple)
    implementation(deps.guice)

    implementation(project(":cache"))
}

application {
    mainClass.set("kt225.http.ApplicationKt")
}
