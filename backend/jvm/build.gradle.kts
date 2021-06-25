plugins {
    kotlin("jvm")
    id("application")
    id("distribution")
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

application {
    mainClass.set("MainServerKt")
}

//main_class_name =

//shadowJar {
//    manifest {
//        attributes "Main-Class": main_class_name
//    }
//    archiveBaseName = "main_server"
//}

dependencies {

    implementation(kotlin("stdlib"))

    implementation(Deps.Jetbrains.Exposed.Core)
    implementation(Deps.Jetbrains.Exposed.Dao)
    implementation(Deps.Jetbrains.Exposed.Jdbc)
    implementation(Deps.Jetbrains.Exposed.Time)

    implementation(Deps.Ktor.Server.Core)
    implementation(Deps.Ktor.Server.Netty)

    implementation(Deps.Ktor.Client.Apache) // todo(check if this really needed)
    implementation(Deps.Ktor.Client.Serialization.Jvm)
    implementation(Deps.Ktor.Client.Json.Jvm)
    implementation(Deps.Ktor.Client.Logging)

    implementation(Deps.Ktor.Auth.Core)
    implementation(Deps.Ktor.Auth.Jwt)

    implementation(Deps.Ktor.Gson)
    implementation(Deps.Ktor.Locations)

    implementation(Deps.Badoo.Reaktive.Jvm)

    implementation(Deps.Logback.Classic)

    implementation(Deps.Mysql.Connector)

    implementation(Deps.FlyWay.Core)

    implementation(Deps.Docker.Java)
    implementation(Deps.Docker.TransportJersey)

    implementation(project(":api-models"))
    implementation(project(":backend:domain"))
    implementation(project(":backend:data"))

    implementation("commons-cli:commons-cli:1.4")
}

//compileKotlin {
//    kotlinOptions.jvmTarget = "1.8"
//}
//
//application {
//    mainClassName = main_class_name
//}
//
//task cleanStatic(type: Delete) {
//    delete "/resources/static"
//}
//
//task deployScript(type: Copy, dependsOn: "cleanStatic") {
//    from "../frontend/js/build/distributions"
//    into "src/main/resources/static/static/js"
//}
//
//task deployStatic(type: Copy, dependsOn: "deployScript") {
//    from "../frontend/js/build/distributions"
//    into "src/main/resources/static"
//}
//
//processResources { dependsOn "deployStatic" }
//clean { dependsOn "cleanStatic" }
