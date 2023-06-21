plugins {
    alias(deps.plugins.jvm)
}

dependencies {
    implementation(deps.guice)
    implementation(deps.jnr)
    implementation(deps.commons.compress)

    implementation(project(":common"))
}
