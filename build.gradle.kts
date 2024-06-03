import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false

    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    alias(libs.plugins.build.config) apply false

    alias(libs.plugins.jetbrains.compose) apply false

    alias(libs.plugins.realm) apply false

    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.performance) apply false
    alias(libs.plugins.google.services) apply false

    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dependency.analysis) apply true
}

val projectSource = file(projectDir)
val configFile = files("$rootDir/config/detekt/detekt.yml")
val baselineFile = file("$rootDir/config/detekt/baseline.xml")
val kotlinFiles = "**/*.kt"
val iosFiles = "**/iosApp/**"
val resourceFiles = "**/resources/**"
val buildFiles = "**/build/**"

apply(plugin = "io.gitlab.arturbosch.detekt")

dependencies {
    detektPlugins(libs.detekt.formatting)
}

tasks.register(name = "detektAll", type = Detekt::class) {
    description = "Custom detekt build for all modules"
    parallel = true
    ignoreFailures = false
    autoCorrect = true
    buildUponDefaultConfig = true
    setSource(projectSource)
    baseline.set(baselineFile)
    config.setFrom(configFile)
    include(kotlinFiles)
    exclude(iosFiles, resourceFiles, buildFiles)
    reports {
        html.required.set(true)
        xml.required.set(false)
        txt.required.set(false)
    }
}

// https://github.com/detekt/detekt/issues/6555
fun DependencyHandlerScope.detektPlugins(dependencyNotation: Any) {
    add("detektPlugins", dependencyNotation)
}
