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

class ChainRenderingDescriptor {
    var backboneAtom: PdbAtom? = null
    var guideAtom: PdbAtom? = null
    lateinit var startAtom: PdbAtom
    lateinit var endAtom: PdbAtom
    // use the C3' atom to extend the spline just a bit
    lateinit var nucleicEndAtom: PdbAtom

    var curveIndex: Int = 0

    //var rendered_flag: Boolean = false
    //lateinit var vertex_cache: FloatArray

    var secondaryStructureType: Int = 0

    var endOfSecondaryStructure = false

    /*
     *
     */
    var nucleicType = NOT_A_TYPE

    lateinit var nucleicCornerAtom: PdbAtom
    lateinit var nucleicGuideAtom: PdbAtom
    lateinit var nucleicPlanarAtom: PdbAtom

    companion object {
        const val RIBBON = 0
        const val ALPHA_HELIX = 1
        const val BETA_SHEET = 2
        const val NUCLEIC = 3
        const val NOT_A_TYPE = -1
        const val PURINE = 1
        const val PYRIMIDINE = 2
    }
}
