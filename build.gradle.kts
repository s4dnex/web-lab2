plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    alias(libs.plugins.shadow)
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is used by the application.
    implementation(libs.gson)
}

java {
    // Apply a specific Java toolchain to ease working on different environments.
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

application {
    // Define the main class for the application.
    mainClass = "sadnex.web.Main"
}

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    archiveFileName.set("${project.name}.jar")
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.startScripts {
    dependsOn(tasks.shadowJar)
    classpath = files(tasks.shadowJar.get().archiveFile)
}
