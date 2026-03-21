/*
 *  Copyright 2020 James Andreas
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

import com.kotmol.pdbParser.ChainRenderingDescriptor.NucleicType.NOT_A_NUCLEIC_TYPE
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

// https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/

internal class RibbonCAonlyTest {

    /**
     * @link https://proteopedia.org/wiki/index.php/Unusual_sequence_numbering
     * 1IGY - residue 82 is distingushed by the insertion codes
     */
    lateinit var str : ByteArrayInputStream
    lateinit var anAtom : String
    @org.junit.jupiter.api.BeforeEach
    fun setUp() { // from 3ixz CA only
        val numbering = """
         1         2         3         4         5         6         7
12345678901234567890123456789012345678901234567890123456789012345678901234567890"""
        anAtom = """
ATOM      1  CA  LYS A  36      38.866  60.745 -26.642  1.00  0.00           C  
ATOM      2  CA  ARG A  37      40.620  57.631 -25.579  1.00  0.00           C  
ATOM      3  CA  LYS A  38      38.386  54.647 -26.046  1.00  0.00           C  
ATOM      4  CA  GLU A  39      40.092  53.260 -23.023  1.00  0.00           C  
ATOM      5  CA  LYS A  40      38.483  55.954 -20.912  1.00  0.00           C  
ATOM      6  CA  LEU A  41      35.218  56.618 -22.675  1.00  0.00           C  
ATOM      7  CA  GLU A  42      34.406  52.949 -23.237  1.00  0.00           C  
ATOM      8  CA  ASN A  43      36.549  51.336 -20.567  1.00  0.00           C  
ATOM      9  CA  MET A  44      35.713  53.288 -17.402  1.00  0.00           C  
        """.trimIndent()

        str = anAtom.byteInputStream()

    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        str.close()
    }

    @Test
    @DisplayName( "test a CA only PDB file for only a ribbon")
    fun testCAonlyPDBfile() {

        val mol = Molecule()
        var messages : MutableList<String> = mutableListOf()

        ParserPdbFile
                .Builder(mol)
                .setMessageStrings(messages)
                .loadPdbFromStream(str)
                .parse()

        assertEquals(9, mol.maxAtomNumber)

        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(9, atoms.size)

        // chain list is also 1 long - has one chain with 9 atoms in the chain
        val chainDescList = mol.listofChainDescriptorLists
        assertEquals(1, chainDescList.size)

        /*
         * there is only one chain in this short PDB
         *    Walk this chain and verify that the guide atom (CA only)
         * is equivalent to the atom number.
         */
        val chain = chainDescList[0]
        for (item in chain.withIndex()) {
            assertEquals(NOT_A_NUCLEIC_TYPE, item.value.nucleicType)
            assertNull(item.value.startAtom)
            assertNull(item.value.endAtom)
            val backbone = item.value.backboneAtom
            val guide = item.value.guideAtom
            assertNotNull(backbone)
            //assertNotNull(guide)
            assertEquals(item.index+1, backbone!!.atomNumber)
            //assertEquals(item.index+1, guide!!.atomNumber)

        }


    }
}