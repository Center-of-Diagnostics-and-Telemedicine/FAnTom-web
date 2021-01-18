setupMultiplatform()

kotlinCompat {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Deps.MVIKotlin.Core)
                api(project(":frontend:domain"))
                implementation(Deps.Badoo.Reaktive.Utils)
            }
        }
    }
}
