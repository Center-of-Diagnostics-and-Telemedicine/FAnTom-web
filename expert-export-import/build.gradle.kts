plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
    id("kotlinx-serialization")
}

version = "unspecified"

repositories {
    mavenCentral()
}

val ktor_version = "1.3.2"
val kotlinx_serialization_version = "0.20.0"
val logback_version = "1.2.3"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$kotlinx_serialization_version")
    
    implementation("org.jetbrains.exposed:exposed-core:0.22.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.22.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.22.1")
    
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("mysql:mysql-connector-java:5.1.46")

    implementation("io.ktor:ktor-auth:$ktor_version")

    implementation(project(":api-models"))
    implementation(project(":backend-models"))
    implementation(project(":main-server-domain"))
    implementation(project(":main-server-data"))
    implementation("org.flywaydb:flyway-core:6.2.0")
}