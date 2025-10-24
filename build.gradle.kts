plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    java
    war
    alias(libs.plugins.shadow)
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is used by the application.
    compileOnly(libs.bundles.jakarta)
    compileOnly(libs.bundles.infinispan)
    compileOnly(libs.bundles.javax)
    implementation(libs.gson)
}

java {
    // Apply a specific Java toolchain to ease working on different environments.
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.war {
    from("/src/webapp") {
        into("")
    }
}