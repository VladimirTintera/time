
plugins {
    `kotlin-dsl`
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    val agpVersion = libs.findVersion("agp").get().requiredVersion
    val kotlinVersion = libs.findVersion("kotlin").get().requiredVersion
    implementation("com.android.tools.build:gradle:$agpVersion")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}