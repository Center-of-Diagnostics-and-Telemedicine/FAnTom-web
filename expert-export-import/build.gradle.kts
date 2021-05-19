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

  implementation(Deps.Logback.Classic)

  implementation(Deps.Mysql.Connector)

  implementation(Deps.Ktor.Auth.Core)

  implementation(Deps.FlyWay.Core)

  implementation(project(":api-models"))
  implementation(project(":backend-models"))
  implementation(project(":main-server-domain"))
  implementation(project(":main-server-data"))
}