import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {

    //binance
    implementation("io.github.binance:binance-connector-java:3.2.0")

    //gson
    implementation("com.google.code.gson:gson:2.10.1")

    //ktor
    implementation("io.ktor:ktor-client-content-negotiation:2.3.11")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.2")
    implementation("io.ktor:ktor-client-core:2.3.11")
    implementation("io.ktor:ktor-client-cio:2.3.11")

    //koin
    implementation("io.insert-koin:koin-core:3.6.0-wasm-alpha2")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")


    //kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("org.apache.commons:commons-lang3:3.12.0")


    testImplementation(kotlin("test"))
}


tasks {
    // Register the shadowJar task
    withType<ShadowJar> {
        archiveBaseName.set("algo-jar")
        archiveClassifier.set("")
        archiveVersion.set(version.toString())
        mergeServiceFiles() // Optional: This merges service files in META-INF/services
        // Optionally include/exclude files
        // include("**/*.class")
        // exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }

    build {
        dependsOn(shadowJar)
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.example.MainKt"
    }
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}