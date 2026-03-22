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

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Tests that label_* columns are used as fallback when auth_* columns are absent.
 */
internal class MmCifAtomCoordTest02 {

    /**
     * File contains only label_* columns (no auth_* columns).
     * The handler should fall back to label_atom_id, label_comp_id, etc.
     */
    private val labelOnlyMmCif = """
data_TEST
loop_
_atom_site.group_PDB
_atom_site.id
_atom_site.type_symbol
_atom_site.label_atom_id
_atom_site.label_comp_id
_atom_site.label_asym_id
_atom_site.label_seq_id
_atom_site.Cartn_x
_atom_site.Cartn_y
_atom_site.Cartn_z
_atom_site.pdbx_PDB_model_num
ATOM 1 N N ALA A 1 1.000 2.000 3.000 1
    """.trimIndent()

    @Test
    @DisplayName("label_* fallback: atom parsed correctly when auth_* columns are missing")
    fun testLabelFallback() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(labelOnlyMmCif.byteInputStream())
            .parse()

        assertEquals(1, mol.atomNumberToAtomInfoHash.size)
        val atom = mol.atomNumberToAtomInfoHash[1]!!
        assertEquals("N",   atom.atomName)
        assertEquals("ALA", atom.residueName)
        assertEquals('A',   atom.chainId)
        assertEquals(1,     atom.residueSeqNumber)
    }

    @Test
    @DisplayName("auth_* columns take priority over label_* when both are present")
    fun testAuthPreferredOverLabel() {
        // auth values differ from label values — auth should win
        val input = """
data_TEST
loop_
_atom_site.group_PDB
_atom_site.id
_atom_site.type_symbol
_atom_site.label_atom_id
_atom_site.label_comp_id
_atom_site.label_asym_id
_atom_site.label_seq_id
_atom_site.auth_atom_id
_atom_site.auth_comp_id
_atom_site.auth_asym_id
_atom_site.auth_seq_id
_atom_site.Cartn_x
_atom_site.Cartn_y
_atom_site.Cartn_z
_atom_site.pdbx_PDB_model_num
ATOM 1 N Nlabel ALAlabel Alabel 1 Nauth ALAauth B 5 0.0 0.0 0.0 1
        """.trimIndent()

        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(input.byteInputStream())
            .parse()

        val atom = mol.atomNumberToAtomInfoHash[1]!!
        assertEquals("Nauth",    atom.atomName)
        assertEquals("ALAauth",  atom.residueName)
        assertEquals('B',        atom.chainId)
        assertEquals(5,          atom.residueSeqNumber)
    }
}
