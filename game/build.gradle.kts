plugins {
    application
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(deps.bundles.ktor)
    implementation(deps.guice)
    implementation(deps.cryptography)

    implementation(project(":cache"))
    implementation(project(":cache-225"))
    implementation(project(":common"))
    implementation(project(":network"))
    implementation(project(":packet"))
}

application {
    mainClass.set("kt225.game.ApplicationKt")
    applicationDefaultJvmArgs += listOf(
        "-XX:+UseZGC",
        "-Djava.library.path=$rootDir/common/src/main/resources/"
    )
}
