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
 * Handles `_struct_sheet_range` loop data from an mmCIF file.
 *
 * Maps each row to a [PdbBetaSheet] and appends it to [Molecule.pdbSheetList].
 * Only the fields consumed by [ParserPdbFile]'s `addSheetSecondaryInformation()` are
 * populated: `sheetIdentification`, `strandNumber`, `initialChainIdChar`,
 * `initialResidueNumber`, `terminalChainIdChar`, `terminalResidueNumber`.
 * Registration fields and `parallelSenseCode` are left as defaults.
 *
 * Column preference: `auth_*` variants are used as primary, with `label_*` fallback.
 */
class MmCifStructSheetRangeHandler(
    private val mol: Molecule,
    private val messages: MutableList<String>
) : MmCifLoopHandler {

    override fun handleLoop(
        columnHeaders: List<Pair<String, String>>,
        tokenizer: MmCifTokenizer
    ) {
        val colIndex: Map<String, Int> = columnHeaders
            .mapIndexed { i, (_, attr) -> attr to i }
            .toMap()

        val nCols = columnHeaders.size

        val iSheetId    = colIndex["sheet_id"] ?: -1
        val iStrandId   = colIndex["id"]       ?: -1

        // Chain IDs: prefer auth_, fall back to label_
        val iBegChain = colIndex["beg_auth_asym_id"] ?: colIndex["beg_label_asym_id"] ?: -1
        val iEndChain = colIndex["end_auth_asym_id"] ?: colIndex["end_label_asym_id"] ?: -1

        // Residue numbers: prefer auth_, fall back to label_
        val iBegSeq = colIndex["beg_auth_seq_id"] ?: colIndex["beg_label_seq_id"] ?: -1
        val iEndSeq = colIndex["end_auth_seq_id"] ?: colIndex["end_label_seq_id"] ?: -1

        while (true) {
            val values = readRow(nCols, tokenizer) ?: break

            val sheet = PdbBetaSheet()

            sheet.sheetIdentification = if (iSheetId >= 0) values[iSheetId] else null
            sheet.strandNumber        = if (iStrandId >= 0) parseInteger(values[iStrandId]) else 0

            sheet.initialChainIdChar =
                if (iBegChain >= 0 && values[iBegChain].isNotEmpty()) values[iBegChain][0] else ' '
            sheet.terminalChainIdChar =
                if (iEndChain >= 0 && values[iEndChain].isNotEmpty()) values[iEndChain][0] else ' '

            sheet.initialResidueNumber  = if (iBegSeq >= 0) parseInteger(values[iBegSeq]) else 0
            sheet.terminalResidueNumber = if (iEndSeq >= 0) parseInteger(values[iEndSeq]) else 0

            // parallelSenseCode and registration fields left as defaults (0 / null / ' ')

            mol.pdbSheetList.add(sheet)
        }
    }

    private fun readRow(nCols: Int, tokenizer: MmCifTokenizer): List<String>? {
        val values = ArrayList<String>(nCols)
        for (i in 0 until nCols) {
            when (val token = tokenizer.nextToken()) {
                is MmCifToken.Value       -> values.add(token.text)
                is MmCifToken.QuotedValue -> values.add(token.text)
                is MmCifToken.TextValue   -> values.add(token.text)
                else -> {
                    tokenizer.pushBack(token)
                    return null
                }
            }
        }
        return values
    }

    private fun parseInteger(s: String): Int =
        try { s.toInt() } catch (_: NumberFormatException) { 0 }
}
