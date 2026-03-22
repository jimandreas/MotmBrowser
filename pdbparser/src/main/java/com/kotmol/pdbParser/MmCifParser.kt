/*
 *  Copyright 2021 James Andreas
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

package com.kotmol.pdbParser

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.SequenceInputStream

/**
 * Top-level mmCIF parser.
 *
 * Drives a [MmCifTokenizer] and dispatches `loop_` blocks to registered
 * [MmCifLoopHandler]s by category name.  Key-value pairs that are not needed
 * for 3D rendering are silently skipped.
 *
 * Registered categories:
 * - `atom_site`          → [MmCifAtomSiteHandler]
 * - `struct_conf`        → [MmCifStructConfHandler]
 * - `struct_sheet_range` → [MmCifStructSheetRangeHandler]
 *
 * The public [atomSiteHandler] property is exposed so the caller can retrieve
 * the average atom position for molecule centering after parsing completes.
 */
class MmCifParser(
    private val mol: Molecule,
    private val messages: MutableList<String>
) {
    val atomSiteHandler = MmCifAtomSiteHandler(mol, messages)

    private val loopHandlers: Map<String, MmCifLoopHandler> = mapOf(
        "atom_site"          to atomSiteHandler,
        "struct_conf"        to MmCifStructConfHandler(mol, messages),
        "struct_sheet_range" to MmCifStructSheetRangeHandler(mol, messages)
    )

    fun parse(stream: InputStream) {
        val reader = BufferedReader(InputStreamReader(stream))
        val tokenizer = MmCifTokenizer(reader)

        var token = tokenizer.nextToken()
        while (token != MmCifToken.End) {
            when (token) {
                is MmCifToken.Loop -> processLoop(tokenizer)
                is MmCifToken.Tag  -> {
                    // key-value pair — consume the associated value and move on
                    tokenizer.nextToken()
                }
                else -> { /* DataBlock, Value, End — just advance */ }
            }
            token = tokenizer.nextToken()
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private fun processLoop(tokenizer: MmCifTokenizer) {
        // Collect all consecutive Tag tokens as column headers
        val columnHeaders = mutableListOf<Pair<String, String>>()

        var token = tokenizer.nextToken()
        while (token is MmCifToken.Tag) {
            columnHeaders.add(token.category to token.attribute)
            token = tokenizer.nextToken()
        }

        if (columnHeaders.isEmpty()) {
            tokenizer.pushBack(token)
            return
        }

        // Push back the first non-Tag token so either the handler or skipLoop can see it
        tokenizer.pushBack(token)

        val category = columnHeaders[0].first
        val handler  = loopHandlers[category]

        if (handler != null) {
            handler.handleLoop(columnHeaders, tokenizer)
        } else {
            skipLoop(columnHeaders.size, tokenizer)
        }
    }

    /**
     * Consume and discard all value tokens belonging to an unrecognised loop.
     * Pushes back the first non-value sentinel so the main parsing loop can
     * process it (e.g. a new `loop_` or `_tag`).
     */
    private fun skipLoop(numColumns: Int, tokenizer: MmCifTokenizer) {
        while (true) {
            when (val token = tokenizer.nextToken()) {
                is MmCifToken.Value,
                is MmCifToken.QuotedValue,
                is MmCifToken.TextValue -> continue
                else -> {
                    tokenizer.pushBack(token)
                    return
                }
            }
        }
    }
}

// =============================================================================
// Format detector
// =============================================================================

/**
 * Determines whether an [InputStream] contains an mmCIF file or a legacy PDB file,
 * without consuming the stream permanently.
 *
 * Detection rule:
 * - If any non-blank, non-comment line starts with `data_` → mmCIF
 * - If any non-blank line starts with one of the legacy PDB record types → PDB
 *
 * The first [BUFFER_BYTES] bytes are buffered and re-prepended so the returned
 * stream contains the complete original content.
 */
object MmCifFormatDetector {

    private const val BUFFER_BYTES = 4096

    private val PDB_RECORD_PREFIXES = listOf(
        "ATOM", "HETATM", "HEADER", "REMARK", "HELIX", "SHEET",
        "SEQRES", "CRYST1", "ORIGX", "SCALE", "COMPND", "SOURCE"
    )

    /**
     * @return Pair(isMmCif, reconstitutedStream) — the stream is fully intact and
     *   ready to be parsed from the beginning.
     */
    fun detect(stream: InputStream): Pair<Boolean, InputStream> {
        val buffer = stream.readNBytes(BUFFER_BYTES)
        val reconstituted = SequenceInputStream(
            ByteArrayInputStream(buffer),
            stream
        )

        val headerText = String(buffer, Charsets.UTF_8)
        var isMmCif = false

        for (line in headerText.lines()) {
            val trimmed = line.trim()
            if (trimmed.isEmpty() || trimmed.startsWith("#")) continue
            when {
                trimmed.startsWith("data_") -> { isMmCif = true; break }
                PDB_RECORD_PREFIXES.any { trimmed.startsWith(it) } -> { isMmCif = false; break }
            }
        }

        return Pair(isMmCif, reconstituted)
    }
}
