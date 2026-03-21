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

internal class ResidueInsertionCodeTest02 {

    /**
     * 2R6P - a CA only PDB model
     */
    lateinit var str : ByteArrayInputStream
    lateinit var anAtom : String
    @org.junit.jupiter.api.BeforeEach
    fun setUp() { // from 2r6p
        val numbering = """
         1         2         3         4         5         6         7
12345678901234567890123456789012345678901234567890123456789012345678901234567890"""
        anAtom = """
ATOM      1  CA  MET A   1      93.887  11.659 215.822  1.00 70.94           C  
ATOM      2  CA  ARG A   2      90.531  10.195 214.833  1.00 72.65           C  
ATOM      3  CA  CYS A   3      91.669   6.554 215.139  1.00 63.88           C  
ATOM      4  CA  ILE A   4      92.293   6.900 218.830  1.00 73.59           C  
ATOM      5  CA  GLY A   5      89.515   5.137 220.704  1.00 75.00           C  
ATOM      6  CA  ILE A   6      88.514   2.427 217.829  1.00 75.00           C  
ATOM      7  CA  SER A   7      89.320  -0.852 218.358  1.00 75.00           C  
ATOM      8  CA  ASN A   8      89.792  -2.127 214.851  1.00 75.00           C  
ATOM      9  CA  ARG A   9      92.907   0.305 214.038  1.00 73.62           C  
        """.trimIndent()

        str = anAtom.byteInputStream()

    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        str.close()
    }

    @Test
    @DisplayName( "test only CA atoms in PDB")
    fun testOnlyCAatomsInPDB() {

        val mol = Molecule()
        val messages : MutableList<String> = mutableListOf()

        ParserPdbFile
                .Builder(mol)
                .setMoleculeName("2R6P")
                .setMessageStrings(messages)
                .loadPdbFromStream(str)
                .parse()

        assertEquals(9, mol.maxAtomNumber)

        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(9, atoms.size)

//        val firstAtomNumber = mol.numList[0]
//        val firstAtom = mol.atoms[firstAtomNumber]
//        assertNotNull(firstAtom)
//        assertEquals("HE2", firstAtom!!.atomName)


    }
}