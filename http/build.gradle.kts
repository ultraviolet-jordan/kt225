plugins {
    application
    alias(deps.plugins.jvm)
    alias(deps.plugins.shadow)
}

dependencies {
    implementation(deps.bundles.ktor)
    implementation(deps.guice)

    implementation(project(":cache"))
}

application {
    mainClass.set("kt225.http.ApplicationKt")
    applicationDefaultJvmArgs += listOf(
        "-Xmx128m",
        "-XX:+UseZGC",
        "-Djava.library.path=$rootDir/cache/src/main/resources/"
    )
}
