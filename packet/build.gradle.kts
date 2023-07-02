plugins {
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(deps.bundles.ktor)
    implementation(deps.guice)
    implementation(deps.pathfinder)

    implementation(project(":cache"))
    implementation(project(":common"))
}
