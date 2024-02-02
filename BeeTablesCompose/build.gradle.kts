/*
 * Copyright 2023 Breens Mbaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.multiplatform)

    alias(libs.plugins.compose)

    id("maven-publish")
}

kotlin {
    kotlin.applyDefaultHierarchyTemplate()

    androidTarget()

    /*val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        when {
            System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
            System.getenv("NATIVE_ARCH")?.startsWith("arm") == true -> ::iosSimulatorArm64
            else -> ::iosX64
        }
    iosTarget("ios") {}

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "https://github.com/Breens-Mbaka/BeeTablesCompose"
        version = "1.0.2"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "BeeTablesCompose"
            isStatic = false
        }
    }*/

    sourceSets {
        sourceSets["commonMain"].dependencies {
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.uiTooling)
            implementation(compose.material3)
            implementation(kotlin("reflect"))
        }

        sourceSets["commonTest"].dependencies {
            implementation(kotlin("test"))
        }

        sourceSets["androidInstrumentedTest"].dependencies {
            implementation(libs.androidX.jUnit)
            implementation(libs.espresso)
        }

        // sourceSets["androidUnitTest"].dependencies {}

    }
}

android {
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    namespace = "com.breens.beetablescompose"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    buildTypes {
        getByName("debug") {}

        getByName("release") {
            isMinifyEnabled = false
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.breens.beetablescompose"
                artifactId = "bee-tables-compose"
                version = "1.0.2"
            }
        }
    }
}
