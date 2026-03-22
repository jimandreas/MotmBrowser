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
 * Tests parsing of _struct_sheet_range loop data into mol.pdbSheetList.
 */
internal class MmCifSheetTest01 {

    // Two strands in the same sheet
    private val sheetInput = """
data_TEST
loop_
_struct_sheet_range.sheet_id
_struct_sheet_range.id
_struct_sheet_range.beg_label_asym_id
_struct_sheet_range.beg_label_seq_id
_struct_sheet_range.end_label_asym_id
_struct_sheet_range.end_label_seq_id
_struct_sheet_range.beg_auth_asym_id
_struct_sheet_range.beg_auth_seq_id
_struct_sheet_range.end_auth_asym_id
_struct_sheet_range.end_auth_seq_id
SHEET1 1 A 5  A 12 A 5  A 12
SHEET1 2 A 15 A 22 A 15 A 22
    """.trimIndent()

    @Test
    @DisplayName("two strands parsed into pdbSheetList")
    fun testTwoStrands() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(sheetInput.byteInputStream())
            .doBondProcessing(false)
            .parse()

        assertEquals(2, mol.pdbSheetList.size)
    }

    @Test
    @DisplayName("first strand has correct sheet ID and residue range")
    fun testFirstStrandFields() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(sheetInput.byteInputStream())
            .doBondProcessing(false)
            .parse()

        val strand1 = mol.pdbSheetList[0]
        assertEquals("SHEET1", strand1.sheetIdentification)
        assertEquals(1,        strand1.strandNumber)
        assertEquals('A',      strand1.initialChainIdChar)
        assertEquals(5,        strand1.initialResidueNumber)
        assertEquals('A',      strand1.terminalChainIdChar)
        assertEquals(12,       strand1.terminalResidueNumber)
    }

    @Test
    @DisplayName("second strand has correct residue range")
    fun testSecondStrandRange() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(sheetInput.byteInputStream())
            .doBondProcessing(false)
            .parse()

        val strand2 = mol.pdbSheetList[1]
        assertEquals(2,  strand2.strandNumber)
        assertEquals(15, strand2.initialResidueNumber)
        assertEquals(22, strand2.terminalResidueNumber)
    }

    @Test
    @DisplayName("label_* fallback used when auth_* columns absent")
    fun testLabelFallback() {
        val input = """
data_TEST
loop_
_struct_sheet_range.sheet_id
_struct_sheet_range.id
_struct_sheet_range.beg_label_asym_id
_struct_sheet_range.beg_label_seq_id
_struct_sheet_range.end_label_asym_id
_struct_sheet_range.end_label_seq_id
SHEET1 1 B 3 B 8
        """.trimIndent()

        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(input.byteInputStream())
            .doBondProcessing(false)
            .parse()

        assertEquals(1, mol.pdbSheetList.size)
        val sheet = mol.pdbSheetList[0]
        assertEquals('B', sheet.initialChainIdChar)
        assertEquals(3,   sheet.initialResidueNumber)
        assertEquals(8,   sheet.terminalResidueNumber)
    }
}
