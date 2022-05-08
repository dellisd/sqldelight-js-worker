plugins {
    kotlin("multiplatform") version "1.7.0"
    id("app.cash.sqldelight") version "2.0.0-alpha03"
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
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}

kotlin {
    js(IR) {
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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
                implementation("app.cash.sqldelight:sqljs-driver:2.0.0-alpha03")
                implementation(npm("@jlongster/sql.js", "1.6.7"))
                implementation(npm("absurd-sql", "0.0.53"))
                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
            }
        }
    }
}