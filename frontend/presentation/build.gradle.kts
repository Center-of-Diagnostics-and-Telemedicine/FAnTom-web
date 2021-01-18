setupMultiplatform()

kotlinCompat {
  sourceSets {
    commonMain {
      dependencies {
        implementation(Deps.MVIKotlin.Core)
        implementation(Deps.MVIKotlin.Reaktive)
        api(project(":frontend:domain"))
        implementation(project(":frontend:domain-internal"))
        implementation(Deps.Badoo.Reaktive.Core)
        implementation(Deps.Badoo.Reaktive.Utils)
        implementation(Deps.Badoo.Reaktive.CoroutinesInterop)
      }
    }

    commonTest {
      dependencies {
        api(project(":frontend:domain"))
        implementation(Deps.MVIKotlin.Main)
        implementation(Deps.Badoo.Reaktive.ReaktiveTesting)
      }
    }
  }
}
