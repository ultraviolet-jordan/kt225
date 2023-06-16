plugins {
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(deps.guice)

    implementation(project(":common"))
}
