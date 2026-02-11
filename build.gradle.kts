val koin_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktor_version = "3.0.3" // Pastikan versi ini konsisten

plugins {
    // Gunakan versi 2.0.x atau 2.1.x yang stabil
    kotlin("jvm") version "2.1.0"
    id("io.ktor.plugin") version "3.0.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
}

group = "org.delcom"
version = "0.0.1"

application {
    // Hapus ".kt", cukup nama class aslinya
    mainClass.set("org.delcom.ApplicationKt")
}

dependencies {
    // Core Ktor & Netty
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")

    // --- TAMBAHKAN INI (Yang bikin error tadi) ---
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    // ---------------------------------------------

    // Serialization
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")

    // Koin
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    // Others
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-config-yaml-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    testImplementation("io.ktor:ktor-server-test-host-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}