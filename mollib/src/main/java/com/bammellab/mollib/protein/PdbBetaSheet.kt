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

class PdbBetaSheet {

    var strandNumber: Int = 0
    var sheetIdentification: String? = null
    var numStrandsInSheet: Int = 0
    var initialResidueName: String? = null
    var initialChainIdChar: Char = ' '
    var initialResidueNumber: Int = 0
    var initialInsertionCodeChar: Char = ' '
    var terminalResidueName: String? = null
    var terminalChainIdChar: Char = ' '
    var terminalResidueNumber: Int = 0
    var terminalInsertionCodeChar: Char = ' '
    var parallelSenseCode: Int = 0  // see SENSE_CODE

    var registrationCurrentAtomName: String? = null
    var registrationCurrentResidueName: String? = null
    var registrationCurrentChainChar: Char = ' '
    var registrationCurrentSeqNumber: Int = 0
    var registrationCurrentInsertionCodeChar: Char = ' '

    var registrationPreviousAtomName: String? = null
    var registrationPreviousResidueName: String? = null
    var registrationPreviousChainChar: Char = ' '
    var registrationPreviousSeqNumber: Int = 0
    var registrationPreviousInsertionCodeChar: Char = ' '

    companion object {
        const val SENSE_CODE_FIRST_STRAND = 0
        const val SENSE_CODE_PARALLEL = 1
        const val SENSE_CODE_ANIT_PARALLEL = -1
    }
}
