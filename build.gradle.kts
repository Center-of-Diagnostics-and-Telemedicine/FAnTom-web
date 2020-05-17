plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version "1.3.72"
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://dl.bintray.com/badoo/maven")
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://dl.bintray.com/kotlin/kotlinx")
        maven("https://plugins.gradle.org/m2/")
        maven("https://dl.bintray.com/badoo/maven")
        maven("https://dl.bintray.com/kotlin/ktor")
        maven("https://dl.bintray.com/arkivanov/maven")
        maven("https://dl.bintray.com/kotlin/kotlin-js-wrappers")
        maven("https://dl.bintray.com/cfraser/muirwik")
        maven("https://dl.bintray.com/kotlin/exposed")
    }
}


//plugins {
//    id("org.jetbrains.kotlin.multiplatform") version "1.3.72" apply false
//    id("kotlinx-serialization") version "1.3.72" apply false
//}
//
//allprojects {
//
//    buildscript {
//        repositories {
//            mavenCentral()
//            jcenter()
//            google()
//            maven { url "https://dl.bintray.com/kotlin/kotlin-dev" }
//            maven { url "https://dl.bintray.com/kotlin/kotlin-eap" }
//            maven { url "https://dl.bintray.com/kotlin/kotlinx" }
//            maven { url "https://plugins.gradle.org/m2/" }
//            maven { url "https://dl.bintray.com/badoo/maven" }
//            maven { url "https://dl.bintray.com/kotlin/ktor" }
//            maven { url "https://dl.bintray.com/arkivanov/maven" }
//            maven { url "https://dl.bintray.com/kotlin/kotlin-js-wrappers" }
//            maven { url "https://dl.bintray.com/cfraser/muirwik" }
//            maven { url "https://dl.bintray.com/kotlin/kotlin-js-wrappers" }
//            maven { url "https://dl.bintray.com/kotlin/exposed" }
//        }
//    }
//
//    repositories {
//        mavenCentral()
//        jcenter()
//        google()
//        maven { url "https://dl.bintray.com/kotlin/kotlin-dev" }
//        maven { url "https://dl.bintray.com/kotlin/kotlin-eap" }
//        maven { url "https://dl.bintray.com/kotlin/kotlinx" }
//        maven { url "https://plugins.gradle.org/m2/" }
//        maven { url "https://dl.bintray.com/badoo/maven" }
//        maven { url "https://dl.bintray.com/kotlin/ktor" }
//        maven { url "https://dl.bintray.com/arkivanov/maven" }
//        maven { url "https://dl.bintray.com/kotlin/kotlin-js-wrappers" }
//        maven { url "https://dl.bintray.com/cfraser/muirwik" }
//        maven { url "https://dl.bintray.com/kotlin/exposed" }
//    }
//
//}
