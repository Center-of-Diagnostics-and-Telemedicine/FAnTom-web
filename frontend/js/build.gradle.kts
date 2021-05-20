plugins {
  kotlin("js")
}

kotlin {
  js(LEGACY) {
    useCommonJs()
    browser {
      webpackTask {
        cssSupport.enabled = true
      }

      runTask {
        cssSupport.enabled = true
      }

      testTask {
        useKarma {
          useChromeHeadless()
          webpackConfig.cssSupport.enabled = true
        }
      }
    }
  }
}

dependencies {
  implementation(Deps.Jetbrains.Kotlin.StdLib.Js)
  implementation(Deps.Jetbrains.Wrappers.React.Core)
  implementation(Deps.Jetbrains.Wrappers.React.ReactDom)
  implementation(Deps.Jetbrains.Wrappers.Styled)
  implementation(Deps.Jetbrains.Wrappers.Extensions)

  implementation(Deps.Muirwik.Components)

  implementation(Deps.MVIKotlin.Core)
  implementation(Deps.MVIKotlin.Main)
  implementation(Deps.MVIKotlin.TimeTravel)
  implementation(Deps.MVIKotlin.Logging)
  implementation(Deps.MVIKotlin.Rx)
  implementation(Deps.Badoo.Reaktive.Reaktive)
  implementation(project(":frontend:data"))
  implementation(project(":frontend:presentation"))

  implementation("com.arkivanov.decompose:decompose:0.2.4")

  implementation(npm("@material-ui/core", "^4.11.0"))
  implementation(npm("@material-ui/lab", "4.0.0-alpha.56"))
  implementation(npm("@material-ui/icons", "^4.9.1"))
}
