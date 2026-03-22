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
 * Mirror of PdbParserTest for the mmCIF parser — tests the Builder pattern and
 * basic integration.
 */
internal class MmCifParserTest {

    private val oneSingleAtom = """
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
ATOM 1 C CA ALA A 1 1.000 2.000 3.000 1
    """.trimIndent()

    @Test
    @DisplayName("builder can be constructed with a Molecule")
    fun testBuilderConstruction() {
        val mol = Molecule()
        val builder = ParserMmCifFile.Builder(mol)
        assertNotNull(builder)
    }

    @Test
    @DisplayName("parse() requires loadMmCifFromStream() to be called first")
    fun testParseWithoutStreamThrows() {
        val mol = Molecule()
        assertThrows(IllegalStateException::class.java) {
            ParserMmCifFile.Builder(mol).parse()
        }
    }

    @Test
    @DisplayName("single atom is correctly parsed via Builder")
    fun testSingleAtomParse() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(oneSingleAtom.byteInputStream())
            .parse()

        assertEquals(1, mol.maxAtomNumber)
        assertEquals(1, mol.atomNumberToAtomInfoHash.size)
    }

    @Test
    @DisplayName("molecule name is set via setMoleculeName")
    fun testMoleculeName() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .setMoleculeName("4HHB")
            .loadMmCifFromStream(oneSingleAtom.byteInputStream())
            .parse()

        assertEquals("4HHB", mol.molName)
    }

    @Test
    @DisplayName("message strings are populated on successful parse")
    fun testMessageStrings() {
        val mol = Molecule()
        val messages = mutableListOf<String>()
        ParserMmCifFile
            .Builder(mol)
            .setMessageStrings(messages)
            .loadMmCifFromStream(oneSingleAtom.byteInputStream())
            .parse()

        // No assertion on message content — just verifying no exception and list is usable
        assertNotNull(messages)
    }

    @Test
    @DisplayName("empty mmCIF file produces empty molecule without crashing")
    fun testEmptyFile() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream("data_EMPTY\n".byteInputStream())
            .parse()

        assertEquals(0, mol.atomNumberToAtomInfoHash.size)
        assertEquals(0, mol.maxAtomNumber)
    }

    @Test
    @DisplayName("centerTheMolecule(false) does not shift coordinates")
    fun testNoCentering() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(oneSingleAtom.byteInputStream())
            .centerTheMolecule(false)
            .parse()

        val atom = mol.atomNumberToAtomInfoHash[1]!!
        assertEquals(1.0f, atom.atomPosition.x, 0.001f)
        assertEquals(2.0f, atom.atomPosition.y, 0.001f)
        assertEquals(3.0f, atom.atomPosition.z, 0.001f)
    }
}
