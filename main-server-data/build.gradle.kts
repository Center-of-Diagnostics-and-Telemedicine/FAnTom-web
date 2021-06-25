plugins {
  kotlin("jvm")
}

version = "unspecified"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))

  implementation(Deps.Jetbrains.Exposed.Core)
  implementation(Deps.Jetbrains.Exposed.Dao)
  implementation(Deps.Jetbrains.Exposed.Jdbc)
  implementation(Deps.Jetbrains.Exposed.Time)

  implementation(Deps.FlyWay.Core)

  implementation(Deps.Ktor.Client.Apache) // todo(check if this really needed)
  implementation(Deps.Ktor.Client.Logging)
  implementation(Deps.Ktor.Client.Serialization.Jvm)
  implementation(Deps.Ktor.Client.Json.Jvm)

  implementation(Deps.Ktor.Auth.Core)

  implementation(Deps.Docker.Java)
  implementation(Deps.Docker.TransportJersey)

  implementation(Deps.Badoo.Reaktive.Jvm)

  api(project(":api-models"))
  api(project(":backend-models"))
  api(project(":main-server-domain"))
}