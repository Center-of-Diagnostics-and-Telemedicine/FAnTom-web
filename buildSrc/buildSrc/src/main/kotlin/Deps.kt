/**
Copyright (c) 2021, Moscow Center for Diagnostics & Telemedicine
All rights reserved.
This file is licensed under BSD-3-Clause license. See LICENSE file for details.
 */

object Deps {

  const val kotlinVersion = "1.5.20"

  object Jetbrains {
    object Kotlin : Group(name = "org.jetbrains.kotlin") {

      object Plugin {
        object Gradle :
          Dependency(group = Kotlin, name = "kotlin-gradle-plugin", version = kotlinVersion)
//        object Serialization: Dependency(group = Kotlin, name = "plugin.serialization", version = _root_ide_package_.Deps.kotlinVersion)
      }

      object StdLib {
        object Common :
          Dependency(group = Kotlin, name = "kotlin-stdlib-common", version = kotlinVersion)

        object Jdk7 :
          Dependency(group = Kotlin, name = "kotlin-stdlib-jdk7", version = kotlinVersion)

        object Jvm : Dependency(group = Kotlin, name = "kotlin-stdlib-jdk8", version = "")
        object Js : Dependency(group = Kotlin, name = "kotlin-stdlib-js", version = kotlinVersion)
      }

      object Reflect : Dependency(group = Kotlin, name = "kotlin-reflect", version = kotlinVersion)

      object Test {
        object Common :
          Dependency(group = Kotlin, name = "kotlin-test-common", version = kotlinVersion)

        object Js : Dependency(group = Kotlin, name = "kotlin-test-js", version = kotlinVersion)
        object Junit :
          Dependency(group = Kotlin, name = "kotlin-test-junit", version = kotlinVersion)
      }

      object TestAnnotations {
        object Common :
          Dependency(
            group = Kotlin,
            name = "kotlin-test-annotations-common",
            version = kotlinVersion
          )
      }
    }

    object Wrappers : Group(name = "org.jetbrains.kotlin-wrappers") {
      private const val kotlinJsVersion = "pre.213-kotlin-$kotlinVersion"
      private const val kotlinReactVersion = "17.0.2-$kotlinJsVersion"
      private const val styledVersion = "5.3.0-$kotlinJsVersion"
      private const val extensionsVersion = "1.0.1-$kotlinJsVersion"

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
        private const val version = "1.5.0"

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
        private const val version = "1.2.1"

        object Json :
          Dependency(group = Kotlinx, name = "kotlinx-serialization-json", version = version)
      }
    }

    object Exposed : Group(name = "org.jetbrains.exposed") {
      private val version = "0.31.1"

      object Core : Dependency(group = Exposed, name = "exposed-core", version = version)
      object Dao : Dependency(group = Exposed, name = "exposed-dao", version = version)
      object Jdbc : Dependency(group = Exposed, name = "exposed-jdbc", version = version)
      object Time : Dependency(group = Exposed, name = "exposed-java-time", version = version)
    }
  }

  object Ktor : Group(name = "io.ktor") {
    private const val version = "1.6.0"

    object Gson : Dependency(group = Ktor, name = "ktor-gson", version = version)
    object Locations : Dependency(group = Ktor, name = "ktor-locations", version = version)

    object Auth {
      object Core : Dependency(group = Ktor, name = "ktor-auth", version = version)
      object Jwt : Dependency(group = Ktor, name = "ktor-auth-jwt", version = version)
    }

    object Client {

      object Common : Dependency(group = Ktor, name = "ktor-client-core", version = version)
      object Js : Dependency(group = Ktor, name = "ktor-client-js", version = version)
      object Apache : Dependency(group = Ktor, name = "ktor-client-apache", version = version)

      object Logging : Dependency(group = Ktor, name = "ktor-client-logging-jvm", version = version)

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

    object Server {
      object Core : Dependency(group = Ktor, name = "ktor-server-core", version = version)
      object Netty : Dependency(group = Ktor, name = "ktor-server-netty", version = version)
    }
  }

  object Badoo {
    object Reaktive : Group(name = "com.badoo.reaktive") {
      private const val version = "1.2.0"

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
    private const val version = "2.0.3"

    object Core : Dependency(group = MVIKotlin, name = "mvikotlin", version = version)
    object Main : Dependency(group = MVIKotlin, name = "mvikotlin-main", version = version)
    object TimeTravel :
      Dependency(group = MVIKotlin, name = "mvikotlin-timetravel", version = version)

    object Logging : Dependency(group = MVIKotlin, name = "mvikotlin-logging", version = version)
    object Rx : Dependency(group = MVIKotlin, name = "rx", version = version)
    object Reaktive :
      Dependency(group = MVIKotlin, name = "mvikotlin-extensions-reaktive", version = version)
  }

  object Decompose : Group(name = "com.arkivanov.decompose") {
    private const val version = "0.2.6"

    object Core : Dependency(group = Decompose, name = "decompose", version = version)
  }

  object Muirwik : Group(name = "com.ccfraser.muirwik") {
    private const val version = "0.8.0"

    object Components : Dependency(group = Muirwik, name = "muirwik-components", version = version)
  }

  object Logback : Group(name = "ch.qos.logback") {
    object Classic : Dependency(group = Logback, name = "logback-classic", version = "1.2.3")
  }

  object Mysql : Group(name = "mysql") {
    object Connector : Dependency(group = Mysql, name = "mysql-connector-java", version = "5.1.46")
  }

  object FlyWay : Group(name = "org.flywaydb") {
    object Core : Dependency(group = FlyWay, name = "flyway-core", version = "6.2.0")
  }

  object Docker : Group(name = "com.github.docker-java") {
    private const val version = "3.2.10"
    object Java : Dependency(group = Docker, name = "docker-java", version = version)
    object TransportJersey : Dependency(group = Docker, name = "docker-java-transport-jersey", version = version)
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
