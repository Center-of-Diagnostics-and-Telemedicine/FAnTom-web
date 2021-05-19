plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm")
  id("kotlinx-serialization")

}

version = "unspecified"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))

  implementation(Deps.Jetbrains.Kotlinx.Serialization.Json)

  implementation(Deps.Jetbrains.Exposed.Core)
  implementation(Deps.Jetbrains.Exposed.Dao)
  implementation(Deps.Jetbrains.Exposed.Jdbc)

  implementation(Deps.Ktor.Auth.Core)

  api(project(":api-models"))
}