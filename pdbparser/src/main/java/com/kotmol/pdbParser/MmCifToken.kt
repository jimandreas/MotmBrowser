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

/**
 * Token types for the STAR grammar used in mmCIF/PDBx files.
 *
 * mmCIF uses the Self-Defining Text Archive and Retrieval (STAR) grammar.
 * Data is organized into named data blocks and presented as either
 * key-value pairs or tabular loop_ structures.
 */
sealed class MmCifToken {
    /** data_<name> — starts a named data block, e.g. data_4HHB */
    data class DataBlock(val name: String) : MmCifToken()

    /** loop_ — begins a tabular data block with column headers followed by rows */
    object Loop : MmCifToken()

    /** _category.attribute — a data item tag, e.g. _atom_site.Cartn_x */
    data class Tag(val category: String, val attribute: String) : MmCifToken()

    /** An unquoted value, including the special values '.' (inapplicable) and '?' (missing) */
    data class Value(val text: String) : MmCifToken()

    /** A single-quoted ('...') or double-quoted ("...") value — may contain whitespace */
    data class QuotedValue(val text: String) : MmCifToken()

    /**
     * A semicolon-delimited multi-line text block.
     * Starts with ';' at position 0 of a line; ends at next line beginning with ';'.
     */
    data class TextValue(val text: String) : MmCifToken()

    /** Signals end of input */
    object End : MmCifToken()
}
