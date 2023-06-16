plugins {
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(deps.guice)

    implementation(project(":cache"))
    implementation(project(":common"))
}
