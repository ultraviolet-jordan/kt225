plugins {
    application
    alias(deps.plugins.jvm)
    alias(deps.plugins.shadow)
}

dependencies {
    implementation(deps.bundles.ktor)
    implementation(deps.guice)
    implementation(deps.pathfinder)

    implementation(project(":cache"))
    implementation(project(":cache-225"))
    implementation(project(":common"))
    implementation(project(":network"))
    implementation(project(":packet"))
}

application {
    mainClass.set("kt225.game.ApplicationKt")
    applicationDefaultJvmArgs += listOf(
        "-Xmx512m",
        "-XX:+UseZGC",
        "-Djava.library.path=$rootDir/cache/src/main/resources/"
    )
}
