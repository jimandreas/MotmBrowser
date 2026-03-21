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

/**
 * Test processing of atom x/y/x coordinates
 *
 * TODO: The max function needs work
 *
 */
internal class AtomCoordTest04 {

    lateinit var str00 : ByteArrayInputStream
    val mol = Molecule()
    val parse = ParserPdbFile(mol)

    @org.junit.jupiter.api.BeforeEach
    fun setUp() { // from 1bna.pdb
        val anAtomZero = """
ATOM      1  O5'  DC A   1      00.000  00.000  00.000  1.00 64.35           O  
ATOM      2  C5'  DC A   1      00.000 -10.000  00.000  1.00 44.69           C  
ATOM      3  C4'  DC A   1      00.000  00.000 -10.000  1.00 31.28           C 
        """.trimIndent()

        str00 = anAtomZero.byteInputStream()
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        str00.close()
    }

    @Test
    @DisplayName( "test with a 10, 10, 10 max coordinate atom")
    fun testWithZeroCoordinateAtom() {

        var messages : MutableList<String> = mutableListOf()

        ParserPdbFile
                .Builder(mol)
                .loadPdbFromStream(str00)
                .parse()

        assertEquals(3, mol.maxAtomNumber)

        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(3, atoms.size)

//        val maxCoord = mol.maxCoordinate
//        assertEquals(maxCoord, 10.0)
    }
}