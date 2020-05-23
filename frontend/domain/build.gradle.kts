setupMultiplatform()

kotlinProject {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Deps.MVIKotlin.Core)
                implementation(Deps.Badoo.Reaktive.Utils)
                api(project(":api-models"))
            }
        }
    }
}
