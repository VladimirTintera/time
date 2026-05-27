plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.dokka) apply true
}

dokka {
    dokkaPublications.html {
        outputDirectory.set(rootDir.resolve("docs"))
        includes.from(project.layout.projectDirectory.file("README.md"))
    }
}

dependencies {
    dokka(projects.locale)
    dokka(projects.time.core)
    dokka(projects.time.format)
    dokka(projects.time.coreContext)
    dokka(projects.time.formatContext)
}