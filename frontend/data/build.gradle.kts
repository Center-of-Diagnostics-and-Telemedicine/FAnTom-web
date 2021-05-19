buildTargets = setOf(BuildTarget.Js)

setupMultiplatform()

kotlinCompat {

  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":frontend:domain"))
        implementation(Deps.Ktor.Client.Common)
        implementation(Deps.Ktor.Client.Json.Common)
        implementation(Deps.Ktor.Client.Serialization.Common)
        implementation(Deps.Badoo.Reaktive.Utils)
      }
    }
    jsMain {
      dependencies {
        implementation(Deps.Ktor.Client.Js)
        implementation(Deps.Ktor.Client.Json.Js)
        implementation(Deps.Ktor.Client.Serialization.Js)

        implementation(npm("text-encoding", "0.7.0"))
      }
    }
    jvmMain {
      dependencies {
        implementation(Deps.Ktor.Client.Common)
        implementation(Deps.Ktor.Client.Json.Jvm)
        implementation(Deps.Ktor.Client.Serialization.Jvm)

      }
    }
  }
}
