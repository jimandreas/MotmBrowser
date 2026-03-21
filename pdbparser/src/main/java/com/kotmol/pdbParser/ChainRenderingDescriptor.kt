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

@file:Suppress(
        "unused",
        "unused_variable",
        "unused_parameter",
        "unused_property",
        "deprecation",
        "ConstantConditionIf",
        "LocalVariableName",
        "PropertyName")
package com.kotmol.pdbParser

import com.kotmol.pdbParser.ChainRenderingDescriptor.SecondaryStructureType.RIBBON

/**
 * this records information used in rendering the CA ribbon for the
 * various chains - Alpha, Beta, and Nucleic
 */
class ChainRenderingDescriptor {
    var secondaryStructureType = RIBBON

    var backboneAtom: PdbAtom? = null
    var guideAtom: PdbAtom? = null
    var startAtom: PdbAtom? = null
    var endAtom: PdbAtom? = null
    // use the C3' atom to extend the spline just a bit
    lateinit var nucleicEndAtom: PdbAtom

    var curveIndex: Int = 0

    var endOfSecondaryStructure = false

    var nucleicType = NucleicType.NOT_A_NUCLEIC_TYPE

    var nucleicCornerAtom: PdbAtom? = null
    var nucleicGuideAtom: PdbAtom? = null
    var nucleicPlanarAtom: PdbAtom? = null

    enum class SecondaryStructureType {
        RIBBON,
        ALPHA_HELIX,
        BETA_SHEET,
        NUCLEIC,
        NOT_A_SECONDARY_STRUCTURE_TYPE
    }

    enum class NucleicType {
        PURINE,
        PYRIMIDINE,
        NOT_A_NUCLEIC_TYPE
    }

}
