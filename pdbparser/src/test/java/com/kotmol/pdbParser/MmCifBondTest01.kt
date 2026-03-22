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
 * Tests that bond processing (via the copied mapBonds / matchBonds logic) produces the
 * correct bond count for a complete standard amino acid residue.
 *
 * Uses alanine (ALA) which has 5 heavy atoms (N, CA, C, O, CB) and 4 bonds in the
 * BondInfo lookup table.
 */
internal class MmCifBondTest01 {

    // Complete ALA residue: N, CA, C, O, CB
    private val alaResidue = """
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
ATOM 1 N N   ALA A 1 1.000 2.000 3.000 1
ATOM 2 C CA  ALA A 1 2.000 3.000 4.000 1
ATOM 3 C C   ALA A 1 3.000 4.000 5.000 1
ATOM 4 O O   ALA A 1 4.000 5.000 6.000 1
ATOM 5 C CB  ALA A 1 5.000 6.000 7.000 1
    """.trimIndent()

    @Test
    @DisplayName("ALA residue: 5 atoms parsed")
    fun testAlaAtomCount() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(alaResidue.byteInputStream())
            .doBondProcessing(true)
            .parse()

        assertEquals(5, mol.atomNumberToAtomInfoHash.size)
    }

    @Test
    @DisplayName("ALA residue: bond list is non-empty after bond processing")
    fun testAlaBondsNonEmpty() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(alaResidue.byteInputStream())
            .doBondProcessing(true)
            .parse()

        assertTrue(mol.bondList.isNotEmpty(),
            "Bond list should be non-empty for a complete ALA residue")
    }

    @Test
    @DisplayName("ALA residue: unbonded atom count is 0 for complete residue")
    fun testAlaUnbondedCount() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(alaResidue.byteInputStream())
            .doBondProcessing(true)
            .parse()

        assertEquals(0, mol.unbondedAtomCount,
            "All atoms in a complete ALA residue should be bonded")
    }

    @Test
    @DisplayName("bond processing can be disabled")
    fun testNoBondProcessing() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(alaResidue.byteInputStream())
            .doBondProcessing(false)
            .parse()

        assertEquals(5, mol.atomNumberToAtomInfoHash.size)
        assertTrue(mol.bondList.isEmpty(), "Bond list should be empty when doBondProcessing=false")
    }
}
