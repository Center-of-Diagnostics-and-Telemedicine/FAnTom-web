plugins {
  id("org.jetbrains.kotlin.js")
}

kotlin {
  target {
    useCommonJs()
    produceExecutable()
    browser {
      dceTask {
        keep("ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io")
      }
    }
  }
}

dependencies {
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

  implementation(npm("core-js", "2.6.5"))
  implementation(npm("svg-inline-loader", "0.8.0"))
  implementation(npm("abort-controller"))
  implementation(npm("react", Deps.reactVersion))
  implementation(npm("react-dom", Deps.reactVersion))
  implementation(npm("react-is", Deps.reactVersion))
  implementation(npm("inline-style-prefixer", "5.1.0"))
  implementation(npm("styled-components", "4.3.2"))
  implementation(npm("@material-ui/core", "4.9.14"))
  implementation(npm("@material-ui/icons", "4.9.1"))
}
