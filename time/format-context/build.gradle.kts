import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.dokka)
}

kotlin {

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xexpect-actual-classes",
            "-Xcontext-parameters"
        )
        optIn.addAll("kotlin.js.ExperimentalWasmJsInterop")
    }

    androidLibrary {
        namespace = "eu.tintera.time.format.context"
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
            api(projects.time.format)
            api(projects.time.coreContext)
            implementation(projects.locale)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
