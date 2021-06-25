plugins {
  `kotlin-dsl`
  kotlin("plugin.serialization") version "1.5.20"
}

buildscript {
  repositories { mavenCentral() }

  dependencies {
    val kotlinVersion = "1.5.20"
    classpath(kotlin("serialization", version = kotlinVersion))
  }
}

allprojects {
  repositories {
    mavenCentral()
  }
}
