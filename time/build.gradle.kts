import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

kotlin {
    androidLibrary {
        namespace = "eu.tintera.time.android"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    jvm()

    iosArm64()
    iosSimulatorArm64()

    watchosArm64()
    watchosSimulatorArm64()

    macosArm64()

    tvosArm64()
    tvosSimulatorArm64()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.datetime)
        }

        webMain.dependencies {
            implementation(libs.wrappers.browser)
        }

        jvmMain.dependencies {
            implementation(libs.icu4j)
            implementation("org.ocpsoft.prettytime:prettytime:5.0.4.Final")
        }
    }
}