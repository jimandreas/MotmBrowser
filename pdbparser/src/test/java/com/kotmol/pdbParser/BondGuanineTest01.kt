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

internal class BondGuanineTest01 {

    /**
     * 1BNA.pdb
     *   Just the first Guanine entry is used in this test.
     */
    lateinit var str : ByteArrayInputStream
    lateinit var aModelTest : String
    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        val theModelString = """
         1         2         3         4         5         6         7
12345678901234567890123456789012345678901234567890123456789012345678901234567890"""
        aModelTest = """
ATOM     17  P    DG A   2      22.409  31.286  21.483  1.00 58.85           P  
ATOM     18  OP1  DG A   2      23.536  32.157  21.851  1.00 57.82           O  
ATOM     19  OP2  DG A   2      21.822  31.459  20.139  1.00 78.33           O  
ATOM     20  O5'  DG A   2      22.840  29.751  21.498  1.00 40.36           O  
ATOM     21  C5'  DG A   2      23.543  29.175  22.594  1.00 47.19           C  
ATOM     22  C4'  DG A   2      23.494  27.709  22.279  1.00 47.81           C  
ATOM     23  O4'  DG A   2      22.193  27.252  22.674  1.00 38.76           O  
ATOM     24  C3'  DG A   2      23.693  27.325  20.807  1.00 28.58           C  
ATOM     25  O3'  DG A   2      24.723  26.320  20.653  1.00 40.44           O  
ATOM     26  C2'  DG A   2      22.273  26.885  20.416  1.00 21.14           C  
ATOM     27  C1'  DG A   2      21.721  26.304  21.716  1.00 33.95           C  
ATOM     28  N9   DG A   2      20.237  26.470  21.780  1.00 34.00           N  
ATOM     29  C8   DG A   2      19.526  27.584  21.429  1.00 36.47           C  
ATOM     30  N7   DG A   2      18.207  27.455  21.636  1.00 32.37           N  
ATOM     31  C5   DG A   2      18.083  26.212  22.142  1.00 15.06           C  
ATOM     32  C6   DG A   2      16.904  25.525  22.545  1.00 11.88           C  
ATOM     33  O6   DG A   2      15.739  25.916  22.518  1.00 21.30           O  
ATOM     34  N1   DG A   2      17.197  24.279  23.037  1.00 15.44           N  
ATOM     35  C2   DG A   2      18.434  23.717  23.155  1.00  9.63           C  
ATOM     36  N2   DG A   2      18.508  22.456  23.668  1.00 16.69           N  
ATOM     37  N3   DG A   2      19.537  24.360  22.770  1.00 30.98           N  
ATOM     38  C4   DG A   2      19.290  25.594  22.274  1.00 18.56           C  
TER      39                                                                            
END   
        """.trimIndent()

        str = aModelTest.byteInputStream()

    }

    /* Guanine:
     * Parameters for all of these force fields may be downloaded from the Mackerell website for free.
* http://mackerell.umaryland.edu/charmm_ff.shtml

     *               O6
     *               ||
     *               C6
     *              /  \
     *          H1-N1   C5--N7\\
     *             |    ||     C8-H8
     *             C2   C4--N9/
     *            / \\ /      \
     *      H21-N2   N3        \
     *          |               \
     *         H22               \
     *                            \
     *        OP1    H5' H4'  O4'  \
     *         |      |    \ /   \  \
     *        -P-O5'-C5'---C4'    C1'
     *         |      |     \     / \
     *        OP2    H5''   C3'--C2' H1'
     *                     / \   / \
     *                  O3' H3' O2' H2'' + H2'
     *                   |       |
     *                          HO2'
 
     */

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        str.close()
    }

    /**
     * make sure there are no bonds between the two residue - i.e. the TER record
     * stops the bond processing even though the residue numbers are the same
     */
    @Test
    @DisplayName( "test processing of Guanine bonds")
    fun testTERrecordProcessing() {

        val mol = Molecule()
        val messages : MutableList<String> = mutableListOf()

        ParserPdbFile
                .Builder(mol)
                .setMoleculeName("1BNA")
                .setMessageStrings(messages)
                .loadPdbFromStream(str)
                .parse()

        //
        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(23, atoms.size)

        // checked bond list by examination - seems OK (relative to asciidraw above)
        assertEquals(24, mol.bondList.size)

        // check that the TER record is entered into the
        // atom list as a TER_RECORD type.
        val shouldBeTER = mol.atomNumberToAtomInfoHash[39]
        assertNotNull(shouldBeTER)
        assertEquals(PdbAtom.AtomType.IS_TER_RECORD, shouldBeTER!!.atomType)

    }

}