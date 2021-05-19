buildTargets = setOf(BuildTarget.Jvm)

setupMultiplatform()

kotlinCompat {
  sourceSets {
    commonMain {
      dependencies {
        implementation(Deps.Jetbrains.Kotlinx.Coroutines.Core)
        api(project(":api-models"))
        api(project(":backend-models"))
      }
    }
  }
}
