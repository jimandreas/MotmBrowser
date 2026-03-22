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
 * Mirror of AtomCoordTest01 for the mmCIF parser.
 * Tests basic atom parsing with a single atom at the origin.
 */
internal class MmCifAtomCoordTest01 {

    private val mmCifAtZero = """
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
ATOM 1 O O5' DC A 1 0.000 0.000 0.000 1
    """.trimIndent()

    @Test
    @DisplayName("single atom at origin: verify atom fields and zero centering magnitude")
    fun testSingleAtomAtOrigin() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(mmCifAtZero.byteInputStream())
            .parse()

        assertEquals(1, mol.maxAtomNumber)

        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(1, atoms.size)

        val atom = atoms[1]
        assertNotNull(atom)
        assertEquals("O5'", atom!!.atomName)
        assertEquals("DC",  atom.residueName)
        assertEquals('A',   atom.chainId)
        assertEquals(1,     atom.residueSeqNumber)
        assertEquals(PdbAtom.AtomType.IS_ATOM, atom.atomType)

        val maxVector = mol.maxPostCenteringVectorMagnitude
        assertEquals(0.0f, maxVector, 0.01f)
    }

    @Test
    @DisplayName("HETATM group_PDB value maps to IS_HETATM atom type")
    fun testHetatmAtomType() {
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
HETATM 1 O O HEM A 500 1.000 2.000 3.000 1
        """.trimIndent()

        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(input.byteInputStream())
            .parse()

        assertEquals(1, mol.atomNumberToAtomInfoHash.size)
        assertEquals(PdbAtom.AtomType.IS_HETATM, mol.atomNumberToAtomInfoHash[1]!!.atomType)
    }
}
