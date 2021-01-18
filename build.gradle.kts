plugins {
  `kotlin-dsl`
  kotlin("plugin.serialization") version "1.4.20"
}

allprojects {
  repositories {
    google()
    jcenter()
    maven("https://dl.bintray.com/badoo/maven")
    maven("https://dl.bintray.com/kotlin/kotlin-dev")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://dl.bintray.com/kotlin/kotlinx")
    maven("https://plugins.gradle.org/m2/")
    maven("https://dl.bintray.com/badoo/maven")
    maven("https://dl.bintray.com/kotlin/ktor")
    maven("https://dl.bintray.com/arkivanov/maven")
    maven("https://dl.bintray.com/kotlin/kotlin-js-wrappers")
    maven("https://dl.bintray.com/cfraser/muirwik")
    maven("https://dl.bintray.com/kotlin/exposed")
  }
}
