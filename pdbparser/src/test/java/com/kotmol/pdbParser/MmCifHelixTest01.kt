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
 * Tests parsing of _struct_conf loop data into mol.helixList.
 *
 * HELX_* rows → PdbHelix entries.
 * STRN and TURN_P rows → ignored.
 */
internal class MmCifHelixTest01 {

    // One HELX_P helix (residues A 10–25) and one STRN (should be ignored)
    private val structConfInput = """
data_TEST
loop_
_struct_conf.conf_type_id
_struct_conf.id
_struct_conf.beg_label_asym_id
_struct_conf.beg_label_seq_id
_struct_conf.end_label_asym_id
_struct_conf.end_label_seq_id
_struct_conf.beg_auth_asym_id
_struct_conf.beg_auth_seq_id
_struct_conf.end_auth_asym_id
_struct_conf.end_auth_seq_id
HELX_P HELX_P1 A 10 A 25 A 10 A 25
STRN   STRN1   A 30 A 40 A 30 A 40
    """.trimIndent()

    @Test
    @DisplayName("HELX_P row parsed; STRN row ignored")
    fun testHelixParsedStrnIgnored() {
        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(structConfInput.byteInputStream())
            .doBondProcessing(false)
            .parse()

        assertEquals(1, mol.helixList.size)
        val helix = mol.helixList[0]
        assertEquals('A', helix.initialChainIdChar)
        assertEquals(10,  helix.initialResidueNumber)
        assertEquals('A', helix.terminalChainIdChar)
        assertEquals(25,  helix.terminalResidueNumber)
    }

    @Test
    @DisplayName("multiple helices parsed in order")
    fun testMultipleHelices() {
        val input = """
data_TEST
loop_
_struct_conf.conf_type_id
_struct_conf.id
_struct_conf.beg_auth_asym_id
_struct_conf.beg_auth_seq_id
_struct_conf.end_auth_asym_id
_struct_conf.end_auth_seq_id
HELX_P HELX_P1 A  5 A 15
HELX_P HELX_P2 B 20 B 35
TURN_P TURN1   A 40 A 44
        """.trimIndent()

        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(input.byteInputStream())
            .doBondProcessing(false)
            .parse()

        assertEquals(2, mol.helixList.size)
        assertEquals(5,  mol.helixList[0].initialResidueNumber)
        assertEquals('B', mol.helixList[1].initialChainIdChar)
        assertEquals(20, mol.helixList[1].initialResidueNumber)
    }

    @Test
    @DisplayName("helixId is populated from id column")
    fun testHelixId() {
        val input = """
data_TEST
loop_
_struct_conf.conf_type_id
_struct_conf.id
_struct_conf.beg_auth_asym_id
_struct_conf.beg_auth_seq_id
_struct_conf.end_auth_asym_id
_struct_conf.end_auth_seq_id
HELX_RH_AL_P HELX_RH_AL_P1 A 1 A 10
        """.trimIndent()

        val mol = Molecule()
        ParserMmCifFile
            .Builder(mol)
            .loadMmCifFromStream(input.byteInputStream())
            .doBondProcessing(false)
            .parse()

        assertEquals(1, mol.helixList.size)
        assertEquals("HELX_RH_AL_P1", mol.helixList[0].helixId)
    }
}
