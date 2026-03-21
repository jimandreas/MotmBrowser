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
 * Checking the centering function
 *
 */
internal class AtomCoordTest05 {

    lateinit var str00 : ByteArrayInputStream
    val mol = Molecule()
    val parse = ParserPdbFile(mol)

    @org.junit.jupiter.api.BeforeEach
    fun setUp() { // from 1bna.pdb
        val pdbWithOffset = """
ATOM   1032  OE1 GLU A 134      31.189 -27.598  29.448  1.00 40.66           O  
ATOM   1033  OE2 GLU A 134      32.782 -27.948  30.926  1.00 26.17           O

HETATM 1368 CD    CD A 188      30.071 -29.333  31.283  0.33 19.30          CD  

HETATM 1403  O   HOH A 358      30.428 -30.357  32.559  1.00 33.45           O  
        """.trimIndent()

        str00 = pdbWithOffset.byteInputStream()
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        str00.close()
    }

    @Test
    @DisplayName( "test centering with a short PDB")
    fun testWithZeroCoordinateAtom() {

        var messages : MutableList<String> = mutableListOf()

        ParserPdbFile
                .Builder(mol)
                .loadPdbFromStream(str00)
                .parse()

        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(4, atoms.size)
        val aveP = mol.averagePosition
        val maxP = mol.maxPostCenteringCoordinate
        val maxV = mol.maxPostCenteringVectorMagnitude

        assertEquals(30.0f, aveP.x, 3.0f)  // pre-centering ave X
        assertEquals(0.0f, maxP.x, 1.0f)  // post-centering ave X
        assertEquals(2.2f, maxV, 1.0f)  // post cenering max V

    }
}