plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm")
  id("kotlinx-serialization")

}

version = "unspecified"

repositories {
  mavenCentral()
}

val ktor_version = "1.3.2"
val kotlinx_serialization_version = "0.20.0"

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$kotlinx_serialization_version")

  implementation("org.jetbrains.exposed:exposed-core:0.22.1")
  implementation("org.jetbrains.exposed:exposed-dao:0.22.1")
  implementation("org.jetbrains.exposed:exposed-jdbc:0.22.1")
  implementation("io.ktor:ktor-auth:$ktor_version")
  api(project(":api-models"))
}