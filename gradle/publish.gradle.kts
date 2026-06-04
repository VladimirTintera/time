import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension
import java.net.URI
import java.util.Properties

apply(plugin = "maven-publish")
apply(plugin = "signing")

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
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("vtintera")
                    name.set("Vladimír Tintera")
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
            val isRelease = !project.version.toString().endsWith("SNAPSHOT")
            val repoUrl = if (isRelease) {
                "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            } else {
                "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            }
            url = URI(repoUrl)
            credentials {
                username = findPropertyOrLocal("mavenCentralUsername")
                password = findPropertyOrLocal("mavenCentralPassword")
            }
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
        sign(extensions.getByType<PublishingExtension>().publications)
    }
}
