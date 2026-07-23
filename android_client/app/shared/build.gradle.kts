import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val koinVersion = "3.5.3"

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "1.9.22"
    id("app.cash.sqldelight")
}

kotlin {
    jvmToolchain(21)
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    android {
       namespace = "com.garoz.devriyemobil.app.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()

       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }

    sourceSets {
        commonMain.dependencies {
            // HTTP İstekleri için Ktor Çekirdeği
            implementation("io.ktor:ktor-client-core:2.3.8")
            // JSON verilerini Kotlin sınıflarına çevirmek için
            implementation("io.ktor:ktor-client-content-negotiation:2.3.8")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.8")
            implementation("app.cash.sqldelight:coroutines-extensions:2.0.1")
            implementation("io.insert-koin:koin-core:3.5.3")
            implementation("io.insert-koin:koin-compose:1.1.2")


            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }

        androidMain.dependencies {
            // Android için Ktor motoru
            implementation("io.ktor:ktor-client-android:2.3.8")
            implementation("app.cash.sqldelight:android-driver:2.0.1")
            implementation("androidx.work:work-runtime-ktx:2.9.0")
            implementation("io.insert-koin:koin-android:${koinVersion}")
            implementation("io.insert-koin:koin-androidx-workmanager:${koinVersion}")
        }

        iosMain.dependencies {
            // iOS için Ktor motoru
            implementation("io.ktor:ktor-client-darwin:2.3.8")
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
sqldelight {
    databases {
        create("DevriyeDatabase") {
            packageName.set("com.garoz.devriyemobil.database")
        }
    }
}