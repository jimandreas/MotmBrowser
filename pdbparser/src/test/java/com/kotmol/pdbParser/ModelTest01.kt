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

internal class ModelTest01 {

    /**
     * 1ZNF - a Zinc Finger PDB with 37 models (edited)
     */
    lateinit var str : ByteArrayInputStream
    lateinit var aModelTest : String
    @org.junit.jupiter.api.BeforeEach
    fun setUp() { // from 2r6p
        val theModelString = """
         1         2         3         4         5         6         7
12345678901234567890123456789012345678901234567890123456789012345678901234567890"""
        aModelTest = """
MODEL        1                                                                  
ATOM     55  SG  CYS A   3      -2.153  -3.317   0.825  1.00  0.00           S  
ATOM     91  SG  CYS A   6      -2.183  -5.291  -2.481  1.00  0.00           S  
ATOM    303  NE2 HIS A  19      -2.090  -1.975  -2.405  1.00  0.00           N  
ATOM    377  NE2 HIS A  23      -4.818  -2.975  -1.254  1.00  0.00           N  
TER     424      NH2 A  26                                                      
HETATM  425 ZN    ZN A  27      -2.880  -3.408  -1.345  1.00  0.00          ZN  
ENDMDL                                                                          
MODEL        2                                                                  
ATOM      7  N   TYR A   1       7.910  -2.401   1.328  1.00  0.00           N  
ENDMDL                                                                          
MODEL        3                                                                  
ATOM      8  CA  TYR A   1       6.945  -2.325   2.443  1.00  0.00           C  
ENDMDL                                                                          
CONECT  425   55   91  303  377                                                 
MASTER      251    0    3    1    2    1    1    615688   37   16    3          
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
    @DisplayName( "test that mol has only five atoms, does not include other models, has bonds to Zinc HETATM")
    fun testModelSkipping() {

        val mol = Molecule()
        val messages : MutableList<String> = mutableListOf()

        ParserPdbFile
                .Builder(mol)
                .setMoleculeName("1ZNF")
                .setMessageStrings(messages)
                .loadPdbFromStream(str)
                .parse()

        // only 5 + 1TER atoms, does not include later models
        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(5+1, atoms.size)

        // has the 4 bonds from the zinc atom to the previous 4 atoms
        assertEquals(4, mol.bondList.size)

    }
}