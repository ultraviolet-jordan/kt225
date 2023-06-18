plugins {
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(deps.guice)
    implementation(deps.openrs2.compress)

    implementation(project(":common"))
}
