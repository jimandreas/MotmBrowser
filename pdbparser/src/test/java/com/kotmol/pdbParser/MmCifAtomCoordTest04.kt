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
 * Tests model-number filtering (NMR ensembles).
 *
 * When pdbx_PDB_model_num is present, only atoms from model 1 should be loaded.
 */
internal class MmCifAtomCoordTest04 {

    // Atom 1: model 1 → ACCEPT
    // Atom 2: model 2 → REJECT
    // Atom 3: model 1 → ACCEPT
    private val multiModelInput = """
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
ATOM 1 N CA ALA A 1 1.0 1.0 1.0 1
ATOM 2 N CA ALA A 1 2.0 2.0 2.0 2
ATOM 3 N CA GLY A 2 3.0 3.0 3.0 1
    """.trimIndent()

    @Test
    @DisplayName("model 2 atoms excluded; only model 1 accepted")
    fun testModelFiltering() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(multiModelInput.byteInputStream())
            .parse()

        assertEquals(2, mol.atomNumberToAtomInfoHash.size)
        assertNotNull(mol.atomNumberToAtomInfoHash[1])
        assertNull(mol.atomNumberToAtomInfoHash[2])
        assertNotNull(mol.atomNumberToAtomInfoHash[3])
    }

    @Test
    @DisplayName("when pdbx_PDB_model_num column absent, all atoms are accepted")
    fun testNoModelColumnAcceptsAll() {
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
ATOM 1 N CA ALA A 1 0.0 0.0 0.0
ATOM 2 N CA GLY A 2 1.0 1.0 1.0
        """.trimIndent()

        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(input.byteInputStream())
            .parse()

        assertEquals(2, mol.atomNumberToAtomInfoHash.size)
    }
}
