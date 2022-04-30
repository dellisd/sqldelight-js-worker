plugins {
    kotlin("multiplatform") version "1.6.20"
    id("app.cash.sqldelight") version "2.1.0-SNAPSHOT"
}

sqldelight {
    database("MyDatabase") {
        packageName = "ca.derekellis.db"
        generateAsync = true
    }
}

group = "ca.derekellis"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.2")
                implementation("app.cash.sqldelight:runtime:2.1.0-SNAPSHOT")
                implementation("app.cash.sqldelight:sqljs-driver:2.1.0-SNAPSHOT")
                implementation(npm("sql.js", "1.6.2"))
                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
            }
        }
    }
}