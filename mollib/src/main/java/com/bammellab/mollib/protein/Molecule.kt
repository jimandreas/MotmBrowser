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
@file:Suppress("unused", "LocalVariableName", "PropertyName")
package com.bammellab.mollib.protein

import android.annotation.SuppressLint
import com.bammellab.mollib.common.math.Vector3
import com.bammellab.mollib.objects.BufferManager
import java.util.*

class Molecule {
    var name: String? = null
    var maxAtomNumber: Int = 0
    lateinit var bufMgr: BufferManager
    val numList: MutableList<Int> = ArrayList()
    @SuppressLint("UseSparseArrays")
    val atoms = HashMap<Int, PdbAtom>()
    val bondList: MutableList<Bond> = ArrayList()
    val helixList: MutableList<PdbHelix> = ArrayList()
    val pdbSheetList: MutableList<PdbBetaSheet> = ArrayList()
    val listofChainDescriptorLists: MutableList<List<*>> = ArrayList()
    private val listofDnaHelixChainLists: MutableList<List<*>> = ArrayList()

    /*
     * for display mode control
     */

    var displayHydrosFlag = false

    /*
     * for tracking memory and time
     */

    var reportedTimeFlag: Boolean = false
    var startOfParseTime: Float = 0.toFloat()
    var geometrySlices = INITIAL_SLICES
    var ribbonSlices = RIBBON_INITIAL_SLICES
    var sphereGeometrySlices = INITIAL_SLICES / 2
    var ribbonNodeCount = 0

    /*
     * list of ribbons to render
     *    pretty simple just yet
     */
    var listofRibbonLists: List<List<*>> = ArrayList()

    val p1p2 = Vector3()
    val P = Vector3()
    val R = Vector3()
    val S = Vector3()
    val p1 = FloatArray(3)
    val p2 = FloatArray(3)
    val p3 = FloatArray(3)
    val cache1: FloatArray
    val cache2: FloatArray
    var cache2_valid: Boolean = false

    var dcOffset: Float = 0.toFloat() // calculated in ParserPdbFile

    init {
        cache1 = FloatArray((RIBBON_INITIAL_SLICES + 1) * 3)
        cache2 = FloatArray((RIBBON_INITIAL_SLICES + 1) * 3)
        cache2_valid = false

    }

    fun clearLists() {
        maxAtomNumber = 0
        numList.clear()
        atoms.clear()
        bondList.clear()
        helixList.clear()
        pdbSheetList.clear()
        listofChainDescriptorLists.clear()
        listofDnaHelixChainLists.clear()
        displayHydrosFlag = false
        geometrySlices = INITIAL_SLICES
        ribbonSlices = RIBBON_INITIAL_SLICES
        sphereGeometrySlices = INITIAL_SLICES / 2
        ribbonNodeCount = 0
        cache2_valid = false
    }

    /*
     * calculate projected triangle allocations in bytes
     *   based on the "slices" or number of facets in the geometry
     */
    fun bondAllocation(numSlices: Int): Int {
        return bondList.size *
                2 * 6 * (numSlices + 1) * STRIDE_IN_BYTES
    }

    fun sphereAllocation(numSlices: Int): Int {
        return (atoms.size *
                numSlices * numSlices // number of vertices

                * 3 * 2 // two triangles worth generated per loop

                * STRIDE_IN_BYTES) // num floats per vertex
    }

    /*
     * note that there are 10 "cylinders" between nodes in the ribbons
     *    this is a wired-in number so far.
     *       This calculation overestimates the needed space by a little bit.
     */
    fun ribbonAllocation(numSlices: Int): Int {
        return ribbonNodeCount * 10 *
                6 * (numSlices + 1) * STRIDE_IN_BYTES
    }

    companion object {

        const val INITIAL_SLICES = 20
        const val RIBBON_INITIAL_SLICES = 30


        private const val POSITION_DATA_SIZE_IN_ELEMENTS = 3
        private const val NORMAL_DATA_SIZE_IN_ELEMENTS = 3
        private const val COLOR_DATA_SIZE_IN_ELEMENTS = 4

        private const val BYTES_PER_FLOAT = 4
        private const val BYTES_PER_SHORT = 2

        private const val STRIDE_IN_FLOATS = POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS + COLOR_DATA_SIZE_IN_ELEMENTS
        private const val STRIDE_IN_BYTES = STRIDE_IN_FLOATS * BYTES_PER_FLOAT


        /**
         * Handler messages
         * for more information on the IntDef in the Studio annotation library:
         *
         * https://noobcoderblog.wordpress.com/2015/04/12/java-enum-and-android-intdefstringdef-annotation/
         */
        const val UI_MESSAGE_GL_READY = 1
        const val UI_MESSAGE_FINISHED_PARSING = 2
        const val UI_MESSAGE_FINISHED_VIEW_CHANGE = 3
    }

}
