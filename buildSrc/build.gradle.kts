plugins {
  `kotlin-dsl`
}

repositories {
  google()
  jcenter()
  maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
  implementation(Deps.Jetbrains.Kotlin.Plugin.Gradle)
  implementation(Deps.Android.Tools.Build.Gradle)
}

kotlin {
  // Add Deps to compilation, so it will become available in main project
  sourceSets.getByName("main").kotlin.srcDir("buildSrc/src/main/kotlin")
}
