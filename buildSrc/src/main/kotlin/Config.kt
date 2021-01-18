
import com.android.build.gradle.BaseExtension
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

enum class BuildType {
  ALL, METADATA, NON_NATIVE, ANDROID, JVM, JS, LINUX, IOS, MAC_OS
}

val ExtensionAware.buildType: BuildType
  get() =
    find("build_type")
      ?.toString()
      ?.let(BuildType::valueOf)
      ?: BuildType.ALL

private fun ExtensionAware.find(key: String) =
  if (extra.has(key)) extra.get(key) else null

interface BuildTarget {

  interface NonNative : BuildTarget

  interface Native : BuildTarget

  interface Darwin : Native

  interface Ios : Darwin

  interface Linux : Native

  object Android : NonNative
  object Jvm : NonNative
  object Js : NonNative
  object IosX64 : Ios
  object IosArm64 : Ios
  object MacOsX64 : Darwin
  object LinuxX64 : Linux

}

private val ALL_BUILD_TARGETS =
  setOf(
    BuildTarget.Android,
    BuildTarget.Jvm,
    BuildTarget.Js
//    BuildTarget.IosX64,
//    BuildTarget.IosArm64,
//    BuildTarget.MacOsX64,
//    BuildTarget.LinuxX64
  )

private val BUILD_TYPE_TO_BUILD_TARGETS: Map<BuildType, Set<BuildTarget>> =
  mapOf(
    BuildType.ALL to ALL_BUILD_TARGETS,
    BuildType.METADATA to ALL_BUILD_TARGETS,
    BuildType.NON_NATIVE to setOf(BuildTarget.Android, BuildTarget.Jvm, BuildTarget.Js),
    BuildType.ANDROID to setOf(BuildTarget.Android),
    BuildType.JVM to setOf(BuildTarget.Jvm),
    BuildType.JS to setOf(BuildTarget.Js)
//    BuildType.LINUX to setOf(BuildTarget.LinuxX64),
//    BuildType.IOS to setOf(BuildTarget.IosX64, BuildTarget.IosArm64),
//    BuildType.MAC_OS to setOf(BuildTarget.MacOsX64)
  )

val BuildType.buildTargets: Set<BuildTarget> get() = requireNotNull(BUILD_TYPE_TO_BUILD_TARGETS[this])

@Suppress("UNCHECKED_CAST")
var ExtensionAware.buildTargets: Set<BuildTarget>
  get() = if (extra.has("project_build_targets")) extra["project_build_targets"] as Set<BuildTarget> else ALL_BUILD_TARGETS
  set(value) {
    extra["project_build_targets"] = value
  }

inline fun <reified T : BuildTarget> ExtensionAware.isBuildTargetAvailable(): Boolean =
  buildType.buildTargets.any { it is T } && buildTargets.any { it is T }

inline fun <reified T : BuildTarget> ExtensionAware.doIfBuildTargetAvailable(block: () -> Unit) {
  if (isBuildTargetAvailable<T>()) {
    block()
  }
}

val Project.isAnyTargetAvailable: Boolean
  get() = buildType.buildTargets.any { it in buildTargets }


fun Project.setupMultiplatform() {
  plugins.apply("kotlin-multiplatform")
  plugins.apply("kotlinx-serialization")

  kotlinCompat {
    doIfBuildTargetAvailable<BuildTarget.Js> {
      js {
        nodejs()
        browser {
        }

        compilations.all {
          compileKotlinTask.kotlinOptions {
            metaInfo = true
            sourceMap = true
            sourceMapEmbedSources = "always"
            moduleKind = "umd"
            main = "call"
          }
        }
      }
    }

    doIfBuildTargetAvailable<BuildTarget.Jvm> {
      jvm()
    }

    sourceSets {
      commonMain {
        dependencies {
          implementation(Deps.Jetbrains.Kotlin.StdLib.Common)
          implementation(Deps.Jetbrains.Kotlinx.Serialization.Json.Core)
        }
      }

      commonTest {
        dependencies {
          implementation(Deps.Jetbrains.Kotlin.Test.Common)
          implementation(Deps.Jetbrains.Kotlin.TestAnnotations.Common)
        }
      }

      jsNativeCommonMain.dependsOn(commonMain)
      jsNativeCommonTest.dependsOn(commonTest)

      jvmCommonMain {
        dependsOn(commonMain)

        dependencies {
          implementation(Deps.Jetbrains.Kotlin.StdLib.Jdk7)
          implementation(Deps.Jetbrains.Kotlinx.Serialization.Json.Core)
        }
      }

      jvmCommonTest {
        dependsOn(commonTest)

        dependencies {
          implementation(Deps.Jetbrains.Kotlin.Test.Junit)
        }
      }

      jvmMain.dependsOn(jvmCommonMain)
      jvmTest.dependsOn(jvmCommonTest)

      jsMain {
        dependsOn(jsNativeCommonMain)

        dependencies {
          implementation(Deps.Jetbrains.Kotlin.StdLib.Js)
//          implementation(Deps.Jetbrains.Kotlinx.Serialization.Runtime.Js)
        }
      }

      jsTest {
        dependsOn(jsNativeCommonTest)

        dependencies {
          implementation(Deps.Jetbrains.Kotlin.Test.Js)
        }
      }
    }
  }
}

fun KotlinMultiplatformExtension.jsCompat(): KotlinJsTarget? = findTargetByName("js")

fun KotlinMultiplatformExtension.jsCompat(block: KotlinJsTarget.() -> Unit) {
  jsCompat()?.apply(block)
}

fun KotlinMultiplatformExtension.androidCompat(): KotlinAndroidTarget? = findTargetByName("android")

fun KotlinMultiplatformExtension.androidCompat(block: KotlinAndroidTarget.() -> Unit) {
  androidCompat()?.apply(block)
}

fun KotlinMultiplatformExtension.jvmCompat(): KotlinJvmTarget? = findTargetByName("jvm")

fun KotlinMultiplatformExtension.jvmCompat(block: KotlinJvmTarget.() -> Unit) {
  jvmCompat()?.apply(block)
}

fun KotlinMultiplatformExtension.linuxX64Compat(): KotlinNativeTarget? = findTargetByName("linuxX64")

fun KotlinMultiplatformExtension.linuxX64Compat(block: KotlinNativeTarget.() -> Unit) {
  linuxX64Compat()?.also(block)
}

fun KotlinMultiplatformExtension.iosX64Compat(): KotlinNativeTarget? = findTargetByName("iosX64")

fun KotlinMultiplatformExtension.iosX64Compat(block: KotlinNativeTarget.() -> Unit) {
  iosX64Compat()?.apply(block)
}

fun KotlinMultiplatformExtension.iosArm64Compat(): KotlinNativeTarget? = findTargetByName("iosArm64")

fun KotlinMultiplatformExtension.iosArm64Compat(block: KotlinNativeTarget.() -> Unit) {
  iosArm64Compat()?.apply(block)
}

fun KotlinMultiplatformExtension.macosX64Compat(): KotlinNativeTarget? = findTargetByName("macosX64")

fun KotlinMultiplatformExtension.macosX64Compat(block: KotlinNativeTarget.() -> Unit) {
  macosX64Compat()?.also(block)
}

private inline fun <reified T : KotlinTarget> KotlinMultiplatformExtension.findTargetByName(name: String): T? =
  targets.findByName(name) as T?

fun Project.androidCompat(block: BaseExtension.() -> Unit) {
  if (isBuildTargetAvailable<BuildTarget.Android>()) {
    extensions.getByType<BaseExtension>().block()
  }
}

fun Project.kotlinCompat(block: KotlinMultiplatformExtension.() -> Unit) {
  if (isAnyTargetAvailable) {
    extensions.getByType<KotlinMultiplatformExtension>().block()
  }
}

typealias SourceSets = NamedDomainObjectContainer<KotlinSourceSet>

fun KotlinMultiplatformExtension.sourceSets(block: SourceSets.() -> Unit) {
  sourceSets.block()
}

private fun SourceSets.getOrCreate(name: String): KotlinSourceSet = findByName(name) ?: create(name)

// common

val SourceSets.commonMain: KotlinSourceSet get() = getOrCreate("commonMain")

fun SourceSets.commonMain(block: KotlinSourceSet.() -> Unit) {
  commonMain.apply(block)
}

val SourceSets.commonTest: KotlinSourceSet get() = getOrCreate("commonTest")

fun SourceSets.commonTest(block: KotlinSourceSet.() -> Unit) {
  commonTest.apply(block)
}

// jsNativeCommon

val SourceSets.jsNativeCommonMain: KotlinSourceSet get() = getOrCreate("jsNativeCommonMain")

fun SourceSets.jsNativeCommonMain(block: KotlinSourceSet.() -> Unit) {
  jsNativeCommonMain.apply(block)
}

val SourceSets.jsNativeCommonTest: KotlinSourceSet get() = getOrCreate("jsNativeCommonTest")

fun SourceSets.jsNativeCommonTest(block: KotlinSourceSet.() -> Unit) {
  jsNativeCommonTest.apply(block)
}

// jvmCommon

val SourceSets.jvmCommonMain: KotlinSourceSet get() = getOrCreate("jvmCommonMain")

fun SourceSets.jvmCommonMain(block: KotlinSourceSet.() -> Unit) {
  jvmCommonMain.apply(block)
}

val SourceSets.jvmCommonTest: KotlinSourceSet get() = getOrCreate("jvmCommonTest")

fun SourceSets.jvmCommonTest(block: KotlinSourceSet.() -> Unit) {
  jvmCommonTest.apply(block)
}

// jvm

val SourceSets.jvmMain: KotlinSourceSet get() = getOrCreate("jvmMain")

fun SourceSets.jvmMain(block: KotlinSourceSet.() -> Unit) {
  jvmMain.apply(block)
}

val SourceSets.jvmTest: KotlinSourceSet get() = getOrCreate("jvmTest")

fun SourceSets.jvmTest(block: KotlinSourceSet.() -> Unit) {
  jvmTest.apply(block)
}

// android

val SourceSets.androidMain: KotlinSourceSet get() = getOrCreate("androidMain")

fun SourceSets.androidMain(block: KotlinSourceSet.() -> Unit) {
  androidMain.apply(block)
}

val SourceSets.androidTest: KotlinSourceSet get() = getOrCreate("androidTest")

fun SourceSets.androidTest(block: KotlinSourceSet.() -> Unit) {
  androidTest.apply(block)
}

// js

val SourceSets.jsMain: KotlinSourceSet get() = getOrCreate("jsMain")

fun SourceSets.jsMain(block: KotlinSourceSet.() -> Unit) {
  jsMain.apply(block)
}

val SourceSets.jsTest: KotlinSourceSet get() = getOrCreate("jsTest")

fun SourceSets.jsTest(block: KotlinSourceSet.() -> Unit) {
  jsTest.apply(block)
}

// nativeCommon

val SourceSets.nativeCommonMain: KotlinSourceSet get() = getOrCreate("nativeCommonMain")

fun SourceSets.nativeCommonMain(block: KotlinSourceSet.() -> Unit) {
  nativeCommonMain.apply(block)
}

val SourceSets.nativeCommonTest: KotlinSourceSet get() = getOrCreate("nativeCommonTest")

fun SourceSets.nativeCommonTest(block: KotlinSourceSet.() -> Unit) {
  nativeCommonTest.apply(block)
}

// linuxX64

val SourceSets.linuxX64Main: KotlinSourceSet get() = getOrCreate("linuxX64Main")

fun SourceSets.linuxX64Main(block: KotlinSourceSet.() -> Unit) {
  linuxX64Main.apply(block)
}

val SourceSets.linuxX64Test: KotlinSourceSet get() = getOrCreate("linuxX64Test")

fun SourceSets.linuxX64Test(block: KotlinSourceSet.() -> Unit) {
  linuxX64Test.apply(block)
}

// darwinCommon

val SourceSets.darwinCommonMain: KotlinSourceSet get() = getOrCreate("darwinCommonMain")

fun SourceSets.darwinCommonMain(block: KotlinSourceSet.() -> Unit) {
  darwinCommonMain.apply(block)
}

val SourceSets.darwinCommonTest: KotlinSourceSet get() = getOrCreate("darwinCommonTest")

fun SourceSets.darwinCommonTest(block: KotlinSourceSet.() -> Unit) {
  darwinCommonTest.apply(block)
}

// iosCommon

val SourceSets.iosCommonMain: KotlinSourceSet get() = getOrCreate("iosCommonMain")

fun SourceSets.iosCommonMain(block: KotlinSourceSet.() -> Unit) {
  iosCommonMain.apply(block)
}

val SourceSets.iosCommonTest: KotlinSourceSet get() = getOrCreate("iosCommonTest")

fun SourceSets.iosCommonTest(block: KotlinSourceSet.() -> Unit) {
  iosCommonTest.apply(block)
}

// iosX64

val SourceSets.iosX64Main: KotlinSourceSet get() = getOrCreate("iosX64Main")

fun SourceSets.iosX64Main(block: KotlinSourceSet.() -> Unit) {
  iosX64Main.apply(block)
}

val SourceSets.iosX64Test: KotlinSourceSet get() = getOrCreate("iosX64Test")

fun SourceSets.iosX64Test(block: KotlinSourceSet.() -> Unit) {
  iosX64Test.apply(block)
}

// iosArm64

val SourceSets.iosArm64Main: KotlinSourceSet get() = getOrCreate("iosArm64Main")

fun SourceSets.iosArm64Main(block: KotlinSourceSet.() -> Unit) {
  iosArm64Main.apply(block)
}

val SourceSets.iosArm64Test: KotlinSourceSet get() = getOrCreate("iosArm64Test")

fun SourceSets.iosArm64Test(block: KotlinSourceSet.() -> Unit) {
  iosArm64Test.apply(block)
}
