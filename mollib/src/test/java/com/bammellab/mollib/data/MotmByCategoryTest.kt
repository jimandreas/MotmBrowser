/*
 *  Copyright 2020 Bammellab / James Andreas
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

package com.bammellab.mollib.data

import com.bammellab.mollib.data.MotmByCategory.MotmCategoryHealth
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.net.URI

/**
 * Tests to verify MotmByCategory data matches the RCSB PDB101 website.
 */
class MotmByCategoryTest {

    /**
     * Verifies that the MotmCategoryHealth table matches the official RCSB PDB101
     * "Molecule of the Month by Category" page for the Health and Disease section.
     *
     * This test fetches https://pdb101.rcsb.org/motm/motm-by-category and verifies:
     * 1. All category names from local data exist on the website
     * 2. All MOTM numbers referenced in local data exist on the website
     */
    @Test
    @DisplayName("verify MotmCategoryHealth matches RCSB website")
    fun verifyMotmCategoryHealthMatchesWebsite() {
        val url = "https://pdb101.rcsb.org/motm/motm-by-category"
        val html = try {
            URI(url).toURL().readText()
        } catch (e: Exception) {
            println("Skipping test: Unable to fetch website - ${e.message}")
            return
        }

        // Parse the local MotmCategoryHealth array into structured data
        val localCategories = parseLocalCategoryArray(MotmCategoryHealth)

        // Extract all MOTM numbers from the website
        val websiteMotmNumbers = extractWebsiteMotmNumbers(html)

        if (websiteMotmNumbers.isEmpty()) {
            fail("No MOTM numbers found on website - HTML parsing may need updating")
        }

        // Verify category names exist on website
        val missingCategories = mutableListOf<String>()
        for (categoryName in localCategories.keys) {
            if (!categoryExistsOnWebsite(html, categoryName)) {
                missingCategories.add(categoryName)
            }
        }

        // Verify all MOTM numbers from local data exist on website
        val allLocalMotmNumbers = localCategories.values.flatten().toSet()
        val missingMotmNumbers = allLocalMotmNumbers - websiteMotmNumbers

        // Report results
        val errors = StringBuilder()

        if (missingCategories.isNotEmpty()) {
            errors.appendLine("Categories in local data but not found on website: $missingCategories")
        }

        if (missingMotmNumbers.isNotEmpty()) {
            errors.appendLine("MOTM numbers in local data but not on website: $missingMotmNumbers")
        }

        if (errors.isNotEmpty()) {
            fail(errors.toString())
        }

        println("Verified ${localCategories.size} categories exist on website")
        println("Verified ${allLocalMotmNumbers.size} unique MOTM numbers from local data exist on website")
        println("Website has ${websiteMotmNumbers.size} total unique MOTM numbers")
    }

    /**
     * Parses the local category array into a map of category name to list of MOTM numbers.
     */
    private fun parseLocalCategoryArray(array: Array<String>): Map<String, List<Int>> {
        val result = mutableMapOf<String, MutableList<Int>>()
        var currentCategory: String? = null

        for (entry in array) {
            val trimmed = entry.trim()

            // Skip section header
            if (trimmed.startsWith("Section ")) {
                continue
            }

            // Check if this is a MOTM number (all digits)
            val motmNumber = trimmed.toIntOrNull()
            if (motmNumber != null) {
                // This is a MOTM number - add to current category
                currentCategory?.let {
                    result.getOrPut(it) { mutableListOf() }.add(motmNumber)
                }
            } else {
                // This is a category name
                currentCategory = trimmed
                result.getOrPut(currentCategory) { mutableListOf() }
            }
        }

        return result
    }

    /**
     * Extracts all MOTM numbers from the website HTML.
     */
    private fun extractWebsiteMotmNumbers(html: String): Set<Int> {
        val motmLinkPattern = Regex("""href="/motm/(\d+)"""")
        return motmLinkPattern.findAll(html)
            .mapNotNull { it.groupValues[1].toIntOrNull() }
            .toSet()
    }

    /**
     * Checks if a category name exists on the website (case-insensitive).
     */
    private fun categoryExistsOnWebsite(html: String, categoryName: String): Boolean {
        // Check for exact match or close variations
        val variations = listOf(
            categoryName,
            categoryName.replace("&", "and"),
            categoryName.replace("and", "&")
        )

        for (variation in variations) {
            val escapedName = Regex.escape(variation)
            // Look for the category name in various HTML contexts
            val patterns = listOf(
                Regex(""">$escapedName\s*<""", RegexOption.IGNORE_CASE),
                Regex(""">$escapedName</""", RegexOption.IGNORE_CASE),
                Regex(""">\s*$escapedName\s*</a>""", RegexOption.IGNORE_CASE)
            )

            if (patterns.any { it.containsMatchIn(html) }) {
                return true
            }
        }

        return false
    }
}
