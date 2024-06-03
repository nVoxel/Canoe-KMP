pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://androidx.dev/storage/compose-compiler/repository")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://androidx.dev/storage/compose-compiler/repository")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }
}

rootProject.name = "Canoe"
include(":composeApp")
include(":source:compose-ui")
include(":source:data:database")
include(":source:data:local")
include(":source:data:network")
include(":source:feature:dashboard")
include(":source:feature:dashboard-api")
include(":source:feature:leaderboards")
include(":source:feature:leaderboards-api")
include(":source:feature:projects")
include(":source:feature:projects-api")
include(":source:feature:settings")
include(":source:feature:settings-api")
include(":source:root")
include(":source:utils")
include(":shared")
