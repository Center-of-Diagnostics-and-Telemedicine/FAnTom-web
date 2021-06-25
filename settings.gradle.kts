rootProject.name = "FantomWeb"

include("main-server")
include("api-models")
include("frontend:domain-internal")
include("frontend:domain")
include("frontend:presentation")
doIfJsTargetAvailable {
  include("frontend:js")
}
include("frontend:data")
include("expert-export-import")
include("backend-models")
include("main-server-domain")
include("main-server-data")

enum class BuildType {
  ALL, METADATA, NON_NATIVE, ANDROID, JVM, JS, LINUX, IOS, MAC_OS
}

val ExtensionAware.buildType: BuildType
  get() =
    find("build_type")
      ?.toString()
      ?.let(BuildType::valueOf)
      ?: BuildType.ALL

fun ExtensionAware.find(key: String) =
  if (extra.has(key)) extra.get(key) else null

fun doIfJvmTargetAvailable(block: () -> Unit) {
  if (buildType in setOf(BuildType.ALL, BuildType.METADATA, BuildType.NON_NATIVE, BuildType.JVM)) {
    block()
  }
}

fun doIfAndroidTargetAvailable(block: () -> Unit) {
  if (buildType in setOf(
      BuildType.ALL,
      BuildType.METADATA,
      BuildType.NON_NATIVE,
      BuildType.ANDROID
    )
  ) {
    block()
  }
}

fun doIfJsTargetAvailable(block: () -> Unit) {
  if (buildType in setOf(BuildType.ALL, BuildType.METADATA, BuildType.NON_NATIVE, BuildType.JS)) {
    block()
  }
}