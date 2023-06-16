plugins {
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(deps.apache.commons)
    implementation(deps.bundles.ktor)
    implementation(deps.cryptography)
}
