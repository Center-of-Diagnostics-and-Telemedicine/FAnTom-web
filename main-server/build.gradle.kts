import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  application
  id("java")
  id("org.jetbrains.kotlin.jvm")
  id("kotlinx-serialization")
  id("application")
  id("distribution")
  id("com.github.johnrengelman.shadow") version "5.0.0"
}

val mMainClassName = "MainServerKt"

application {
  mainClassName = mMainClassName
}

tasks.shadowJar {
  manifest.attributes["Main-class"] = mMainClassName
}

dependencies {
//  implementation(Deps.Jetbrains.Kotlinx.Serialization.Json.Core)
  implementation(Deps.Jetbrains.Kotlin.StdLib.Jdk8)

  implementation(Deps.Jetbrains.Exposed.Core)
  implementation(Deps.Jetbrains.Exposed.Dao)
  implementation(Deps.Jetbrains.Exposed.Jdbc)

  implementation(Deps.Ktor.Server.Netty)
  implementation(Deps.Ktor.Client.Apache)

  implementation(Deps.Ktor.Client.Serialization.Common)
  implementation(Deps.Ktor.Client.Serialization.Jvm)
  implementation(Deps.Ktor.Client.Gson)
  implementation(Deps.Ktor.Client.Json.Jvm)

  implementation(Deps.Ktor.Client.HtmlBuilder)
  implementation(Deps.Ktor.Client.Logging.Jvm)

  implementation(Deps.Ktor.Client.Auth.Core)
  implementation(Deps.Ktor.Client.Auth.Jwt)

  implementation(Deps.Ktor.Client.Locations)
  implementation(Deps.Badoo.Reaktive.Jvm)

  implementation("ch.qos.logback:logback-classic:1.2.3")
  implementation("mysql:mysql-connector-java:5.1.46")
  api(project(":api-models"))
  implementation("org.flywaydb:flyway-core:6.2.0")
  compile("com.github.docker-java:docker-java:3.2.1")
  implementation("commons-cli:commons-cli:1.4")
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>().all {
  kotlinOptions.jvmTarget = "1.8"
}

tasks.register<Delete>("cleanStatic") {
  delete("/resources/static")
}

tasks.register<Copy>("deployScript") {
  dependsOn("cleanStatic")
  from("../frontend/js/build/distributions")
  into("src/main/resources/static/static/js")
}

tasks.register<Copy>("deployScript") {
  dependsOn("deployScript")
  from("../frontend/js/build/distributions")
  into("src/main/resources/static")
}
tasks.processResources {
  dependsOn("deployStatic")
}
tasks.clean {
  dependsOn("cleanStatic")
}
