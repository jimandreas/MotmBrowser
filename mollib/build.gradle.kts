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

plugins {
    alias(libs.plugins.android.library)
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

android {
    namespace = "com.bammellab.mollib"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // let the consuming app handle minification
            consumerProguardFiles("src/main/proguard-rules.pro")
        }
    }



    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
        disable += listOf("UnusedResources", "VectorRaster", "DefaultLocale")
        lintConfig = file("lint.xml")
        textOutput = file("stdout")
        textReport = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

// Default: exclude long-running tagged tests from all standard Test tasks
tasks.withType<Test>().configureEach {
    if (name != "testLongRunning") {
        useJUnitPlatform {
            excludeTags("long-running")
        }
    }
}

// Explicit task to run only long-running tests (requires network).
// Borrows classpath/testClassesDirs from testDebugUnitTest (registered by AGP).
afterEvaluate {
    val debugUnitTest = tasks.findByName("testDebugUnitTest") as? Test ?: return@afterEvaluate
    tasks.register<Test>("testLongRunning") {
        description = "Downloads and parses every PDB entry in PDBs.kt via mmCIF from RCSB (network required, ~10–30 min)"
        group = "verification"
        testClassesDirs = debugUnitTest.testClassesDirs
        classpath = debugUnitTest.classpath
        dependsOn(debugUnitTest.dependsOn)
        maxHeapSize = "2g"
        useJUnitPlatform {
            includeTags("long-running")
        }
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.preference.ktx)

    implementation(libs.timber)
    implementation(libs.okhttp)
    implementation(project(":pdbparser"))

    testImplementation(libs.jetbrains.annotations)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.truth)
    testRuntimeOnly(libs.junit.platform.launcher)
}
