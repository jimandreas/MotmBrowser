/*
 *  Copyright 2026 Bammellab / James Andreas
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

import com.kotmol.pdbParser.Molecule
import com.kotmol.pdbParser.ParserMmCifFile
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI
import java.util.zip.GZIPInputStream

/**
 * Long-running integration test: downloads every PDB entry listed in [PDBs] from RCSB as
 * mmCIF (.cif.gz), parses it with [ParserMmCifFile], and reports any failures.
 *
 * This test is tagged "long-running" and is excluded from the default `./gradlew :mollib:test`
 * run.  To execute it explicitly:
 *
 *   ./gradlew.bat :mollib:testLongRunning
 *
 * Network access is required.  The test skips gracefully when the network is unavailable.
 * HTTP 404 responses (entries removed from RCSB) are reported as warnings, not failures.
 * A structure that parses with zero atoms is counted as a parser failure.
 */
@Tag("long-running")
class MmCifDownloadParseAllTest {

    @Test
    fun downloadAndParseAllPdbIds() {
        val uniqueIds = PDBs.obtainPdbMappingList()
            .map { it.pdbName.uppercase() }
            .toSortedSet()

        println("\n=== MmCifDownloadParseAllTest ===")
        println("Unique PDB IDs to test: ${uniqueIds.size}")

        // Check network reachability before iterating
        try {
            (URI("https://files.rcsb.org").toURL().openConnection() as HttpURLConnection).apply {
                connectTimeout = 5_000
                readTimeout = 5_000
                connect()
                disconnect()
            }
        } catch (e: Exception) {
            println("Network unavailable — skipping test: ${e.message}")
            return
        }

        val failures   = mutableListOf<String>()
        val notFound   = mutableListOf<String>()
        var successCount = 0

        uniqueIds.forEachIndexed { index, pdbId ->
            val url = "https://files.rcsb.org/download/$pdbId.cif.gz"
            if ((index + 1) % 50 == 0) {
                println("  Progress: ${index + 1}/${uniqueIds.size}  " +
                        "(ok=$successCount  fail=${failures.size}  404=${notFound.size})")
            }

            try {
                val conn = (URI(url).toURL().openConnection() as HttpURLConnection).apply {
                    connectTimeout = 30_000
                    readTimeout    = 120_000
                }

                if (conn.responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    notFound.add(pdbId)
                    conn.disconnect()
                    return@forEachIndexed
                }

                val atomCount: Int
                val messages = mutableListOf<String>()
                run {
                    val mol = Molecule()
                    GZIPInputStream(conn.inputStream).use { stream ->
                        ParserMmCifFile.Builder(mol)
                            .setMoleculeName(pdbId)
                            .setMessageStrings(messages)
                            .loadMmCifFromStream(stream)
                            .doBondProcessing(true)
                            .parse()
                    }
                    atomCount = mol.atomNumberList.size
                    mol.clearLists()
                }
                conn.disconnect()

                if (atomCount == 0) {
                    failures.add("$pdbId: parsed 0 atoms")
                } else {
                    successCount++
                }
            } catch (e: IOException) {
                failures.add("$pdbId: download failed — ${e.message}")
            } catch (e: Exception) {
                failures.add("$pdbId: parse exception — ${e::class.simpleName}: ${e.message}")
            }
        }

        // Summary
        println("\n--- Results (${uniqueIds.size} unique IDs) ---")
        println("  PASS : $successCount")
        println("  FAIL : ${failures.size}")
        println("  404  : ${notFound.size}")

        if (notFound.isNotEmpty()) {
            println("\nNot found on RCSB (HTTP 404):")
            notFound.forEach { println("  $it") }
        }

        if (failures.isNotEmpty()) {
            println("\nFailures:")
            failures.forEach { println("  $it") }
            throw AssertionError(
                "${failures.size} PDB ID(s) failed to parse correctly:\n" +
                        failures.joinToString("\n") { "  $it" }
            )
        }

        println("\nAll $successCount entries parsed successfully.")
    }
}
