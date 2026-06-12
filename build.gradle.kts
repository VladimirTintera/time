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
    alias(libs.plugins.nmcp.aggregation)
}

dokka {
    dokkaPublications.html {
        outputDirectory.set(rootDir.resolve("docs"))
        includes.from(project.layout.projectDirectory.file("README.md"))
    }
}

val localProperties = java.util.Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

fun findPropertyOrLocal(name: String): String? {
    return project.findProperty(name) as? String
        ?: localProperties.getProperty(name)
        ?: System.getenv(name.uppercase().replace('.', '_'))
}

nmcpAggregation {
    centralPortal {
        username.set(findPropertyOrLocal("mavenCentralUsername"))
        password.set(findPropertyOrLocal("mavenCentralPassword"))
        publishingType.set("AUTOMATIC")
    }
}

dependencies {
    dokka(projects.locale)
    dokka(projects.time.core)
    dokka(projects.time.format)
    dokka(projects.time.coreContext)
    dokka(projects.time.formatContext)

    nmcpAggregation(projects.locale)
    nmcpAggregation(projects.time.core)
    nmcpAggregation(projects.time.format)
    nmcpAggregation(projects.time.coreContext)
    nmcpAggregation(projects.time.formatContext)
}