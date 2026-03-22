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
 * Handles `_struct_conf` loop data from an mmCIF file.
 *
 * Filters rows to only those whose `conf_type_id` starts with "HELX" (protein helices).
 * STRN (strands), TURN_P (turns), and other types are ignored — those are handled by
 * [MmCifStructSheetRangeHandler] or are not needed for ribbon rendering.
 *
 * Maps each accepted row to a [PdbHelix] and appends it to [Molecule.helixList].
 * The helix start/end residue numbers and chain IDs are populated; `initialResidueName`
 * and `terminalResidueName` are null because they are not present in `_struct_conf`.
 *
 * Column preference: `auth_*` variants are used as primary, with `label_*` fallback.
 */
class MmCifStructConfHandler(
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

        val iConfType = colIndex["conf_type_id"] ?: return

        val iId = colIndex["id"] ?: -1

        // Chain IDs: prefer auth_, fall back to label_
        val iBegChain = colIndex["beg_auth_asym_id"] ?: colIndex["beg_label_asym_id"] ?: -1
        val iEndChain = colIndex["end_auth_asym_id"] ?: colIndex["end_label_asym_id"] ?: -1

        // Residue numbers: prefer auth_, fall back to label_
        val iBegSeq = colIndex["beg_auth_seq_id"] ?: colIndex["beg_label_seq_id"] ?: -1
        val iEndSeq = colIndex["end_auth_seq_id"] ?: colIndex["end_label_seq_id"] ?: -1

        // Optional: helix class (1=right-handed alpha, 5=310, etc.)
        val iHelixClass = colIndex["pdbx_PDB_helix_class"] ?: -1

        var serialCounter = 0

        while (true) {
            val values = readRow(nCols, tokenizer) ?: break

            // Only process helix-type conformations
            val confType = values[iConfType]
            if (!confType.startsWith("HELX")) continue

            val helix = PdbHelix()
            helix.serialNumber = ++serialCounter
            helix.helixId      = if (iId >= 0) values[iId] else confType

            helix.initialChainIdChar =
                if (iBegChain >= 0 && values[iBegChain].isNotEmpty()) values[iBegChain][0] else ' '
            helix.terminalChainIdChar =
                if (iEndChain >= 0 && values[iEndChain].isNotEmpty()) values[iEndChain][0] else ' '

            helix.initialResidueNumber  = if (iBegSeq >= 0) parseInteger(values[iBegSeq]) else 0
            helix.terminalResidueNumber = if (iEndSeq >= 0) parseInteger(values[iEndSeq]) else 0

            // Use pdbx_PDB_helix_class if available, otherwise default to 1 (right-handed alpha)
            helix.helixClass = if (iHelixClass >= 0) parseInteger(values[iHelixClass]).let {
                if (it == 0) 1 else it
            } else 1

            mol.helixList.add(helix)
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
