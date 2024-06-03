plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
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
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.decompose)
            implementation(libs.decompose.extensions)

            implementation(libs.paging.compose)
            implementation(libs.shimmer)

            implementation(compose.material3)
            // implementation(libs.material.icons.extended)

            implementation(libs.ktor.client.core)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.mp)
            implementation(libs.coil.network.ktor)

            implementation(libs.koalaplot)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            api(project(":source:root"))
            api(project(":source:feature:dashboard"))
            api(project(":source:feature:dashboard-api"))
            api(project(":source:feature:leaderboards"))
            api(project(":source:feature:projects"))
            api(project(":source:feature:settings"))
            implementation(project(":source:feature:leaderboards-api"))
            implementation(project(":source:feature:projects-api"))
            implementation(project(":source:utils"))
        }

        androidMain.dependencies {
            implementation(libs.activity.compose)
        }
    }
}

android {
    namespace = "com.voxeldev.canoe.compose.ui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.extension.get()
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
}
