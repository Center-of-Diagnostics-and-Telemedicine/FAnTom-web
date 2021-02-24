plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm")
}

version = "unspecified"

repositories {
  mavenCentral()
}

val ktor_version = "1.3.2"
val reaktive_version = "1.1.19"

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib")

  implementation("org.jetbrains.exposed:exposed-core:0.22.1")
  implementation("org.jetbrains.exposed:exposed-dao:0.22.1")
  implementation("org.jetbrains.exposed:exposed-jdbc:0.22.1")
  implementation("org.flywaydb:flyway-core:6.2.0")

  implementation("io.ktor:ktor-client-apache:$ktor_version")
  implementation("io.ktor:ktor-client-logging-jvm:$ktor_version")
  implementation(Deps.Ktor.Client.Serialization.Jvm)
  implementation(Deps.Ktor.Client.Json.Jvm)
  implementation("io.ktor:ktor-auth:$ktor_version")

  implementation("com.github.docker-java:docker-java:3.2.1")

  implementation(Deps.Badoo.Reaktive.Jvm)

  api(project(":api-models"))
  api(project(":backend-models"))
  api(project(":main-server-domain"))
}