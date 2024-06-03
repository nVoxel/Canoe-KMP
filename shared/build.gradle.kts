plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.versions.jvm.target.get()
            }
        }
    }

    jvm()

    sourceSets {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "CanoeKit"
                isStatic = true

                export(project(":source:data:database"))
                export(project(":source:data:local"))
                export(project(":source:data:network"))
                export(project(":source:feature:dashboard"))
                export(project(":source:feature:dashboard-api"))
                export(project(":source:feature:leaderboards"))
                export(project(":source:feature:leaderboards-api"))
                export(project(":source:feature:projects"))
                export(project(":source:feature:projects-api"))
                export(project(":source:feature:settings"))
                export(project(":source:feature:settings-api"))
                export(project(":source:root"))
                export(project(":source:utils"))

                export(libs.decompose)
                export(libs.mvikotlin)
                export(libs.mvikotlin.main)
                export(libs.essenty.lifecycle)
            }
        }

        commonMain.dependencies {
            api(project(":source:data:database"))
            api(project(":source:data:local"))
            api(project(":source:data:network"))
            api(project(":source:feature:dashboard"))
            api(project(":source:feature:dashboard-api"))
            api(project(":source:feature:leaderboards"))
            api(project(":source:feature:leaderboards-api"))
            api(project(":source:feature:projects"))
            api(project(":source:feature:projects-api"))
            api(project(":source:feature:settings"))
            api(project(":source:feature:settings-api"))
            api(project(":source:root"))
            api(project(":source:utils"))

            api(libs.decompose)
            api(libs.mvikotlin)
            api(libs.mvikotlin.main)
            api(libs.essenty.lifecycle)
        }
    }
}

android {
    namespace = "com.voxeldev.canoe.shared"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }
}
