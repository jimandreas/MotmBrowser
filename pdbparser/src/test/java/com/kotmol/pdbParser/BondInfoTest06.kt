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

internal class BondInfoTest06 {

    /**
     * 4AJX - contains H1/H2/H3 atoms bonded to the N atom
     *    The H1 / H3 atoms were missing from the Bond database.
     *    Use these examples to add H1 / H3 to the bond info
     *    and test that they are bonded.
     */
    lateinit var str : ByteArrayInputStream
    lateinit var aModelTest : String
    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        val theModelString = """
         1         2         3         4         5         6         7
12345678901234567890123456789012345678901234567890123456789012345678901234567890"""
        aModelTest = """
ATOM      1  N   GLY A   1      53.224  -6.918  16.646  1.00 11.20           N
ATOM      2  CA  GLY A   1      52.194  -6.272  15.815  1.00  9.87           C
ATOM      3  C   GLY A   1      50.937  -6.027  16.630  1.00  9.81           C
ATOM      4  O   GLY A   1      50.794  -6.512  17.771  1.00  9.85           O
ATOM      5  HA2 GLY A   1      52.524  -5.423  15.481  1.00 11.85           H  
ATOM      6  HA3 GLY A   1      51.974  -6.842  15.061  1.00 11.85           H  
ATOM      7  H1  GLY A   1      52.824  -7.242  17.457  1.00 13.44           H  
ATOM      8  H2  GLY A   1      53.610  -7.653  16.164  1.00 13.44           H  
ATOM      9  H3  GLY A   1      53.909  -6.281  16.860  1.00 13.44           H  
TER      10                                                                       
ATOM    362  N   PHE B   1      42.139 -16.805  13.560  1.00 21.72           N
ATOM    363  CA  PHE B   1      40.918 -17.256  14.255  1.00 19.54           C
ATOM    364  C   PHE B   1      39.851 -16.185  14.096  1.00 16.61           C
ATOM    365  O   PHE B   1      40.149 -14.987  13.946  1.00 16.04           O
ATOM    366  CB  PHE B   1      41.206 -17.513  15.745  1.00 21.28           C
ATOM    367  CG  PHE B   1      42.194 -18.626  15.997  1.00 24.24           C
ATOM    368  CD1 PHE B   1      41.841 -19.948  15.781  1.00 23.78           C
ATOM    369  CD2 PHE B   1      43.468 -18.352  16.479  1.00 26.04           C
ATOM    370  CE1 PHE B   1      42.738 -20.971  16.015  1.00 26.54           C
ATOM    371  CE2 PHE B   1      44.370 -19.370  16.734  1.00 25.59           C
ATOM    372  CZ  PHE B   1      44.008 -20.677  16.503  1.00 26.98           C
ATOM    373  HA  PHE B   1      40.594 -18.087  13.850  1.00 23.45           H  
ATOM    374  HB2 PHE B   1      41.567 -16.703  16.138  1.00 25.53           H  
ATOM    375  HB3 PHE B   1      40.376 -17.750  16.187  1.00 25.53           H  
ATOM    376  HD1 PHE B   1      40.992 -20.148  15.458  1.00 28.54           H  
ATOM    377  HD2 PHE B   1      43.716 -17.470  16.640  1.00 31.25           H  
ATOM    378  HE1 PHE B   1      42.489 -21.854  15.863  1.00 31.85           H  
ATOM    379  HE2 PHE B   1      45.221 -19.170  17.050  1.00 30.71           H  
ATOM    380  HZ  PHE B   1      44.615 -21.363  16.663  1.00 32.38           H  
ATOM    381  H1  PHE B   1      41.924 -16.075  12.976  1.00 26.07           H  
ATOM    382  H2  PHE B   1      42.788 -16.517  14.207  1.00 26.07           H  
ATOM    383  H3  PHE B   1      42.501 -17.531  13.046  1.00 26.07           H  
TER     384                                                                            
END   
        """.trimIndent()

        str = aModelTest.byteInputStream()

    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        str.close()
    }

    /**
     * make sure there are no bonds between the two residue - i.e. the TER record
     * stops the bond processing even though the residue numbers are the same
     */
    @Test
    @DisplayName( "test processing of a TER record")
    fun testTERrecordProcessing() {

        val mol = Molecule()
        val messages : MutableList<String> = mutableListOf()

        ParserPdbFile
                .Builder(mol)
                .setMoleculeName("4AJX")
                .setMessageStrings(messages)
                .loadPdbFromStream(str)
                .parse()

        //
        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(33, atoms.size)

        // bonds from the two records don't overlap
        assertEquals(30, mol.bondList.size)

        // check that the TER record is entered into the
        // atom list as a TER_RECORD type.
        val shouldBeTER = mol.atomNumberToAtomInfoHash[10]
        assertNotNull(shouldBeTER)
        assertEquals(PdbAtom.AtomType.IS_TER_RECORD, shouldBeTER!!.atomType)

    }


    /**
     * make sure there are no bonds between the two residue - i.e. the TER record
     * stops the bond processing even though the residue numbers are the same
     */
    @Test
    @DisplayName( "test that bond processing is suppressed")
    fun testDoBondProcessingFlag() {

        val mol = Molecule()
        val messages : MutableList<String> = mutableListOf()

        ParserPdbFile
                .Builder(mol)
                .setMoleculeName("4AJX")
                .setMessageStrings(messages)
                .loadPdbFromStream(str)
                .doBondProcessing(false)  // skip over bond processing
                .parse()

        //
        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(33, atoms.size)

        // should have no bonds in bond list
        assertEquals(0, mol.bondList.size)
    }
}