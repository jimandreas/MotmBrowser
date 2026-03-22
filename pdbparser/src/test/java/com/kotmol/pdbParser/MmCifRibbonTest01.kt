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
 * Mirror of RibbonCAonlyTest for the mmCIF parser.
 *
 * 9 CA-only atoms in a single protein chain should produce exactly one chain
 * descriptor list with 9 entries, each with a non-null backboneAtom.
 */
internal class MmCifRibbonTest01 {

    // 9 CA-only atoms: residues 36–44 in chain A (same as RibbonCAonlyTest)
    private val caOnlyMmCif = """
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
ATOM 1 C CA LYS A 36  38.866  60.745 -26.642 1
ATOM 2 C CA ARG A 37  40.620  57.631 -25.579 1
ATOM 3 C CA LYS A 38  38.386  54.647 -26.046 1
ATOM 4 C CA GLU A 39  40.092  53.260 -23.023 1
ATOM 5 C CA LYS A 40  38.483  55.954 -20.912 1
ATOM 6 C CA LEU A 41  35.218  56.618 -22.675 1
ATOM 7 C CA GLU A 42  34.406  52.949 -23.237 1
ATOM 8 C CA ASN A 43  36.549  51.336 -20.567 1
ATOM 9 C CA MET A 44  35.713  53.288 -17.402 1
    """.trimIndent()

    @Test
    @DisplayName("9 CA atoms produce one chain with 9 ribbon nodes")
    fun testCaOnlyRibbon() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(caOnlyMmCif.byteInputStream())
            .parse()

        assertEquals(9, mol.maxAtomNumber)
        assertEquals(9, mol.atomNumberToAtomInfoHash.size)

        val chainDescList = mol.listofChainDescriptorLists
        assertEquals(1, chainDescList.size, "Expected exactly one chain")

        val chain = chainDescList[0]
        assertEquals(9, chain.size, "Chain should have 9 ribbon nodes")
    }

    @Test
    @DisplayName("each ribbon node has a non-null backboneAtom with sequential atom number")
    fun testBackboneAtomOrder() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(caOnlyMmCif.byteInputStream())
            .parse()

        val chain = mol.listofChainDescriptorLists[0]
        for (item in chain.withIndex()) {
            val backbone = item.value.backboneAtom
            assertNotNull(backbone, "Backbone atom at index ${item.index} should not be null")
            assertEquals(item.index + 1, backbone!!.atomNumber,
                "Atom number should match sequential index + 1")
        }
    }
}
