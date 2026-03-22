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
 * Tests alternate-location filtering.
 *
 * Only atoms with label_alt_id of '.' (inapplicable) or 'A' should be accepted.
 * Atoms with label_alt_id of 'B', 'C', etc. should be excluded.
 */
internal class MmCifAtomCoordTest03 {

    // Atom 1: altLoc '.'  → ACCEPT
    // Atom 2: altLoc 'A'  → ACCEPT
    // Atom 3: altLoc 'B'  → REJECT
    private val altLocInput = """
data_TEST
loop_
_atom_site.group_PDB
_atom_site.id
_atom_site.type_symbol
_atom_site.label_alt_id
_atom_site.auth_atom_id
_atom_site.auth_comp_id
_atom_site.auth_asym_id
_atom_site.auth_seq_id
_atom_site.Cartn_x
_atom_site.Cartn_y
_atom_site.Cartn_z
_atom_site.pdbx_PDB_model_num
ATOM 1 N . CA ALA A 1 1.0 2.0 3.0 1
ATOM 2 N A CB ALA A 1 4.0 5.0 6.0 1
ATOM 3 N B CD ALA A 1 7.0 8.0 9.0 1
    """.trimIndent()

    @Test
    @DisplayName("altLoc '.' and 'A' accepted; 'B' rejected")
    fun testAltLocFiltering() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(altLocInput.byteInputStream())
            .parse()

        // Only atoms 1 and 2 should be present
        assertEquals(2, mol.atomNumberToAtomInfoHash.size)
        assertNotNull(mol.atomNumberToAtomInfoHash[1])
        assertNotNull(mol.atomNumberToAtomInfoHash[2])
        assertNull(mol.atomNumberToAtomInfoHash[3])
    }

    @Test
    @DisplayName("when no label_alt_id column, all atoms are accepted")
    fun testNoAltLocColumnAcceptsAll() {
        val input = """
data_TEST
loop_
_atom_site.group_PDB
_atom_site.id
_atom_site.type_symbol
_atom_site.auth_atom_id
_atom_site.auth_comp_id
_atom_site.auth_asym_id
_atom_site.auth_seq_id
_atom_site.Cartn_x
_atom_site.Cartn_y
_atom_site.Cartn_z
_atom_site.pdbx_PDB_model_num
ATOM 1 N CA ALA A 1 0.0 0.0 0.0 1
ATOM 2 C CB ALA A 1 1.0 1.0 1.0 1
        """.trimIndent()

        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(input.byteInputStream())
            .parse()

        assertEquals(2, mol.atomNumberToAtomInfoHash.size)
    }
}
