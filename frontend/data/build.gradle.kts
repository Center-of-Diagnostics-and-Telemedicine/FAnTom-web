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
        implementation(Deps.Badoo.Reaktive.Reaktive)
      }
    }
    jsMain {
      dependencies {
        implementation(Deps.Ktor.Client.Js)
        implementation(Deps.Ktor.Client.Serialization.Js)
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
