plugins {
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(deps.bundles.ktor)
    implementation(deps.pathfinder)
    testImplementation(deps.bouncycastle)
}
