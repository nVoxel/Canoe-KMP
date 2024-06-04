import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.build.config)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.versions.jvm.target.get()
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.security.crypto)
            implementation(libs.decompose)
            implementation(libs.mvikotlin)
            implementation(libs.mvikotlin.rx)
            implementation(libs.ktor.client.core)

            implementation(libs.koin.core)

            api(libs.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.activity.compose)
            implementation(libs.material)

            implementation(libs.koin)

            implementation(libs.firebase.analytics)
        }
    }
}

buildConfig {
    buildConfigField("wakatime_base_url", extra["WAKATIME_BASE_URL"].toString())
    buildConfigField("wakatime_api_base_url", extra["WAKATIME_API_BASE_URL"].toString())
    buildConfigField("wakatime_oauth_base_url", extra["WAKATIME_OAUTH_BASE_URL"].toString())
    buildConfigField("wakatime_photo_base_url", extra["WAKATIME_PHOTO_BASE_URL"].toString())
    buildConfigField("wakatime_profile_base_url", extra["WAKATIME_PROFILE_BASE_URL"].toString())

    buildConfigField("oauth_redirect_url_deeplink", extra["OAUTH_REDIRECT_URL_DEEPLINK"].toString())
    buildConfigField("oauth_redirect_url_inbrowser", extra["OAUTH_REDIRECT_URL_INBROWSER"].toString())

    val secrets = Properties()
    secrets.load(FileInputStream(rootProject.file("secrets.properties")))
    buildConfigField("oauth_client_id", secrets["OAUTH_CLIENT_ID"].toString())
    buildConfigField("oauth_client_secret", secrets["OAUTH_CLIENT_SECRET"].toString())
}

android {
    namespace = "com.voxeldev.canoe.utils"
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

    testFixtures {
        enable = true
    }
}

dependencies {
    testImplementation(libs.junit)
    testFixturesImplementation(libs.junit)
    testFixturesImplementation(libs.mockito.core)
    testFixturesImplementation(libs.mockito.kotlin)
    testFixturesImplementation(libs.koin.test)
    testFixturesImplementation(libs.koin.test.junit4)
    testFixturesImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.test.ext.junit)
}
