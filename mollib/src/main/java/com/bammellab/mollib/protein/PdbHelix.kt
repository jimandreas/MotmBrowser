/*
 * Copyright (C) 2016-2018 James Andreas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

@file:Suppress(
        "unused",
        "unused_variable",
        "unused_parameter",
        "unused_property",
        "deprecation",
        "ConstantConditionIf",
        "LocalVariableName",
        "PropertyName")
package com.bammellab.mollib.protein

class PdbHelix {

    var serialNumber: Int = 0
    var helixId: String? = null

    var initialResidueName: String? = null
    var initialChainIdChar: Char = ' '
    var initialResidueNumber: Int = 0
    var initialInsertionCode: Char = ' '

    var terminalResidueName: String? = null
    var terminalChainIdChar: Char = ' '
    var terminalResidueNumber: Int = 0
    var terminalInsertionCode: Char = ' '

    var helixClass: Int = 0
    var helixComment: String? = null
    var helixLength: Int = 0
}

/*
 * Helices are classified as follows:
CLASS NUMBER
TYPE OF HELIX (COLUMNS 39 - 40)
--------------------------------------------------------------
Right-handed alpha (default) 1
Right-handed omega 2
Right-handed pi 3
Right-handed gamma 4
Right-handed 310 5
Left-handed alpha 6
Left-handed omega 7
Left-handed gamma 8
27 ribbon/helix 9
Polyproline 10
Relationships to

Record Format
COLUMNS DATA TYPE FIELD DEFINITION
-----------------------------------------------------------------------------------
1 - 6 Record name "HELIX "
8 - 10 Integer serNum Serial number of the helix. This starts
at 1 and increases incrementally.
12 - 14 LString(3) helixID Helix identifier. In addition to a serial
number, each helix is given an
alphanumeric character helix identifier.
16 - 18 Residue name initResName Name of the initial residue.
20 Character initChainID Chain identifier for the chain containing
this helix.
22 - 25 Integer initSeqNum Sequence number of the initial residue.
26 AChar initICode Insertion code of the initial residue.
28 - 30 Residue name endResName Name of the terminal residue of the helix.
32 Character endChainID Chain identifier for the chain containing
this helix.
34 - 37 Integer endSeqNum Sequence number of the terminal residue.
38 AChar endICode Insertion code of the terminal residue.
39 - 40 Integer helixClass Helix class (see below).
41 - 70 String comment Comment about this helix.
72 - 76 Integer length Length of this helix.
 */