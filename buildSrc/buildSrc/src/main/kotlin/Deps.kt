object Deps {

  const val reactVersion = "16.13.0"

  object Jetbrains {
    object Kotlin : Group(name = "org.jetbrains.kotlin") {
      private const val version = "1.4-M1"

      object Plugin {
        object Gradle : Dependency(group = Kotlin, name = "kotlin-gradle-plugin", version = version)
//        object Serialization: Dependency(group = Kotlin, name = "plugin.serialization", version = version)
      }

      object StdLib {
        object Common : Dependency(group = Kotlin, name = "kotlin-stdlib-common", version = version)
        object Jdk7 : Dependency(group = Kotlin, name = "kotlin-stdlib-jdk7", version = version)
        object Jvm : Dependency(group = Kotlin, name = "kotlin-stdlib-jdk8", version = "")
        object Js : Dependency(group = Kotlin, name = "kotlin-stdlib-js", version = version)
      }

      object Reflect : Dependency(group = Kotlin, name = "kotlin-reflect", version = version)

      object Test {
        object Common : Dependency(group = Kotlin, name = "kotlin-test-common", version = version)
        object Js : Dependency(group = Kotlin, name = "kotlin-test-js", version = version)
        object Junit : Dependency(group = Kotlin, name = "kotlin-test-junit", version = version)
      }

      object TestAnnotations {
        object Common :
          Dependency(group = Kotlin, name = "kotlin-test-annotations-common", version = version)
      }
    }

    object Wrappers : Group(name = "org.jetbrains") {
      private const val kotlinVersion = "pre.94-kotlin-1.3.70"
      private const val kotlinReactVersion = "$reactVersion-$kotlinVersion"
      private const val styledVersion = "1.0.0-$kotlinVersion"
      private const val extensionsVersion = "1.0.1-$kotlinVersion"

      object React {
        object Core :
          Dependency(group = Wrappers, name = "kotlin-react", version = kotlinReactVersion)

        object ReactDom :
          Dependency(group = Wrappers, name = "kotlin-react-dom", version = kotlinReactVersion)
      }

      object Styled : Dependency(group = Wrappers, name = "kotlin-styled", version = styledVersion)
      object Extensions :
        Dependency(group = Wrappers, name = "kotlin-extensions", version = extensionsVersion)
    }

    object Kotlinx : Group(name = "org.jetbrains.kotlinx") {
      object Coroutines {
        private const val version = "1.3.5-1.4-M1"

        object Core :
          Dependency(group = Kotlinx, name = "kotlinx-coroutines-core", version = version) {
          object Common :
            Dependency(group = Kotlinx, name = "kotlinx-coroutines-core-common", version = version)

          object Native :
            Dependency(group = Kotlinx, name = "kotlinx-coroutines-core-native", version = version)

          object Js :
            Dependency(group = Kotlinx, name = "kotlinx-coroutines-core-js", version = version)
        }

        object Android :
          Dependency(group = Kotlinx, name = "kotlinx-coroutines-android", version = version)
      }

      object Serialization {
        private const val version = "0.20.0-1.4-M1"

        object Runtime {
          object Core :
            Dependency(group = Kotlinx, name = "kotlinx-serialization-runtime", version = version)

          object Common :
            Dependency(
              group = Kotlinx,
              name = "kotlinx-serialization-runtime-common",
              version = version
            )

          object Native :
            Dependency(
              group = Kotlinx,
              name = "kotlinx-serialization-runtime-native",
              version = version
            )

          object Js :
            Dependency(
              group = Kotlinx,
              name = "kotlinx-serialization-runtime-js",
              version = version
            )
        }
      }
    }
  }

  object Ktor : Group(name = "io.ktor") {
    private const val version = "1.3.2-1.4-M1"

    object Client {

      object Common : Dependency(group = Ktor, name = "ktor-client-core", version = version)
      object Js : Dependency(group = Ktor, name = "ktor-client-js", version = version)

      object Json {
        object Common : Dependency(group = Ktor, name = "ktor-client-json", version = version)
        object Jvm : Dependency(group = Ktor, name = "ktor-client-json-jvm", version = version)
        object Js : Dependency(group = Ktor, name = "ktor-client-json-js", version = version)
      }

      object Serialization {
        object Common :
          Dependency(group = Ktor, name = "ktor-client-serialization", version = version)

        object Jvm :
          Dependency(group = Ktor, name = "ktor-client-serialization-jvm", version = version)

        object Js :
          Dependency(group = Ktor, name = "ktor-client-serialization-js", version = version)
      }
    }
  }

  object Badoo {
    object Reaktive : Group(name = "com.badoo.reaktive") {
      private const val version = "1.1.19"

      object Reaktive : Dependency(group = Badoo.Reaktive, name = "reaktive", version = version)
      object Jvm : Dependency(group = Badoo.Reaktive, name = "reaktive-jvm", version = version)
      object ReaktiveAnnotations :
        Dependency(group = Badoo.Reaktive, name = "reaktive-annotations", version = version)

      object CoroutinesInterop :
        Dependency(group = Badoo.Reaktive, name = "coroutines-interop", version = version)

      object ReaktiveTesting :
        Dependency(group = Badoo.Reaktive, name = "reaktive-testing", version = version)

      object Utils : Dependency(group = Badoo.Reaktive, name = "utils", version = version)
    }
  }

  object MVIKotlin : Group(name = "com.arkivanov.mvikotlin") {
    private const val version = "2.0.0"

    object Core : Dependency(group = MVIKotlin, name = "mvikotlin", version = version)
    object Main : Dependency(group = MVIKotlin, name = "mvikotlin-main", version = version)
    object TimeTravel :
      Dependency(group = MVIKotlin, name = "mvikotlin-timetravel", version = version)

    object Logging : Dependency(group = MVIKotlin, name = "mvikotlin-logging", version = version)
    object Rx : Dependency(group = MVIKotlin, name = "rx", version = version)
    object Reaktive :
      Dependency(group = MVIKotlin, name = "mvikotlin-extensions-reaktive", version = version)
  }

  object Muirwik : Group(name = "com.ccfraser.muirwik") {
    private const val version = "0.4.1"

    object Components : Dependency(group = Muirwik, name = "muirwik-components", version = version)
  }

  object TouchLab : Group(name = "co.touchlab") {
    object KotlinXcodeSync : Dependency(group = TouchLab, name = "kotlinxcodesync", version = "0.2")
  }

  object Android {
    object Tools {
      object Build : Group(name = "com.android.tools.build") {
        object Gradle : Dependency(group = Build, name = "gradle", version = "3.6.0")
      }
    }
  }

  object AndroidX {
    object Core : Group(name = "androidx.core") {
      object Ktx : Dependency(group = Core, name = "core-ktx", version = "1.1.0")
    }

    object AppCompat : Group(name = "androidx.appcompat") {
      object AppCompat :
        Dependency(group = AndroidX.AppCompat, name = "appcompat", version = "1.1.0")
    }

    object RecyclerView : Group(name = "androidx.recyclerview") {
      object RecyclerView :
        Dependency(group = AndroidX.RecyclerView, name = "recyclerview", version = "1.1.0")
    }

    object ConstraintLayout : Group(name = "androidx.constraintlayout") {
      object ConstraintLayout :
        Dependency(group = AndroidX.ConstraintLayout, name = "constraintlayout", version = "1.1.3")
    }

    object DrawerLayout : Group(name = "androidx.drawerlayout") {
      object DrawerLayout :
        Dependency(group = AndroidX.DrawerLayout, name = "drawerlayout", version = "1.0.0")
    }

    object Lifecycle : Group(name = "androidx.lifecycle") {
      object LifecycleCommonJava8 :
        Dependency(group = Lifecycle, name = "lifecycle-common-java8", version = "2.2.0")
    }
  }

  open class Group(val name: String)

  open class Dependency private constructor(
    private val notation: String
  ) : CharSequence by notation {
    constructor(group: Group, name: String, version: String) : this("${group.name}:$name:$version")

    override fun toString(): String = notation
  }
}
