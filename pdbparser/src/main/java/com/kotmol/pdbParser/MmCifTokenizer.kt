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

/**
 * Pull-based tokenizer for mmCIF/PDBx files (STAR grammar).
 *
 * Reads from a BufferedReader and emits one [MmCifToken] per call to [nextToken].
 * Supports single-token push-back via [pushBack] so that parsers can un-read
 * a sentinel token discovered at the end of a loop.
 *
 * STAR grammar rules implemented:
 * - Lines starting with '#' are comments and are skipped entirely.
 * - A ';' at position 0 of a line begins a multi-line text block that ends
 *   at the next line whose first character is ';'.
 * - 'single-quoted' and "double-quoted" strings may contain whitespace.
 * - data_NAME → DataBlock("NAME")
 * - loop_     → Loop
 * - _cat.attr → Tag("cat", "attr")
 * - Everything else → Value(text)
 */
class MmCifTokenizer(private val reader: BufferedReader) {

    private val pendingTokens: ArrayDeque<MmCifToken> = ArrayDeque()
    private val pushbackStack: ArrayDeque<MmCifToken> = ArrayDeque()

    /**
     * Return the next token from the stream.
     * Returns [MmCifToken.End] when no more input is available.
     */
    fun nextToken(): MmCifToken {
        // Check push-back stack first (LIFO)
        if (pushbackStack.isNotEmpty()) return pushbackStack.removeFirst()

        // Drain the pending-token queue before reading more lines
        while (pendingTokens.isEmpty()) {
            val line = reader.readLine() ?: return MmCifToken.End

            // Skip blank lines
            if (line.isBlank()) continue

            // Skip comment lines (# must appear as the first non-whitespace
            // character, but per STAR spec a '#' at start of any token is a comment)
            if (line.trimStart().startsWith("#")) continue

            // Semicolon text block: ';' MUST be at position 0 (first char of raw line)
            if (line[0] == ';') {
                val sb = StringBuilder()
                // Everything after the opening ';' on the same line is content
                if (line.length > 1) sb.append(line.substring(1))
                while (true) {
                    val nextLine = reader.readLine() ?: break
                    if (nextLine.startsWith(";")) break
                    if (sb.isNotEmpty()) sb.append('\n')
                    sb.append(nextLine)
                }
                pendingTokens.addLast(MmCifToken.TextValue(sb.toString()))
                continue
            }

            tokenizeLine(line)
        }

        return pendingTokens.removeFirst()
    }

    /**
     * Push a token back so it will be returned by the next [nextToken] call.
     * Used by loop handlers to "unget" a sentinel token that marks the end of a loop.
     */
    fun pushBack(token: MmCifToken) {
        pushbackStack.addFirst(token)
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private fun tokenizeLine(line: String) {
        var pos = 0
        val len = line.length

        while (pos < len) {
            // Skip horizontal whitespace
            while (pos < len && (line[pos] == ' ' || line[pos] == '\t')) pos++
            if (pos >= len) break

            val ch = line[pos]

            // '#' as a token boundary starts a line comment — discard the rest
            if (ch == '#') break

            // Quoted string: either single or double quote
            if (ch == '\'' || ch == '"') {
                val closeQuote = ch
                pos++ // skip opening quote
                val sb = StringBuilder()
                while (pos < len) {
                    val c = line[pos]
                    // Closing quote must be followed by whitespace or end-of-string
                    if (c == closeQuote &&
                        (pos + 1 >= len || line[pos + 1] == ' ' || line[pos + 1] == '\t')) {
                        pos++ // skip closing quote
                        break
                    }
                    sb.append(c)
                    pos++
                }
                pendingTokens.addLast(MmCifToken.QuotedValue(sb.toString()))
                continue
            }

            // Regular token: read until whitespace
            val start = pos
            while (pos < len && line[pos] != ' ' && line[pos] != '\t') pos++
            val token = line.substring(start, pos)

            classifyToken(token)
        }
    }

    private fun classifyToken(token: String) {
        when {
            token.startsWith("data_") ->
                pendingTokens.addLast(MmCifToken.DataBlock(token.substring(5)))

            token == "loop_" ->
                pendingTokens.addLast(MmCifToken.Loop)

            token.startsWith("_") -> {
                val dotPos = token.indexOf('.')
                if (dotPos > 0) {
                    pendingTokens.addLast(
                        MmCifToken.Tag(
                            token.substring(1, dotPos),
                            token.substring(dotPos + 1)
                        )
                    )
                } else {
                    // Tag with no dot — treat attribute as empty string
                    pendingTokens.addLast(MmCifToken.Tag(token.substring(1), ""))
                }
            }

            else -> pendingTokens.addLast(MmCifToken.Value(token))
        }
    }
}
