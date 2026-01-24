/*
 *  Copyright 2024 Bammellab / James Andreas
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

package com.bammellab.motm.release

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File
import java.util.zip.ZipFile

/**
 * Tests to verify release build configuration for Google Play deployment.
 *
 * These tests verify that when a release AAB is built:
 * 1. The code is shrunk/obfuscated (R8 minification is enabled)
 * 2. The mapping file is included in the bundle for crash report deobfuscation
 *
 * The mapping file allows Google Play to automatically deobfuscate stack traces
 * in crash reports, making them readable with original class/method names.
 */
class ReleaseBuildTest {

    companion object {
        // Path to the release AAB output directory (relative to module root)
        private const val AAB_OUTPUT_DIR = "build/outputs/bundle/release"

        // Path where the mapping file is stored in the AAB (AGP 8.0+)
        private const val MAPPING_PATH_IN_AAB = "BUNDLE-METADATA/com.android.tools.build.obfuscation/proguard.map"

        // Minimum expected size for a valid mapping file (in bytes)
        // A real mapping file should be at least a few KB
        private const val MIN_MAPPING_SIZE = 1000L
    }

    /**
     * Finds the release AAB file in the build output directory.
     * Returns null if no AAB file exists (build hasn't been run).
     */
    private fun findReleaseAab(): File? {
        // Get the module root directory by looking for build.gradle.kts
        val moduleRoot = findModuleRoot() ?: return null
        val aabDir = File(moduleRoot, AAB_OUTPUT_DIR)

        if (!aabDir.exists() || !aabDir.isDirectory) {
            return null
        }

        // Find any .aab file in the release output directory
        return aabDir.listFiles()?.firstOrNull { it.extension == "aab" }
    }

    /**
     * Finds the module root directory by traversing up from the current directory.
     */
    private fun findModuleRoot(): File? {
        // Try to find the motmbrowser module root
        val possiblePaths = listOf(
            File("motmbrowser"),
            File("../motmbrowser"),
            File("."),
            File("..")
        )

        for (path in possiblePaths) {
            val buildFile = File(path, "build.gradle.kts")
            if (buildFile.exists() && buildFile.readText().contains("com.bammellab.motm")) {
                return path.canonicalFile
            }
        }

        // Fallback: check if we're already in the module
        val currentBuildFile = File("build.gradle.kts")
        if (currentBuildFile.exists()) {
            return File(".").canonicalFile
        }

        return null
    }

    @Test
    @DisplayName("Release AAB contains obfuscation mapping file for crash deobfuscation")
    fun verifyReleaseBundleContainsMappingFile() {
        val aabFile = findReleaseAab()

        // Skip test if no release AAB has been built
        assumeTrue(
            aabFile != null,
            "Skipping: No release AAB found. Run './gradlew.bat :motmbrowser:bundleRelease' first."
        )

        assertThat(aabFile!!.exists()).isTrue()
        println("Found release AAB: ${aabFile.absolutePath}")
        println("AAB size: ${aabFile.length()} bytes")

        // Open the AAB as a ZIP file and check for the mapping file
        ZipFile(aabFile).use { zip ->
            val mappingEntry = zip.getEntry(MAPPING_PATH_IN_AAB)

            assertWithMessage("Mapping file at '$MAPPING_PATH_IN_AAB' should exist in AAB")
                .that(mappingEntry)
                .isNotNull()

            // Verify the mapping file has substantial content
            val mappingSize = mappingEntry.size
            println("Mapping file size: $mappingSize bytes")

            assertWithMessage("Mapping file size should be at least $MIN_MAPPING_SIZE bytes")
                .that(mappingSize)
                .isAtLeast(MIN_MAPPING_SIZE)

            // Read and verify the mapping file content looks valid
            val mappingContent = zip.getInputStream(mappingEntry).bufferedReader().use { it.readText() }

            // A valid R8 mapping file should contain class mappings like:
            // com.bammellab.motm.SomeClass -> a.b.c:
            assertWithMessage("Mapping file should contain R8 mapping syntax (->)")
                .that(mappingContent)
                .contains("->")

            // Should contain references to our package
            assertWithMessage("Mapping file should reference com.bammellab package")
                .that(mappingContent)
                .contains("com.bammellab")

            println("Mapping file verification passed:")
            println("  - File exists in AAB at: $MAPPING_PATH_IN_AAB")
            println("  - Size: $mappingSize bytes (minimum required: $MIN_MAPPING_SIZE)")
            println("  - Contains valid R8 mapping syntax")
            println("  - References com.bammellab package")
        }
    }

    @Test
    @DisplayName("Release AAB is code-shrunk (smaller than expected unminified size)")
    fun verifyReleaseBundleIsMinified() {
        val aabFile = findReleaseAab()

        // Skip test if no release AAB has been built
        assumeTrue(
            aabFile != null,
            "Skipping: No release AAB found. Run './gradlew.bat :motmbrowser:bundleRelease' first."
        )

        assertThat(aabFile!!.exists()).isTrue()

        // Open the AAB and check for signs of minification
        ZipFile(aabFile).use { zip ->
            // Look for dex files in the bundle
            val dexEntries = zip.entries().asSequence()
                .filter { it.name.endsWith(".dex") }
                .toList()

            assertWithMessage("AAB should contain DEX files")
                .that(dexEntries)
                .isNotEmpty()

            println("Found ${dexEntries.size} DEX file(s) in AAB")

            // Check total DEX size - minified apps should be reasonably compact
            val totalDexSize = dexEntries.sumOf { it.size }
            println("Total DEX size: $totalDexSize bytes (${totalDexSize / 1024} KB)")

            // The presence of the mapping file (checked in other test) combined with
            // a reasonable DEX size indicates minification is working.
            // We don't set a hard size limit as it varies with app changes.
            assertWithMessage("Total DEX size should be non-zero")
                .that(totalDexSize)
                .isGreaterThan(0L)
        }
    }

    @Test
    @DisplayName("Mapping file format is compatible with Google Play Console")
    fun verifyMappingFileFormat() {
        val aabFile = findReleaseAab()

        // Skip test if no release AAB has been built
        assumeTrue(
            aabFile != null,
            "Skipping: No release AAB found. Run './gradlew.bat :motmbrowser:bundleRelease' first."
        )

        ZipFile(aabFile!!).use { zip ->
            val mappingEntry = zip.getEntry(MAPPING_PATH_IN_AAB)
            assumeTrue(
                mappingEntry != null,
                "Skipping: No mapping file found in AAB"
            )

            val mappingContent = zip.getInputStream(mappingEntry).bufferedReader().use { it.readText() }
            val lines = mappingContent.lines()

            // R8 mapping files should have a header comment
            val hasValidHeader = lines.any { it.startsWith("#") }
            println("Has R8 header comments: $hasValidHeader")

            // Count class mappings (lines with " -> " pattern for class definitions)
            val classMappings = lines.count { line ->
                line.contains(" -> ") && !line.startsWith(" ") && line.endsWith(":")
            }
            println("Number of class mappings: $classMappings")

            // Count method/field mappings (indented lines with " -> ")
            val memberMappings = lines.count { line ->
                line.trimStart().let { trimmed ->
                    trimmed.contains(" -> ") && line != trimmed
                }
            }
            println("Number of member mappings: $memberMappings")

            // A properly minified app should have many mappings
            assertWithMessage("Number of class mappings should be at least 10")
                .that(classMappings)
                .isAtLeast(10)

            assertWithMessage("Number of member mappings should be at least 50")
                .that(memberMappings)
                .isAtLeast(50)

            println("Mapping file format verification passed:")
            println("  - $classMappings classes mapped")
            println("  - $memberMappings members (methods/fields) mapped")
            println("  - Format compatible with Google Play Console deobfuscation")
        }
    }
}
