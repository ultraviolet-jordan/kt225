plugins {
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(deps.bundles.ktor)
    implementation(deps.cryptography)
}
