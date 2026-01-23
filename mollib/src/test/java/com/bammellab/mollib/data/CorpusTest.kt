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

import com.bammellab.mollib.data.Corpus.corpus
import com.bammellab.mollib.data.Corpus.motmDateByKey
import com.bammellab.mollib.data.Corpus.motmThumbnailImageList
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.net.URI


/**
 * boundary testing of basic date string functionq
 */
class CorpusTest {
    @Test
    @DisplayName("test motm key to date function")
    fun testMonthMap() {
        assertThat(1, `is`(1))
        assertThat(motmDateByKey(1), `is`("January 2000"))
        assertThat(motmDateByKey(203), `is`("November 2016"))
        assertThat(motmDateByKey(0), `is`("INVALID DATE"))
        // .put(203, "November 2016")

    }

    /**
     * testing the completeness and order of the motmImageList
     *   This test takes advantage of the tagging the name
     *   of the image with the Motm index number.
     *
     *   Note that on two occasions a pair of strings are swapped in position.
     */
    @Test
    @DisplayName("check the contents of the motmImageList")
    fun motmImageListGet() {

        for (iter in motmThumbnailImageList.withIndex()) {
            val entry = iter.value
            val num = entry.substringBefore('-')
            try {
                Integer.parseInt(num)
            } catch (_: NumberFormatException) {
                fail("bad value $num for integer at index ${motmThumbnailImageList.size - iter.index}")
            }
            // Each entry should be numbered to match its 1-based position in the list
            assertEquals(num.toInt(), iter.index + 1)
        }
    }

    /**
     * Claude Prompt:
     * The webpage https://pdb101.rcsb.org/motm/motm-by-date lists all the
     * Molecules Of The Month (motm) categorized by year.  Please create a
     * test in the mollib that checks the "corpus" table in
     * mollib/src/main/java/com/bammellab/mollib/data/Corpus.kt against
     * the website - to verify that the names in the table match the name
     * given in the website.
     *
     * Verifies that the corpus titles match the official RCSB PDB101
     * "Molecule of the Month" list from the website.
     *
     * This test fetches https://pdb101.rcsb.org/motm/motm-by-date and
     * compares the extracted titles against the local corpus list.
     */
    @Test
    @DisplayName("verify corpus titles match RCSB website")
    fun verifyCorpusTitlesMatchWebsite() {
        val url = "https://pdb101.rcsb.org/motm/motm-by-date"
        val html = try {
            URI(url).toURL().readText()
        } catch (e: Exception) {
            println("Skipping test: Unable to fetch website - ${e.message}")
            return
        }

        // Extract all MOTM entries from the HTML
        // The page contains links like: <a href="/motm/313">Natural RNA-Only Assemblies</a>
        val motmPattern = Regex("""<a[^>]+href="/motm/(\d+)"[^>]*>([^<]+)</a>""")
        val websiteEntries = mutableMapOf<Int, String>()

        motmPattern.findAll(html).forEach { match ->
            val motmNumber = match.groupValues[1].toIntOrNull()
            val title = match.groupValues[2]
                .replace("&amp;", "&")
                .replace("&#39;", "'")
                .replace("&quot;", "\"")
                .trim()
            if (motmNumber != null && title.isNotEmpty()) {
                // Store the first occurrence (the main link, not navigation duplicates)
                if (!websiteEntries.containsKey(motmNumber)) {
                    websiteEntries[motmNumber] = title
                }
            }
        }

        // Verify we got entries from the website
        if (websiteEntries.isEmpty()) {
            fail("No MOTM entries found on website - HTML parsing may need updating")
        }

        val mismatches = mutableListOf<String>()
        val missing = mutableListOf<Int>()

        // Compare each corpus entry against the website
        for (index in corpus.indices) {
            val motmNumber = index + 1 // corpus is 0-indexed, MOTM numbers are 1-indexed
            val corpusTitle = corpus[index]
            val websiteTitle = websiteEntries[motmNumber]

            if (websiteTitle == null) {
                missing.add(motmNumber)
            } else if (!titlesMatch(corpusTitle, websiteTitle)) {
                mismatches.add("MOTM $motmNumber: corpus='$corpusTitle' vs website='$websiteTitle'")
            }
        }

        // Report results
        if (missing.isNotEmpty()) {
            println("MOTM entries not found on website: $missing")
        }

        if (mismatches.isNotEmpty()) {
            fail("Title mismatches found:\n${mismatches.joinToString("\n")}")
        }

        println("Verified ${corpus.size} corpus entries against website (${websiteEntries.size} website entries)")
    }

    /**
     * Compares titles with some flexibility for minor formatting differences.
     * Returns true if titles match or are equivalent variations.
     */
    private fun titlesMatch(corpusTitle: String, websiteTitle: String): Boolean {
        // Exact match
        if (corpusTitle == websiteTitle) return true

        // Normalize for comparison: lowercase, remove extra whitespace, normalize punctuation
        val normalizeTitle: (String) -> String = { title ->
            title.lowercase()
                .replace(Regex("\\s+"), " ")
                .replace(Regex("\\s*/\\s*"), "/") // Normalize "a / b" to "a/b"
                .replace("–", "-")
                .replace("—", "-")
                .replace("'", "'")
                .replace("`", "'")
                .trim()
        }

        return normalizeTitle(corpusTitle) == normalizeTitle(websiteTitle)
    }
}