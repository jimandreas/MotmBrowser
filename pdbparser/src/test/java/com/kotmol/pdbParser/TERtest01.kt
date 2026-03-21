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
import com.kotmol.pdbParser.PdbAtom.AtomType.IS_TER_RECORD

// https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/

internal class TERtest01 {

    /**
     * 1ZNF - a Zinc Finger PDB with 37 models (edited)
     *    What is unique about this test case is that the same
     *    residue number is used across a TER record.  The residue names
     *    change.   The TER record asserts that the chain ends there.
     */
    lateinit var str : ByteArrayInputStream
    lateinit var aModelTest : String
    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        val theModelString = """
         1         2         3         4         5         6         7
12345678901234567890123456789012345678901234567890123456789012345678901234567890"""
        aModelTest = """
ATOM    204  P    DT E  12      41.677 -64.307   5.732  1.00122.09           P  
ATOM    205  OP1  DT E  12      41.863 -63.536   6.997  1.00122.09           O  
ATOM    206  OP2  DT E  12      40.540 -65.263   5.602  1.00122.09           O  
ATOM    207  O5'  DT E  12      41.490 -63.265   4.544  1.00122.09           O  
ATOM    208  C5'  DT E  12      41.284 -63.725   3.216  1.00122.09           C  
ATOM    209  C4'  DT E  12      41.127 -62.551   2.284  1.00122.09           C  
ATOM    210  O4'  DT E  12      42.352 -61.765   2.361  1.00122.09           O  
ATOM    211  C3'  DT E  12      41.055 -63.092   0.847  1.00122.09           C  
ATOM    212  O3'  DT E  12      40.589 -62.225  -0.194  1.00122.09           O  
ATOM    213  C2'  DT E  12      42.507 -63.144   0.452  1.00122.09           C  
ATOM    214  C1'  DT E  12      42.986 -61.878   1.111  1.00122.09           C  
ATOM    215  N1   DT E  12      44.435 -61.796   1.185  1.00122.09           N  
ATOM    216  C2   DT E  12      45.194 -62.908   1.513  1.00122.09           C  
ATOM    217  O2   DT E  12      44.746 -63.884   2.121  1.00121.05           O  
ATOM    218  N3   DT E  12      46.495 -62.834   1.058  1.00122.05           N  
ATOM    219  C4   DT E  12      47.050 -61.795   0.349  1.00122.09           C  
ATOM    220  O4   DT E  12      48.112 -61.938  -0.174  1.00122.09           O  
ATOM    221  C5   DT E  12      46.248 -60.610   0.228  1.00122.09           C  
ATOM    222  C7   DT E  12      46.823 -59.410  -0.452  1.00122.09           C  
ATOM    223  C6   DT E  12      45.009 -60.650   0.696  1.00122.09           C  
TER     224       DT E  12                                                      
ATOM    225  O5'  DA D  12      57.253 -61.992  -0.544  1.00117.97           O  
ATOM    226  C5'  DA D  12      57.447 -61.049  -1.619  1.00120.06           C  
ATOM    227  C4'  DA D  12      56.212 -60.189  -1.578  1.00122.09           C  
ATOM    228  O4'  DA D  12      55.088 -61.095  -1.431  1.00122.09           O  
ATOM    229  C3'  DA D  12      56.193 -59.244  -0.360  1.00122.09           C  
ATOM    230  O3'  DA D  12      55.679 -57.956  -0.705  1.00122.09           O  
ATOM    231  C2'  DA D  12      55.383 -59.973   0.710  1.00122.09           C  
ATOM    232  C1'  DA D  12      54.568 -60.901  -0.144  1.00122.09           C  
ATOM    233  N9   DA D  12      53.641 -61.943   0.293  1.00122.09           N  
ATOM    234  C8   DA D  12      53.592 -63.296   0.675  1.00122.09           C  
ATOM    235  N7   DA D  12      52.407 -63.697   1.027  1.00122.09           N  
ATOM    236  C5   DA D  12      51.648 -62.522   0.878  1.00122.09           C  
ATOM    237  C6   DA D  12      50.290 -62.250   1.103  1.00122.09           C  
ATOM    238  N6   DA D  12      49.433 -63.180   1.501  1.00122.09           N  
ATOM    239  N1   DA D  12      49.855 -60.973   0.861  1.00122.09           N  
ATOM    240  C2   DA D  12      50.735 -60.076   0.425  1.00122.09           C  
ATOM    241  N3   DA D  12      52.029 -60.220   0.194  1.00122.09           N  
ATOM    242  C4   DA D  12      52.392 -61.477   0.435  1.00122.09           C  
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
                .setMoleculeName("1ZNF")
                .setMessageStrings(messages)
                .loadPdbFromStream(str)
                .parse()

        // two residues separated by a TER record (see above)
        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(39, atoms.size)

        // bonds from the two records don't overlap
        assertEquals(41, mol.bondList.size)

        // check that the TER record is entered into the
        // atom list as a TER_RECORD type.
        val shouldBeTER = mol.atomNumberToAtomInfoHash[224]
        assertNotNull(shouldBeTER)
        assertEquals(IS_TER_RECORD, shouldBeTER!!.atomType)

    }
}