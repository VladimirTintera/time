package eu.tintera

import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension
import java.net.URI
import java.util.Properties

plugins {
    id("maven-publish")
    signing
}

val projectName = project.name

// Load properties from local.properties if it exists
val localProperties = Properties()
val localPropertiesFile = project.rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

fun findPropertyOrLocal(name: String): String? {
    return project.findProperty(name) as? String
        ?: localProperties.getProperty(name)
        ?: System.getenv(name.uppercase().replace('.', '_'))
}

configure<PublishingExtension> {
    publications.withType<MavenPublication> {
        val javadocJar = tasks.register("${name}JavadocJar", Jar::class.java) {
            description = ""
            archiveClassifier.set("javadoc")
            archiveBaseName.set("${project.name}-${name}")
            val dokkaHtmlTask = project.tasks.findByName("dokkaGenerateHtml")
            if (dokkaHtmlTask != null) {
                dependsOn(dokkaHtmlTask)
                from(project.layout.buildDirectory.dir("dokka/html"))
            } else {
                val dummyFile = project.layout.buildDirectory.file("tmp/javadoc-dummy-${name}.txt")
                doFirst {
                    val file = dummyFile.get().asFile
                    file.parentFile.mkdirs()
                    file.writeText("Javadoc placeholder for ${project.name} - $name")
                }
                from(dummyFile)
            }
        }
        artifact(javadocJar)

        // Automatically prefix artifact IDs for submodules inside :time
        if (project.path.startsWith(":time:")) {
            artifactId = artifactId.replace(projectName, "time-$projectName")
        }

        pom {
            name.set(project.name)
            description.set("KMPTime library module - ${project.name}")
            url.set("https://github.com/VladimirTintera/time")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("vtintera")
                    name.set("Vladimír Tintěra")
                    email.set("vladimir.tintera@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:git://github.com/VladimirTintera/time.git")
                developerConnection.set("scm:git:ssh://github.com/VladimirTintera/time.git")
                url.set("https://github.com/VladimirTintera/time")
            }
        }
    }

    repositories {
        maven {
            name = "mavenCentral"
            url = URI("https://central.sonatype.com/api/v1/publisher/deployments/maven/")

            credentials {
                username = findPropertyOrLocal("mavenCentralUsername")
                password = findPropertyOrLocal("mavenCentralPassword")
            }
        }
        maven {
            name = "localRepo"
            url = URI("file://${rootProject.rootDir.absolutePath}/build/local-repo")
        }
    }
}

configure<SigningExtension> {
    val signingKey = findPropertyOrLocal("signing.key")
    val hasSigningKey = !signingKey.isNullOrEmpty()

    if (hasSigningKey) {
        useInMemoryPgpKeys(
            findPropertyOrLocal("signing.keyId"),
            signingKey,
            findPropertyOrLocal("signing.password")
        )
        // Podepíše všechny publikace, které v projektu existují
        sign(extensions.getByType<PublishingExtension>().publications)
    }

    // Podepisování bude striktně VYŽADOVÁNO pouze na CI,
    // kde klíč existuje a zároveň se spouští úkol spojený s Maven Central.
    // Pro lokální buildy bez klíče to zůstane volitelné (isRequired = false).
    isRequired = hasSigningKey && gradle.startParameter.taskNames.any {
        it.contains("mavenCentral", ignoreCase = true)
    }
}