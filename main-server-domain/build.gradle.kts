plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm")
}

version = "unspecified"

repositories {
  mavenCentral()
}

dependencies {
  implementation(Deps.Jetbrains.Kotlin.StdLib.Jvm)
  implementation(Deps.Jetbrains.Kotlinx.Coroutines.Core)
  api(project(":api-models"))
  api(project(":backend-models"))
}