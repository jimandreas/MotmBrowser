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

import com.bammellab.mollib.common.math.Vector3

/*
 * accumulated info for all atom types
 *    used for lookup in rendering the atom types
 */
class AtomInfo {

    var atomAtomicNumber: Int = 0
    var vdwRadius: Int = 0
    lateinit var atomChemicalSymbol: String
    lateinit var atomElementName: String
    lateinit var color: FloatArray
    /*
     * this class is leveraged by the select() method.
     *    just add a transformed position for sorting the selected atoms
     */
    val atomTransformedPosition = Vector3()
    var indexNumber: Int = 0
}

