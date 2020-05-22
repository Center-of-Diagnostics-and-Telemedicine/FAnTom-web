pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
    }
    repositories {
        gradlePluginPortal()
        maven ("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

rootProject.name = "FantomWeb"

enableFeaturePreview("GRADLE_METADATA")

include("shared")
include("js-frontend")
include("main-server")
include("api-models")
include("library-server")
include("frontend:domain-internal")
include("frontend:domain")
include("frontend:presentation")
include("frontend:js")
include("frontend:data")

