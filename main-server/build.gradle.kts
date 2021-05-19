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
    val ktor_version = "1.3.2"
    val logbackVersion = "1.2.3"
    val reaktiveVersion = "1.1.13"

    implementation(kotlin("stdlib"))

    implementation("org.jetbrains.exposed:exposed-core:0.22.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.22.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.22.1")

    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")

    implementation("io.ktor:ktor-client-apache:$ktor_version")
    implementation("io.ktor:ktor-client-serialization-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-logging-jvm:$ktor_version")

    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")

    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")

    implementation("io.ktor:ktor-locations:$ktor_version")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("mysql:mysql-connector-java:5.1.46")

    implementation(project(":api-models"))
    implementation(project(":backend-models"))
    implementation(project(":main-server-domain"))
    implementation(project(":main-server-data"))

    implementation("org.flywaydb:flyway-core:6.2.0")
    compile("com.github.docker-java:docker-java:3.2.1")
    implementation("com.badoo.reaktive:reaktive-jvm:$reaktiveVersion")
    // https://mvnrepository.com/artifact/commons-cli/commons-cli
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
