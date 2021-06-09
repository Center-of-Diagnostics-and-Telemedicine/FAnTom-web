buildTargets = setOf(BuildTarget.Js)

setupMultiplatform()

kotlinCompat {
  sourceSets {
    commonMain {
      dependencies {
        implementation(Deps.MVIKotlin.Core)
        implementation(Deps.Badoo.Reaktive.Utils)
        implementation(Deps.Badoo.Reaktive.Reaktive)
        api(project(":api-models"))
      }
    }
  }
}
