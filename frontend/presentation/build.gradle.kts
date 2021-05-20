buildTargets = setOf(BuildTarget.Js)

setupMultiplatform()

kotlinCompat {
  sourceSets {
    commonMain {
      dependencies {
        implementation(Deps.MVIKotlin.Core)
        implementation(Deps.MVIKotlin.Rx)
        implementation(Deps.MVIKotlin.Reaktive)
        api(project(":frontend:domain"))
        implementation(project(":frontend:domain-internal"))
        implementation(Deps.Badoo.Reaktive.Reaktive)
        implementation(Deps.Badoo.Reaktive.Utils)
        implementation(Deps.Badoo.Reaktive.Utils)
        implementation(Deps.Badoo.Reaktive.CoroutinesInterop)

        implementation("com.arkivanov.decompose:decompose:0.2.4")
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
