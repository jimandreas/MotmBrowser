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

@file:Suppress("unused")

package com.kotmol.pdbParser

/**
 * Handles `_atom_site` loop data from an mmCIF file.
 *
 * Maps mmCIF columns to [PdbAtom] fields and writes each accepted atom into
 * the shared [Molecule].  Filters applied:
 * - Only model 1 (when `pdbx_PDB_model_num` is present)
 * - Only altLoc '.' or 'A' (when `label_alt_id` is present)
 * - Skips O5T and O3T atom names (no bond info available)
 *
 * Column preference: `auth_*` columns are preferred over `label_*` equivalents
 * (auth_ matches legacy PDB chain letters and residue numbers).
 *
 * After [handleLoop] returns, call [getAveragePosition] to obtain the centroid
 * of all accepted atoms for molecule centering.
 */
class MmCifAtomSiteHandler(
    private val mol: Molecule,
    private val messages: MutableList<String>
) : MmCifLoopHandler {

    var sumX = 0.0
    var sumY = 0.0
    var sumZ = 0.0
    private var atomCount = 0

    /** Returns the average (x,y,z) of all atoms accepted into the molecule. */
    fun getAveragePosition(): KotmolVector3 {
        if (atomCount == 0) return KotmolVector3()
        return KotmolVector3(
            (sumX / atomCount).toFloat(),
            (sumY / atomCount).toFloat(),
            (sumZ / atomCount).toFloat()
        )
    }

    override fun handleLoop(
        columnHeaders: List<Pair<String, String>>,
        tokenizer: MmCifTokenizer
    ) {
        // Build attribute→columnIndex lookup
        val colIndex: Map<String, Int> = columnHeaders
            .mapIndexed { i, (_, attr) -> attr to i }
            .toMap()

        val nCols = columnHeaders.size

        // --- Mandatory columns -----------------------------------------------
        val iGroupPdb = colIndex["group_PDB"] ?: return
        val iId       = colIndex["id"]        ?: return
        val iCartnX   = colIndex["Cartn_x"]   ?: return
        val iCartnY   = colIndex["Cartn_y"]   ?: return
        val iCartnZ   = colIndex["Cartn_z"]   ?: return

        // --- Preferred auth_* with label_* fallback --------------------------
        val iAtomName   = colIndex["auth_atom_id"]  ?: colIndex["label_atom_id"]  ?: -1
        val iResName    = colIndex["auth_comp_id"]   ?: colIndex["label_comp_id"]  ?: -1
        val iChainId    = colIndex["auth_asym_id"]   ?: colIndex["label_asym_id"]  ?: -1
        val iSeqNum     = colIndex["auth_seq_id"]    ?: colIndex["label_seq_id"]   ?: -1

        // --- Optional columns ------------------------------------------------
        val iTypeSymbol = colIndex["type_symbol"]         ?: -1
        val iAltLoc     = colIndex["label_alt_id"]        ?: -1
        val iModelNum   = colIndex["pdbx_PDB_model_num"]  ?: -1
        val iInsCode    = colIndex["pdbx_PDB_ins_code"]   ?: -1

        // --- Read rows -------------------------------------------------------
        while (true) {
            val values = readRow(nCols, tokenizer) ?: break

            // Filter: only model 1
            if (iModelNum >= 0) {
                val modelNum = values[iModelNum]
                if (modelNum != "1" && modelNum != ".") continue
            }

            // Filter: only altLoc '.' (inapplicable) or 'A'
            if (iAltLoc >= 0) {
                val altLoc = values[iAltLoc]
                if (altLoc != "." && altLoc != "A") continue
            }

            val atom = PdbAtom()

            // Atom type
            atom.atomType = when (values[iGroupPdb]) {
                "ATOM"   -> PdbAtom.AtomType.IS_ATOM
                "HETATM" -> PdbAtom.AtomType.IS_HETATM
                else     -> PdbAtom.AtomType.IS_NOT_A_TYPE
            }

            atom.atomNumber    = parseInteger(values[iId])
            atom.atomName      = if (iAtomName >= 0) values[iAtomName] else ""
            atom.residueName   = if (iResName  >= 0) values[iResName]  else ""
            atom.elementSymbol = if (iTypeSymbol >= 0) values[iTypeSymbol] else ""

            atom.chainId = if (iChainId >= 0 && values[iChainId].isNotEmpty())
                values[iChainId][0] else ' '

            atom.residueSeqNumber = if (iSeqNum >= 0) parseInteger(values[iSeqNum]) else 0

            atom.residueInsertionCode = if (iInsCode >= 0) {
                val ic = values[iInsCode]
                if (ic == "." || ic == "?" || ic.isEmpty()) ' ' else ic[0]
            } else ' '

            val vx = parseFloat(values[iCartnX])
            val vy = parseFloat(values[iCartnY])
            val vz = parseFloat(values[iCartnZ])
            atom.atomPosition = KotmolVector3(vx, vy, vz)

            // Skip O5T and O3T atoms — no bond info available (mirrors PDB parser behaviour)
            if (atom.atomName == "O5T" || atom.atomName == "O3T") {
                messages.add("MmCifAtomSiteHandler: atom ${atom.atomName} skipped (no bond info)")
                continue
            }

            // Accumulate for centroid (only accepted atoms)
            sumX += vx
            sumY += vy
            sumZ += vz
            atomCount++

            mol.atomNumberToAtomInfoHash[atom.atomNumber] = atom
            mol.atomNumberList.add(atom.atomNumber)
            mol.maxAtomNumber = maxOf(mol.maxAtomNumber, atom.atomNumber)
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Reads exactly [nCols] value tokens from [tokenizer] to form one data row.
     * Returns null (and pushes back the sentinel) if a non-value token is encountered,
     * signalling that the loop has ended.
     */
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

    private fun parseFloat(s: String): Float =
        try { s.toFloat() } catch (_: NumberFormatException) { 0f }

    private fun parseInteger(s: String): Int =
        try { s.toInt() } catch (_: NumberFormatException) { 0 }
}
