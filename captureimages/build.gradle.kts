/*
 *  Copyright 2022 Bammellab / James Andreas
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

val versionMajor = 2
val versionMinor = 2
val versionPatch = 0
val versionBuild = 2201

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

android {
    namespace = "com.bammellab.captureimages"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.bammellab.captureimages"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = versionMajor * 1000000 + versionMinor * 10000 + versionPatch * 100 + versionBuild
        versionName = "$versionMajor.$versionMinor.$versionPatch"
        resourceConfigurations += listOf("en")
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        create("release") {
            val props = Properties()
            val propFile = file("../gradle/signing.properties")
            if (propFile.canRead()) {
                props.load(FileInputStream(propFile))
                if (props.containsKey("STORE_FILE") && props.containsKey("STORE_PASSWORD") &&
                    props.containsKey("KEY_ALIAS") && props.containsKey("KEY_PASSWORD")
                ) {
                    storeFile = file(props["STORE_FILE"] as String)
                    storePassword = props["STORE_PASSWORD"] as String
                    keyAlias = props["KEY_ALIAS"] as String
                    keyPassword = props["KEY_PASSWORD"] as String
                }
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.txt"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        abortOnError = false
        checkDependencies = true
        checkReleaseBuilds = false
        checkTestSources = true
        disable += listOf(
            "UnusedResources", "VectorRaster", "VectorPath", "StaticFieldLeak",
            "UseCompoundDrawables", "TypographyEllipsis", "IconLauncherShape",
            "IconLocation", "Autofill", "RtlEnabled"
        )
        explainIssues = false
        lintConfig = file("lint.xml")
        textOutput = file("stdout")
        textReport = true
    }
}

dependencies {
    implementation(project(":mollib"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.material)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.timber)
    implementation(libs.pdbparser)

    testImplementation(libs.jetbrains.annotations)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.truth)
}
