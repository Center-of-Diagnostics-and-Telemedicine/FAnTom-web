plugins {
  `kotlin-dsl`
  kotlin("plugin.serialization") version "1.4.30"
}

buildscript {
  repositories { mavenCentral() }

  dependencies {
    val kotlinVersion = "1.4.30"
    classpath(kotlin("serialization", version = kotlinVersion))
  }
}

allprojects {
  repositories {
    google()
    jcenter()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
    maven("https://dl.bintray.com/kotlin/kotlinx")
    maven("https://plugins.gradle.org/m2/")
    maven("https://dl.bintray.com/kotlin/ktor")
    maven("https://dl.bintray.com/cfraser/muirwik")
    maven("https://dl.bintray.com/kotlin/exposed")
    maven("https://dl.bintray.com/badoo/maven")
    maven("https://dl.bintray.com/arkivanov/maven")
  }
}
