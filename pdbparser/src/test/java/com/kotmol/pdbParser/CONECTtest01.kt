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

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

// https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/

internal class CONECTtest01 {

    /**
     * 5W1O -
     *    has CONECT records with no spaces.   Check that the HETATMs are
     *    properly CONECT'ed
     */
    lateinit var str : ByteArrayInputStream
    lateinit var aModelTest : String
    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        val theModelString = """
         1         2         3         4         5         6         7
12345678901234567890123456789012345678901234567890123456789012345678901234567890"""
        aModelTest = """
HETATM17079  S   IDS E 512       8.437   1.398  80.249  1.00 31.19           S  
HETATM17080  O1S IDS E 512       9.147   0.259  79.768  1.00 31.82           O  
HETATM17081  O2S IDS E 512       7.206   1.638  79.577  1.00 33.46           O  
HETATM17082  O3S IDS E 512       9.251   2.557  80.323  1.00 30.40           O  
CONECT1708017079                                                                
CONECT1708117079                                                                
CONECT1708217079                                                                                  
END    
        """.trimIndent()

        str = aModelTest.byteInputStream()

    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        str.close()
    }

    /**
     *
     */
    @Test
    @DisplayName( "test processing of a TER record")
    fun testModelSkipping() {

        val mol = Molecule()
        val messages : MutableList<String> = mutableListOf()

        ParserPdbFile
                .Builder(mol)
                .setMoleculeName("5W1O")
                .setMessageStrings(messages)
                .loadPdbFromStream(str)
                .parse()

        // four atoms with three CONECT records
        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(4, atoms.size)

        // bonds from the two records don't overlap
        assertEquals(3, mol.bondList.size)

        // TODO: check the Bond list

    }
}