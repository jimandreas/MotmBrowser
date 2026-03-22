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
 * Tests MmCifFormatDetector and MoleculeParserFactory format routing.
 */
internal class MmCifFormatDetectionTest {

    @Test
    @DisplayName("data_ at line start → detected as mmCIF")
    fun testMmCifDetected() {
        val content = "data_4HHB\nloop_\n_atom_site.id\n1\n"
        val (isMmCif, _) = MmCifFormatDetector.detect(content.byteInputStream())
        assertTrue(isMmCif, "data_ prefix should indicate mmCIF format")
    }

    @Test
    @DisplayName("ATOM record at line start → detected as PDB")
    fun testPdbAtomDetected() {
        val content = "ATOM      1  CA  ALA A   1      1.000   2.000   3.000  1.00  0.00           C  \n"
        val (isMmCif, _) = MmCifFormatDetector.detect(content.byteInputStream())
        assertFalse(isMmCif, "ATOM record prefix should indicate PDB format")
    }

    @Test
    @DisplayName("HEADER record at line start → detected as PDB")
    fun testPdbHeaderDetected() {
        val content = "HEADER    OXYGEN TRANSPORT                        22-JAN-84   4HHB              \n"
        val (isMmCif, _) = MmCifFormatDetector.detect(content.byteInputStream())
        assertFalse(isMmCif, "HEADER record should indicate PDB format")
    }

    @Test
    @DisplayName("REMARK record at line start → detected as PDB")
    fun testPdbRemarkDetected() {
        val content = "REMARK   1 REFERENCE 1\n"
        val (isMmCif, _) = MmCifFormatDetector.detect(content.byteInputStream())
        assertFalse(isMmCif, "REMARK record should indicate PDB format")
    }

    @Test
    @DisplayName("reconstituted stream is complete after detection")
    fun testReconstitutedStreamIsComplete() {
        val original = "data_TEST\nloop_\n_atom_site.id\n1\n"
        val (_, stream) = MmCifFormatDetector.detect(original.byteInputStream())
        val content = stream.bufferedReader().readText()
        assertTrue(content.contains("data_TEST"), "Reconstituted stream should contain original content")
        assertTrue(content.contains("loop_"),     "Reconstituted stream should contain loop_ token")
    }

    @Test
    @DisplayName("MoleculeParserFactory routes mmCIF to mmCIF parser")
    fun testFactoryRoutesMmCif() {
        val mmCifContent = """
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
ATOM 1 N N ALA A 1 0.0 0.0 0.0 1
        """.trimIndent()

        val mol = Molecule()
        MoleculeParserFactory.parse(mol, mmCifContent.byteInputStream(), "TEST")

        assertEquals(1, mol.atomNumberToAtomInfoHash.size)
        assertEquals("N",   mol.atomNumberToAtomInfoHash[1]!!.atomName)
        assertEquals("ALA", mol.atomNumberToAtomInfoHash[1]!!.residueName)
    }

    @Test
    @DisplayName("MoleculeParserFactory routes PDB to PDB parser")
    fun testFactoryRoutesPdb() {
        val pdbContent = "ATOM      1  N   ALA A   1       0.000   0.000   0.000  1.00  0.00           N  \n"

        val mol = Molecule()
        MoleculeParserFactory.parse(mol, pdbContent.byteInputStream(), "TEST")

        assertEquals(1, mol.atomNumberToAtomInfoHash.size)
        assertEquals("N",   mol.atomNumberToAtomInfoHash[1]!!.atomName)
        assertEquals("ALA", mol.atomNumberToAtomInfoHash[1]!!.residueName)
    }
}
