pluginManagement {
  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "kotlinx-serialization") {
        useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
      }
    }
  }
  repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-dev")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://plugins.gradle.org/m2/")
    mavenCentral()
  }
}

rootProject.name = "FantomWeb"

enableFeaturePreview("GRADLE_METADATA")

include("main-server")
include("api-models")
include("frontend:domain-internal")
include("frontend:domain")
include("frontend:presentation")
include("frontend:js")
include("frontend:data")
include("backend-models")
include("main-server-domain")
include("main-server-data")
