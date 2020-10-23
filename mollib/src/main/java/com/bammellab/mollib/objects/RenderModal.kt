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
        "deprecation",
        "ConstantConditionIf",
        "LocalVariableName", "SameParameterValue")

package com.bammellab.mollib.objects

import android.annotation.SuppressLint
import com.bammellab.mollib.common.math.CatmullRomCurve
import com.bammellab.mollib.common.math.MathUtil
import com.bammellab.mollib.common.math.MotmVector3
import com.bammellab.mollib.objects.GlobalObject.BRIGHTNESS_FACTOR
import com.bammellab.mollib.objects.SegmentAtomToAtomBond.Companion.cache2_valid
import com.kotmol.pdbParser.ChainRenderingDescriptor
import com.kotmol.pdbParser.ChainRenderingDescriptor.SecondaryStructureType
import com.kotmol.pdbParser.Molecule
import com.kotmol.pdbParser.PdbAtom
import timber.log.Timber
import kotlin.math.acos

/*
 * render the polypeptide backbone "ribbon" - follows the Alpha Carbons (CA)
 */
class RenderModal(private val molecule: Molecule) {


    private val cylinderIndexCount: Int = 0
    private lateinit var vertexData: FloatArray
    private var mOffset: Int = 0
    private val vboTopAndBottom = IntArray(1)
    private val vboBody = IntArray(1)
    private val ibo = IntArray(1)

    private val cache1: FloatArray
    private val cache2: FloatArray

    private val debugWhiteStripe = false

    init {
        cache1 = FloatArray((RIBBON_INITIAL_SLICES + 1) * 3)
        cache2 = FloatArray((RIBBON_INITIAL_SLICES + 1) * 3)
        cache2_valid = false
    }

    fun renderModal() {
        var curvePoints: CatmullRomCurve
        var guideAtomPoints: CatmullRomCurve
        var chainDescriptorList: List<*>
        var chainEntry: ChainRenderingDescriptor
        var mainChainAtom: PdbAtom?
        var guideAtom: PdbAtom?
        var vnew: MotmVector3
        var j: Int

        for (i in 0 until molecule.listofChainDescriptorLists.size) {
            chainDescriptorList = molecule.listofChainDescriptorLists[i]
            if (chainDescriptorList.size < 2) {
                continue
            }
            /*
             * enter all backbone atom (CA) positions into the spline curve_points
             */
            curvePoints = CatmullRomCurve()
            guideAtomPoints = CatmullRomCurve()
            j = 0
            while (j < chainDescriptorList.size) {
                chainEntry = chainDescriptorList[j] as ChainRenderingDescriptor
                /*
                 * initial condition- start with CA atom and add the 1st carbon to
                 *   set up the curve.   For the guide curve (used in HELIX and SHEET)
                 *   start with the CA atom and then switch to the oxygen (guide) atom.
                 */
                if (j == 0) {
                    mainChainAtom = chainEntry.backboneAtom
                    if (mainChainAtom == null) {
                        Timber.e("renderModal: backbone atom is null in list, i = $i")
                        return
                    }

                    // initial guide spline atom is the mainchain atom
                    vnew = MotmVector3(mainChainAtom.atomPosition)
                    guideAtomPoints.addPoint(vnew)

                    //  initial curve atom is the startAtom position
                    mainChainAtom = chainEntry.startAtom
                    // start atom will be null for CA-only ribbons
                    if (mainChainAtom == null) {
                        mainChainAtom = chainEntry.guideAtom
                    }
                    vnew = MotmVector3(mainChainAtom!!.atomPosition)
                    curvePoints.addPoint(vnew)

                    guideAtom = chainEntry.guideAtom
                    if (guideAtom != null) {
                        vnew = MotmVector3(guideAtom.atomPosition)
                        guideAtomPoints.addPoint(vnew)
                    }

                    chainEntry.curveIndex = 0
                }

                /*
                 * loop body : hook up all the CA atoms and Oxygen (O) atoms in the separate curves
                 */
                mainChainAtom = chainEntry.backboneAtom
                if (mainChainAtom != null) {
                    vnew = MotmVector3(mainChainAtom.atomPosition)
                    curvePoints.addPoint(vnew)
                }

                guideAtom = chainEntry.guideAtom
                if (guideAtom != null) {
                    vnew = MotmVector3(guideAtom.atomPosition)
                    guideAtomPoints.addPoint(vnew)
                }

                chainEntry.curveIndex = j * CSF

                /*
                 * ending condition - if this is nucleic - then extend the spline using the C3' atom
                 *   so that the spline can match the end of the helix
                 */
                if (j == chainDescriptorList.size - 1) {
                    if (chainEntry.secondaryStructureType == SecondaryStructureType.NUCLEIC) {
                        mainChainAtom = chainEntry.nucleicEndAtom
                        vnew = MotmVector3(mainChainAtom.atomPosition)
                        curvePoints.addPoint(vnew)

                        guideAtom = chainEntry.guideAtom
                        vnew = MotmVector3(guideAtom!!.atomPosition)
                        guideAtomPoints.addPoint(vnew)

                        chainEntry.curveIndex = j * CSF

                    }
                }
                j++
            }
            /*
             * at the end of the list - tack on the end atom for the curve -
             *    the curve needs an extra point at start and end
             */
            if (j-- == 0) { // if null list then keep looping
                continue
            }
            chainEntry = chainDescriptorList[j] as ChainRenderingDescriptor
            // add the end atom as last in the curve
            mainChainAtom = chainEntry.endAtom
            // end atom will be null for CA-only ribbons
            if (mainChainAtom != null) {
                vnew = MotmVector3(mainChainAtom.atomPosition)
                curvePoints.addPoint(vnew)
            }

            /*
             * now figure out how to render the list
             */
            scanCurve(chainDescriptorList, curvePoints, guideAtomPoints)
        }

        /*
         * done rendering, commit the triangles
         */
        BufferManager.transferToGl()
    }

    /*
     * walk the list of chain entries.
     *    They consist of a series of RIBBONs, HELIXs, and BETA_SHEETs.
     * Direct the rendering to the appropriate method for the series.
     *
     */
    @SuppressLint("UnusedParameter")
    private fun scanCurve(chain_list: List<*>,
                          curve: CatmullRomCurve,
                          guide_curve: CatmullRomCurve) {

        var chainEntry = chain_list[0] as ChainRenderingDescriptor
        var currentType = chainEntry.secondaryStructureType
        var startIndex = 0
        var j = 1
        val descriptorCount = chain_list.size
        cache2_valid = false
        while (j < descriptorCount) {
            chainEntry = chain_list[j] as ChainRenderingDescriptor

            /*
             * if this is a new secondary type, then render the previous
             *  secondary, and start the index for the new secondary type
             *  at the terminating atom of the previous secondary.
             */

            if (currentType != chainEntry.secondaryStructureType) {
                when (currentType) {
                    SecondaryStructureType.RIBBON -> renderRibbon(curve, startIndex, j, descriptorCount)
                    SecondaryStructureType.ALPHA_HELIX, SecondaryStructureType.NUCLEIC ->
                        // renderRibbon(guide_curve, start_index, j, descriptor_count);
                        // renderRibbon(curve, start_index, j, descriptor_count);
                        renderHelix(curve, startIndex, j, descriptorCount, chain_list,
                                SecondaryStructureType.ALPHA_HELIX)
                    SecondaryStructureType.BETA_SHEET ->
                        // renderRibbon(guide_curve, start_index, j, descriptor_count);
                        // renderRibbon(curve, start_index, j, descriptor_count);
                        renderHelix(curve, startIndex, j, descriptorCount, chain_list,
                                SecondaryStructureType.BETA_SHEET)
                    else -> {
                    }
                }
                startIndex = j
                currentType = chainEntry.secondaryStructureType
            }
            j++
        }
        /*
         * render the last secondary at the end of the list
         */
        when (currentType) {
            SecondaryStructureType.RIBBON -> renderRibbon(curve, startIndex, j - 1, descriptorCount)

            SecondaryStructureType.ALPHA_HELIX ->
                // renderRibbon(guide_curve, start_index, j, descriptor_count);
                // renderRibbon(curve, start_index, j, descriptor_count);
                renderHelix(curve, startIndex, j - 1, descriptorCount, chain_list,
                        SecondaryStructureType.ALPHA_HELIX)

            SecondaryStructureType.BETA_SHEET ->
                // renderRibbon(guide_curve, start_index, j, descriptor_count);
                // renderRibbon(curve, start_index, j, descriptor_count);
                renderHelix(curve, startIndex, j - 1, descriptorCount, chain_list,
                        SecondaryStructureType.BETA_SHEET)

            SecondaryStructureType.NUCLEIC ->
                // renderRibbon(guide_curve, start_index, j, descriptor_count);
                // renderRibbon(curve, start_index, j, descriptor_count);
                renderNucleic(curve, startIndex, j, descriptorCount, chain_list)
            else -> {
            }
        }
    }

    /**
     *
     * Internally:
     *
     * backbone C5prime, start = O5prime, end = O3prime, guide = C1prime
     */

    private fun renderNucleic(path: CatmullRomCurve,
                              start_index: Int, end_index: Int, descriptor_count: Int,
                              chain_list: List<*>) {

        val positionPrevious = MotmVector3()
        val positionStart = MotmVector3()
        val positionEnd = MotmVector3()
        val positionBeyond = MotmVector3()

        val P1 = MotmVector3()
        val P2 = MotmVector3()
        val R1 = MotmVector3()
        val R2 = MotmVector3()
        val S1 = MotmVector3()
        val S2 = MotmVector3()

        val delta = MotmVector3()
        val temp = MotmVector3()

        val prev1 = MotmVector3()
        val prev2 = MotmVector3()
        val prev3 = MotmVector3()


        // float radius = 0.25f;
        val radius = 1.0f // helix ribbon is 4X and then scaled down for ellipse profile

        val p1 = FloatArray(3)
        val p2 = FloatArray(3)
        val p3 = FloatArray(3)
        var n: FloatArray
        var x1: Double
        var y1: Double
        var z1: Double

        //        float[] v1 = new float[(numSlices + 1) * 3];
        //        float[] v2 = new float[(numSlices + 1) * 3];

        val numSlices = ribbonSlices
        val v1 = cache1
        val v2 = cache2

        // subtract two points for the two unused end points
        val scaling = (path.numPoints - 2) * 10
        var start = start_index * scaling / descriptor_count
        val end = end_index * scaling / descriptor_count
        var endAtom: PdbAtom?
        var guideAtom: PdbAtom?
        var nextEndAtom: PdbAtom?
        var nextGuideAtom: PdbAtom?

        /*
         * LOOP: iterate over the curve
         *    challenge is to get the alignment to the guideAtom atom right and then
         *    interpolate it
         */
        // set up offset so we have a "previous" point
        if (start == 0) start = 1
        for (where_in_spline in start until end) {

            // TODO: add error handling here

            /*
             * map the curve position to the backbone chain entries
             *    add 2 to descriptor count to factor in the extra atom at the start and end
             */
            var chainIndex = where_in_spline * descriptor_count / scaling
            if (chainIndex == chain_list.size) {
                chainIndex = chain_list.size - 1
            }
            val currentChainEntry = chain_list[chainIndex] as ChainRenderingDescriptor
            endAtom = currentChainEntry.endAtom
            guideAtom = currentChainEntry.guideAtom

            if (endAtom == null) {
                Timber.e("renderNucleic: endAtom is null!! continuing")
                continue
            }
            if (guideAtom == null) {
                Timber.e("renderNucleic: guideAtom is null!! continuing")
                continue
            }

            if (chainIndex + 1 < chain_list.size) {
                val nextChainEntry = chain_list[chainIndex + 1] as ChainRenderingDescriptor

                nextEndAtom = nextChainEntry.endAtom
                nextGuideAtom = nextChainEntry.guideAtom
            } else {
                if (debug) Timber.i("AT END **********************")
                nextEndAtom = endAtom
                nextGuideAtom = guideAtom
            }

            if (nextEndAtom == null) {
                Timber.e("renderNucleic: nextEndAtom is null continuing")
                continue
            }
            if (nextGuideAtom == null) {
                Timber.e("renderNucleic: nextGuideAtom is null continuing")
                continue
            }

            //            Timber.i("endAtom " + endAtom.atom_number
            //                    + " curve = " + current_chain_entry.curveIndex
            //                    + ", " + where_in_spline
            //                    + " oxy-atom " + current_chain_entry.guideAtom.atom_number
            //                    + " index = " + chain_index);

            R1.setAll(MotmVector3(guideAtom.atomPosition))
            R1.subtract(MotmVector3(endAtom.atomPosition))

            delta.setAll(MotmVector3(nextGuideAtom.atomPosition))
            delta.subtract(MotmVector3(nextEndAtom.atomPosition))

            var weight = (where_in_spline - currentChainEntry.curveIndex).toDouble()
            weight /= CSF

            // guide_curve.calculatePoint(upper_curve, (where_in_spline+the_min_index) / (float) scaling);

            path.calculatePoint(positionPrevious, ((where_in_spline - 1) / scaling.toFloat()).toDouble())
            path.calculatePoint(positionStart, (where_in_spline / scaling.toFloat()).toDouble())
            path.calculatePoint(positionEnd, ((where_in_spline + 1) / scaling.toFloat()).toDouble())
            path.calculatePoint(positionBeyond, ((where_in_spline + 2) / scaling.toFloat()).toDouble())

            P1.setAll(positionStart)
            P1.subtract(positionPrevious)


            S1.setAll(R1)
            S1.cross(P1)

            P2.setAll(positionEnd)
            P2.subtract(positionStart)

            R2.setAll(MotmVector3(guideAtom.atomPosition))
            R2.subtract(MotmVector3(endAtom.atomPosition))

            if (R2.dot(prev1) < 0) {
                R2.multiply(-1.0)
            }
            temp.setAll(R2)
            temp.subtract(delta)
            temp.multiply(weight)
            // R2.add(delta);

            /*
             * smooth the position as a running average with the 3 previous positions
             */
            if (where_in_spline == start) {
                prev1.setAll(R2)
                prev2.setAll(R2)
                prev3.setAll(R2)
            } else {
                temp.setAll(prev1)
                temp.add(prev2)
                temp.add(prev3)
                temp.add(R2)
                temp.divide(4.0)
                R2.setAll(temp)
                prev3.setAll(prev2)
                prev2.setAll(prev1)
                prev1.setAll(R2)
            }

            S2.setAll(R2)
            S2.cross(P2)

            S1.normalize()
            S2.normalize()
            R1.normalize()
            R2.normalize()

            R1.divide(5.0)
            R2.divide(5.0)

            if (where_in_spline == start) {
                // calculate and cache
                for (i in 0..numSlices) {

                    val angleInRadians1 = i.toDouble() / numSlices.toDouble() * (Math.PI * 2f)

                    val s1 = radius * MathUtil.sin(angleInRadians1)
                    val c1 = radius * MathUtil.cos(angleInRadians1)
                    x1 = R1.x * c1 + S1.x * s1
                    y1 = R1.y * c1 + S1.y * s1
                    z1 = R1.z * c1 + S1.z * s1

                    v1[i * 3] = (x1 + positionStart.x).toFloat()
                    v1[i * 3 + 1] = (y1 + positionStart.y).toFloat()
                    v1[i * 3 + 2] = (z1 + positionStart.z).toFloat()
                }
            }


            // calculate and cache
            for (i in 0..numSlices) {

                val angleInRadians1 = i.toDouble() / numSlices.toDouble() * (Math.PI * 2f)

                val s1 = radius * MathUtil.sin(angleInRadians1)

                val c1 = radius * MathUtil.cos(angleInRadians1)


                x1 = R2.x * c1 + S2.x * s1
                y1 = R2.y * c1 + S2.y * s1
                z1 = R2.z * c1 + S2.z * s1

                v2[i * 3] = (x1 + positionEnd.x).toFloat()
                v2[i * 3 + 1] = (y1 + positionEnd.y).toFloat()
                v2[i * 3 + 2] = (z1 + positionEnd.z).toFloat()
            }


            vertexData = BufferManager.getFloatArray(6 * (numSlices + 1) * STRIDE_IN_FLOATS)
            mOffset = BufferManager.floatArrayIndex

            run {

                val whiteColor = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)
                val greenColor = floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)


                /*
                 * first generate the points, then map them to the position
                 * (1) version 1
                 * (2) version 2 - should transform the plane
                 * (3) version 3 - based on a spline calculation
                 */
                for (i in 0 until numSlices) {

                    p1[0] = v1[i * 3]
                    p1[1] = v1[i * 3 + 1]
                    p1[2] = v1[i * 3 + 2]

                    // first bottom point

                    p3[0] = v2[i * 3]
                    p3[1] = v2[i * 3 + 1]
                    p3[2] = v2[i * 3 + 2]

                    // SECOND BOTTOM point

                    p2[0] = v2[(i + 1) * 3]
                    p2[1] = v2[(i + 1) * 3 + 1]
                    p2[2] = v2[(i + 1) * 3 + 2]

                    // OK that is one triangle.

                    // n = XYZ.getNormal(p1, p2, p3);
                    n = XYZ.getNormal(p3, p2, p1)
                    if (i == 0 && debugWhiteStripe) {
                        putTri(p1, p2, p3, n, whiteColor)
                        //                    } else if (flag_color) {
                        //                        putTri(p1, p2, p3, n, white_color);
                    } else {
                        putTri(p1, p2, p3, n, greenColor)
                    }


                    // SECOND triangle NOW
                    run {
                        // first top point

                        p1[0] = v1[i * 3]
                        p1[1] = v1[i * 3 + 1]
                        p1[2] = v1[i * 3 + 2]

                    }
                    run {
                        // SECOND BOTTOM point

                        p3[0] = v2[(i + 1) * 3]
                        p3[1] = v2[(i + 1) * 3 + 1]
                        p3[2] = v2[(i + 1) * 3 + 2]
                    }
                    run {
                        // SECOND top point

                        p2[0] = v1[(i + 1) * 3]
                        p2[1] = v1[(i + 1) * 3 + 1]
                        p2[2] = v1[(i + 1) * 3 + 2]

                    }

                    // n = XYZ.getNormal(p1, p2, p3);
                    n = XYZ.getNormal(p3, p2, p1)
                    // putTri(p1, p2, p3, n, color);
                    if (i == 0 && debugWhiteStripe) {
                        putTri(p1, p2, p3, n, whiteColor)
                        //                    } else if (flag_color) {
                        //                        putTri(p1, p2, p3, n, white_color);
                    } else {
                        putTri(p1, p2, p3, n, greenColor)
                    }
                }
                // reuse the 2nd array
                System.arraycopy(v2, 0, v1, 0, (numSlices + 1) * 3)

                BufferManager.floatArrayIndex = mOffset
            }
        }
    }


    private fun renderHelix(path: CatmullRomCurve,
                            start_index: Int, end_index: Int, descriptor_count: Int,
                            chain_list: List<*>,
                            descriptor: SecondaryStructureType) {

        val positionPrevious = MotmVector3()
        val positionStart = MotmVector3()
        val positionEnd = MotmVector3()
        val positionBeyond = MotmVector3()

        //val p1p2 = MotmVector3()
        val P1 = MotmVector3()
        val P2 = MotmVector3()
        val R1 = MotmVector3()
        val R2 = MotmVector3()
        val S1 = MotmVector3()
        val S2 = MotmVector3()

        val delta = MotmVector3()
        val temp = MotmVector3()

        val prev1 = MotmVector3()
        val prev2 = MotmVector3()
        val prev3 = MotmVector3()

        //val flag_color: Boolean
        var drawArrow = false
        var arrowCountdown = 11


        // float radius = 0.25f;
        var radius = 1.0f // helix ribbon is 4X and then scaled down for ellipse profile

        val p1 = FloatArray(3)
        val p2 = FloatArray(3)
        val p3 = FloatArray(3)
        var n: FloatArray
        var x1: Double
        var y1: Double
        var z1: Double

        //        float[] v1 = new float[(numSlices + 1) * 3];
        //        float[] v2 = new float[(numSlices + 1) * 3];
        val numSlices = ribbonSlices
        val v1 = cache1
        val v2 = cache2

        // private void renderRibbon( CatmullRomCurve curve, int start_index, int end_index ) {

        val scaling = path.numPoints * 10 // HACK: wired for now
        val start = start_index * scaling / descriptor_count
        val end = end_index * scaling / descriptor_count
        var carbon: PdbAtom?
        var oxygen: PdbAtom?
        var nextCarbon: PdbAtom?
        var nextOxygen: PdbAtom?

        /*
         * see if we can calculate the best offset at this point
         * status : algorithm abandoned (but was interesting to implement!
         */
        //        MotmVector3 upper_curve = new MotmVector3();
        //        MotmVector3 best_point = new MotmVector3();

        //        double min_distance = 999;
        //        int the_min_index = 0;
        //        double target_in_curve = (start+50) / (float)scaling;
        //        path.calculatePoint(position_start, target_in_curve);
        //        for (int attempt = -20; attempt < 20; attempt++) {
        //            guide_curve.calculatePoint(upper_curve, (start+50+attempt) / (float)scaling);
        //            double distance = position_start.distanceTo(upper_curve);
        //            Timber.i("attempt = " + attempt+ " distance  = " + distance);
        //            if (distance < min_distance) {
        //                min_distance = distance;
        //                the_min_index = attempt;
        //                target_in_curve = (start+50+attempt);
        //                best_point.setAll(upper_curve);
        //
        //            }
        //        }
        //        Timber.i("best index = " + the_min_index + " and distance = " + min_distance);

        /*
         * LOOP: iterate over the curve
         *    challenge is to get the alignment to the oxygen atom right and then
         *    interpolate it
         */
        var whereInSpline = start
        while (whereInSpline < end) {

            // TODO: add error handling here

            // int start = start_index * scaling / descriptor_count;

            /*
             * map the curve position to the backbone chain entries
             *    add 2 to descriptor count to factor in the extra atom at the start and end
             */
            val chainIndex = (whereInSpline + 1) * descriptor_count / scaling
            val currentChainEntry = chain_list[chainIndex] as ChainRenderingDescriptor
            carbon = currentChainEntry.endAtom
            oxygen = currentChainEntry.guideAtom

            if (carbon == null) {
                Timber.e("RenderHelix: carbon is null continuing")
                continue
            }
            if (oxygen == null) {
                Timber.e("RenderHelix: oxygen is null continuing")
                continue
            }

            if (debug)
                Timber.i("end carbon %d oxy %d", carbon.atomNumber, oxygen!!.atomNumber)

            if (chainIndex + 1 < chain_list.size) {
                val nextChainEntry = chain_list[chainIndex + 1] as ChainRenderingDescriptor
                if (nextChainEntry.secondaryStructureType != descriptor) {
                    if (debug)
                        Timber.i("next is end of HELIX ****** index = $chainIndex plus one")
                    nextCarbon = carbon
                    nextOxygen = oxygen
                    if (descriptor == SecondaryStructureType.BETA_SHEET) {
                        drawArrow = true
                    }
                } else {
                    nextCarbon = nextChainEntry.endAtom
                    nextOxygen = nextChainEntry.guideAtom
                    /*if (nextCarbon == null) {
                        Timber.e("RenderHelix - null next_carbon : chain_index %d of size %d",
                                chainIndex, chain_list.size)
                    }
                    if (nextOxygen == null) {
                        Timber.e("RenderHelix - null next_oxygen : chain_index %d of size %d",
                                chainIndex, chain_list.size)
                    }*/
                }

                if (nextCarbon == null) {
                    Timber.e("RenderHelix: nextCarbon is null continuing")
                    continue
                }
                if (nextOxygen == null) {
                    Timber.e("RenderHelix: nextOxygen is null continuing")
                    continue
                }

            } else {
                if (debug) Timber.i("AT END **********************")
                nextCarbon = carbon
                nextOxygen = oxygen
                if (descriptor == SecondaryStructureType.BETA_SHEET) {
                    drawArrow = true
                }
            }

            //            Timber.i("carbon " + carbon.atom_number
            //                    + " curve = " + current_chain_entry.curveIndex
            //                    + ", " + where_in_spline
            //                    + " oxy-atom " + current_chain_entry.guideAtom.atom_number
            //                    + " index = " + chain_index);

            R1.setAll(MotmVector3(oxygen.atomPosition))
            R1.subtract(MotmVector3(carbon.atomPosition))

            delta.setAll(MotmVector3(nextOxygen.atomPosition))
            delta.subtract(MotmVector3(nextCarbon.atomPosition))

            var weight = (whereInSpline - currentChainEntry.curveIndex).toDouble()
            weight /= CSF

            // guide_curve.calculatePoint(upper_curve, (where_in_spline+the_min_index) / (float) scaling);

            path.calculatePoint(positionPrevious, ((whereInSpline - 1) / scaling.toFloat()).toDouble())
            path.calculatePoint(positionStart, (whereInSpline / scaling.toFloat()).toDouble())
            path.calculatePoint(positionEnd, ((whereInSpline + 1) / scaling.toFloat()).toDouble())
            path.calculatePoint(positionBeyond, ((whereInSpline + 2) / scaling.toFloat()).toDouble())
            //                p1p2.setAll(position_end);
            //                p1p2.subtract(position_previous);
            P1.setAll(positionStart)
            P1.subtract(positionPrevious)
            /*
             * attempt to calculate the best fit
             */
            //                R1.setAll(position_start);
            //                R1.subtract(upper_curve);

            S1.setAll(R1)
            S1.cross(P1)

            //                p1p2.setAll(position_beyond);
            //                p1p2.subtract(position_start);
            P2.setAll(positionEnd)
            P2.subtract(positionStart)

            R2.setAll(MotmVector3(oxygen.atomPosition))
            R2.subtract(MotmVector3(carbon.atomPosition))

            if (R2.dot(prev1) < 0) {
                R2.multiply(-1.0)
            }
            temp.setAll(R2)
            temp.subtract(delta)
            temp.multiply(weight)
            // R2.add(delta);

            if (whereInSpline == start) {
                prev1.setAll(R2)
                prev2.setAll(R2)
                prev3.setAll(R2)
            } else {
                temp.setAll(prev1)
                temp.add(prev2)
                temp.add(prev3)
                temp.add(R2)
                temp.divide(4.0)
                R2.setAll(temp)
                prev3.setAll(prev2)
                prev2.setAll(prev1)
                prev1.setAll(R2)
            }

            S2.setAll(R2)
            S2.cross(P2)

            S1.normalize()
            S2.normalize()
            R1.normalize()
            R2.normalize()

            if (drawArrow) {
                /*
                 * 1st element of arrow is just an extension to the base (countdown=11)
                 * 2nd element is the pop-out and then the pop-out is scaled down
                 *    to coincide with the ribbon
                 */
                if (arrowCountdown == 11) {
                    whereInSpline-- // repeat this location
                    arrowCountdown--
                    S1.divide(5.0)
                    S2.divide(5.0)
                } else {
                    radius = 1.5f * arrowCountdown.toFloat() / 10.0f + 0.1f
                    if (arrowCountdown > 0) arrowCountdown--
                    if (arrowCountdown < 3) {
                        S2.divide((2 * arrowCountdown + 1).toDouble())
                    } else {
                        S2.divide(5.0)
                    }
                }
            } else {
                S1.divide(5.0)
                S2.divide(5.0)
            }

            if (whereInSpline == start) {
                // calculate and cache
                for (i in 0..numSlices) {

                    val angleInRadians1 = i.toDouble() / numSlices.toDouble() * (Math.PI * 2f)

                    val s1 = radius * MathUtil.sin(angleInRadians1)
                    val c1 = radius * MathUtil.cos(angleInRadians1)
                    x1 = R1.x * c1 + S1.x * s1
                    y1 = R1.y * c1 + S1.y * s1
                    z1 = R1.z * c1 + S1.z * s1

                    v1[i * 3] = (x1 + positionStart.x).toFloat()
                    v1[i * 3 + 1] = (y1 + positionStart.y).toFloat()
                    v1[i * 3 + 2] = (z1 + positionStart.z).toFloat()
                }
                // render a "connection tube" to bridge between possible
                // previous section of cartoon
                connectionTube()
            }


            // calculate and cache
            for (i in 0..numSlices) {

                val angleInRadians1 = i.toDouble() / numSlices.toDouble() * (Math.PI * 2f)

                val s1 = radius * MathUtil.sin(angleInRadians1)

                val c1 = radius * MathUtil.cos(angleInRadians1)


                x1 = R2.x * c1 + S2.x * s1
                y1 = R2.y * c1 + S2.y * s1
                z1 = R2.z * c1 + S2.z * s1

                v2[i * 3] = (x1 + positionEnd.x).toFloat()
                v2[i * 3 + 1] = (y1 + positionEnd.y).toFloat()
                v2[i * 3 + 2] = (z1 + positionEnd.z).toFloat()
            }


            vertexData = BufferManager.getFloatArray(6 * (numSlices + 1) * STRIDE_IN_FLOATS)
            mOffset = BufferManager.floatArrayIndex

            run {

                val whiteColor = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)
                val greenColor = floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)


                /*
                 * first generate the points, then map them to the position
                 * (1) version 1
                 * (2) version 2 - should transform the plane
                 * (3) version 3 - based on a spline calculation
                 */
                for (i in 0 until numSlices) {

                    p1[0] = v1[i * 3]
                    p1[1] = v1[i * 3 + 1]
                    p1[2] = v1[i * 3 + 2]

                    // first bottom point

                    p3[0] = v2[i * 3]
                    p3[1] = v2[i * 3 + 1]
                    p3[2] = v2[i * 3 + 2]

                    // SECOND BOTTOM point

                    p2[0] = v2[(i + 1) * 3]
                    p2[1] = v2[(i + 1) * 3 + 1]
                    p2[2] = v2[(i + 1) * 3 + 2]

                    // OK that is one triangle.

                    // n = XYZ.getNormal(p1, p2, p3);
                    n = XYZ.getNormal(p3, p2, p1)
                    if (i == 0 && debugWhiteStripe) {
                        putTri(p1, p2, p3, n, whiteColor)
                        //                    } else if (flag_color) {
                        //                        putTri(p1, p2, p3, n, white_color);
                    } else {
                        putTri(p1, p2, p3, n, greenColor)
                    }


                    // SECOND triangle NOW
                    run {
                        // first top point

                        p1[0] = v1[i * 3]
                        p1[1] = v1[i * 3 + 1]
                        p1[2] = v1[i * 3 + 2]

                    }
                    run {
                        // SECOND BOTTOM point

                        p3[0] = v2[(i + 1) * 3]
                        p3[1] = v2[(i + 1) * 3 + 1]
                        p3[2] = v2[(i + 1) * 3 + 2]
                    }
                    run {
                        // SECOND top point

                        p2[0] = v1[(i + 1) * 3]
                        p2[1] = v1[(i + 1) * 3 + 1]
                        p2[2] = v1[(i + 1) * 3 + 2]

                    }

                    // n = XYZ.getNormal(p1, p2, p3);
                    n = XYZ.getNormal(p3, p2, p1)
                    // putTri(p1, p2, p3, n, color);
                    if (i == 0 && debugWhiteStripe) {
                        putTri(p1, p2, p3, n, whiteColor)
                        //                    } else if (flag_color) {
                        //                        putTri(p1, p2, p3, n, white_color);
                    } else {
                        putTri(p1, p2, p3, n, greenColor)
                    }
                }
                // reuse the 2nd array
                System.arraycopy(v2, 0, v1, 0, (numSlices + 1) * 3)
                cache2_valid = true

                BufferManager.floatArrayIndex = mOffset
            }
            whereInSpline++
        }
        //        Timber.i("end of helix");
    }

    /**
     * special method to "simply" make one tube of triangles -
     * that span between the cached "v2" coordinates and the new "v1"
     * coordinates at the start of the next cartoon entity.
     */
    private fun connectionTube() {

        if (!cache2_valid) {
            return
        }
        val p1 = FloatArray(3)
        val p2 = FloatArray(3)
        val p3 = FloatArray(3)
        var n: FloatArray
        val numSlices = ribbonSlices
        val v1 = cache1
        val v2 = cache2
        val whiteColor = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)
        val greenColor = floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)

        vertexData = BufferManager.getFloatArray(6 * (numSlices + 1) * STRIDE_IN_FLOATS)
        mOffset = BufferManager.floatArrayIndex
        for (i in 0 until numSlices) {

            p1[0] = v1[i * 3]
            p1[1] = v1[i * 3 + 1]
            p1[2] = v1[i * 3 + 2]

            // first bottom point

            p2[0] = v2[i * 3]
            p2[1] = v2[i * 3 + 1]
            p2[2] = v2[i * 3 + 2]

            // SECOND BOTTOM point

            p3[0] = v2[(i + 1) * 3]
            p3[1] = v2[(i + 1) * 3 + 1]
            p3[2] = v2[(i + 1) * 3 + 2]

            // OK that is one triangle.

            // n = XYZ.getNormal(p1, p2, p3);
            n = XYZ.getNormal(p3, p2, p1)
            if (i == 0 && debugWhiteStripe) {
                putTri(p1, p2, p3, n, whiteColor)
                //                    } else if (flag_color) {
                //                        putTri(p1, p2, p3, n, white_color);
            } else {
                putTri(p1, p2, p3, n, whiteColor)
            }


            // SECOND triangle NOW
            run {
                // first top point

                p1[0] = v1[i * 3]
                p1[1] = v1[i * 3 + 1]
                p1[2] = v1[i * 3 + 2]

            }
            run {
                // SECOND BOTTOM point

                p2[0] = v2[(i + 1) * 3]
                p2[1] = v2[(i + 1) * 3 + 1]
                p2[2] = v2[(i + 1) * 3 + 2]
            }
            run {
                // SECOND top point

                p3[0] = v1[(i + 1) * 3]
                p3[1] = v1[(i + 1) * 3 + 1]
                p3[2] = v1[(i + 1) * 3 + 2]

            }

            // n = XYZ.getNormal(p1, p2, p3);
            n = XYZ.getNormal(p3, p2, p1)
            // putTri(p1, p2, p3, n, color);
            if (i == 0 && debugWhiteStripe) {
                putTri(p1, p2, p3, n, whiteColor)
                //                    } else if (flag_color) {
                //                        putTri(p1, p2, p3, n, white_color);
            } else {
                putTri(p1, p2, p3, n, greenColor)
            }
        }
        BufferManager.floatArrayIndex = mOffset
    }

    private fun renderRibbon(path: CatmullRomCurve, start_index: Int, end_index: Int, descriptor_count: Int) {


        val redColor = floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f)
        val whiteColor = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)
        var color: FloatArray

        val positionStart = MotmVector3()
        val positionEnd = MotmVector3()
        val positionBeyond = MotmVector3()


        val radius = 0.25f

        val p1 = FloatArray(3)
        val p2 = FloatArray(3)
        val p3 = FloatArray(3)
        var n: FloatArray
        var x1: Double
        var y1: Double
        var z1: Double
        var v1: FloatArray

        //        double x2, y2, z2;
        //        double olddot = 1.0;
        //        int state = 0;


        val p1p2 = MotmVector3()
        val P = MotmVector3()
        val R = MotmVector3()
        val S = MotmVector3()
        // MotmVector3 randomPoint = new MotmVector3(Math.random(), Math.random(), Math.random());
        val randomPoint = MotmVector3(0.0, 999.0, 0.0)
        val saveR = MotmVector3()
        val oldR = MotmVector3()

        //        int numSlices = 30;
        //        float[] start_vertex_cache = new float[(numSlices + 1) * 3];
        //        float[] end_vertex_cache = new float[(numSlices + 1) * 3];

        val numSlices = ribbonSlices
        val startVertexCache = cache1
        v1 = startVertexCache
        val endVertexCache = cache2

        // private void renderRibbon( CatmullRomCurve curve, int start_index, int end_index ) {

        val scaling = path.numPoints * 10 // HACK: wired for now
        val start = start_index * scaling / descriptor_count
        val end = end_index * scaling / descriptor_count
        for (where_in_spline in start until end) {

            path.calculatePoint(positionStart, (where_in_spline / scaling.toFloat()).toDouble())
            path.calculatePoint(positionEnd, ((where_in_spline + 1) / scaling.toFloat()).toDouble())
            path.calculatePoint(positionBeyond, ((where_in_spline + 2) / scaling.toFloat()).toDouble())
            /*

                 */
            if (where_in_spline == start) {
                v1 = startVertexCache

                /*
                 * calculate two vectors that are normal to our bond segment - R and S.
                 */

                // go from vector previous to next, then set up normal to previous to current
                //   this should be the bisector normal, in theory

                p1p2.setAll(positionEnd)
                p1p2.subtract(positionStart)
                P.setAll(randomPoint)
                R.setAll(p1p2)
                R.cross(P)
                S.setAll(R)
                S.cross(p1p2)
                R.normalize()
                S.normalize()
                oldR.setAll(R)
                // calculate and cache
                for (slice in 0..numSlices) {

                    val angleInRadians1 = slice.toDouble() / numSlices.toDouble() * (Math.PI * 2f)

                    val s1 = radius * MathUtil.sin(angleInRadians1)

                    val c1 = radius * MathUtil.cos(angleInRadians1)


                    x1 = R.x * c1 + S.x * s1
                    y1 = R.y * c1 + S.y * s1
                    z1 = R.z * c1 + S.z * s1

                    v1[slice * 3] = (x1 + positionStart.x).toFloat()
                    v1[slice * 3 + 1] = (y1 + positionStart.y).toFloat()
                    v1[slice * 3 + 2] = (z1 + positionStart.z).toFloat()
                }

                // render a "connection tube" to bridge between possible
                // previous section of cartoon
                connectionTube()
            }


/*
                 * calculate two vectors that are normal to our bond segment - R and S.
                 */

            // go from vector previous to next, then set up normal to previous to current
            //   this should be the bisector normal, in theory

            p1p2.setAll(positionBeyond)
            p1p2.subtract(positionStart)
            P.setAll(positionEnd)
            P.subtract(positionStart)
            // R.setAll(p1p2);
            R.setAll(randomPoint)
            R.cross(P)
            S.setAll(R)
            S.cross(P) // change to cross only our segment of interest!!
            R.normalize()
            S.normalize()

            // ***
            // val thedot = oldR.dot(R)
            // @SuppressLint("DefaultLocale") val dot = String.format("%6.2f", thedot)

            color = redColor.clone()
            //                if ((thedot < 0.7) && (thedot >= 0.0)) {
            //                    color = green_color.clone();
            //                    Timber.i("dot =" + dot + " index is " + where_in_spline + "    UGH    green ****");
            //                    // v1 = rotateVertexCache(v1, numSlices + 1, thedot);
            //                } else if ((thedot < 0) && (thedot >= -0.5)) {
            //                    color = redgreen_color.clone();
            //                    Timber.i("dot =" + dot + " index is " + where_in_spline + "    UGH    yellow redgreen");
            //                    // v1 = rotateVertexCache(v1, numSlices + 1, thedot);
            //                } else if (thedot < -0.5) {
            //                    color = blue_color.clone();
            //                    Timber.i("dot =" + dot + " index is " + where_in_spline + "    UGH    blue");
            //                    // v1 = rotateVertexCache(v1, numSlices + 1, thedot);
            //                } else {
            //                    Timber.i("dot =" + dot + " index is " + where_in_spline + " case tbd");
            //                }
            oldR.setAll(R)

            // calculate and cache
            for (slice in 0..numSlices) {

                val angleInRadians1 = slice.toDouble() / numSlices.toDouble() * (Math.PI * 2f)

                val s1 = radius * MathUtil.sin(angleInRadians1)

                val c1 = radius * MathUtil.cos(angleInRadians1)


                x1 = R.x * c1 + S.x * s1
                y1 = R.y * c1 + S.y * s1
                z1 = R.z * c1 + S.z * s1

                endVertexCache[slice * 3] = (x1 + positionEnd.x).toFloat()
                endVertexCache[slice * 3 + 1] = (y1 + positionEnd.y).toFloat()
                endVertexCache[slice * 3 + 2] = (z1 + positionEnd.z).toFloat()
            }

            vertexData = BufferManager.getFloatArray(6 * (numSlices + 1) * STRIDE_IN_FLOATS)
            mOffset = BufferManager.floatArrayIndex

            //                if (thedot < 2.7) {
            //                    v1 = matchVertexCache(v1, numSlices + 1, v2, p1p2);
            //                }

            /*
             * always optimize the v1 cache - rotate it for best match to current conditions
             */
            // TODO: check this optimization for all cases
            v1 = matchVertexCache(v1, numSlices + 1, endVertexCache, p1p2)

            /*
                 * first generate the points, then map them to the position
                 * (1) version 1
                 * (2) version 2 - should transform the plane
                 * (3) version 3 - based on a spline calculation
                 */
            for (i in 0 until numSlices) {

                p1[0] = v1[i * 3]
                p1[1] = v1[i * 3 + 1]
                p1[2] = v1[i * 3 + 2]

                // first bottom point

                p3[0] = endVertexCache[i * 3]
                p3[1] = endVertexCache[i * 3 + 1]
                p3[2] = endVertexCache[i * 3 + 2]

                // SECOND BOTTOM point

                p2[0] = endVertexCache[(i + 1) * 3]
                p2[1] = endVertexCache[(i + 1) * 3 + 1]
                p2[2] = endVertexCache[(i + 1) * 3 + 2]

                // OK that is one triangle.

                // n = XYZ.getNormal(p1, p2, p3);
                n = XYZ.getNormal(p3, p2, p1)
                if (i == 0 && debugWhiteStripe) {
                    putTri(p1, p2, p3, n, whiteColor)
                } else {
                    putTri(p1, p2, p3, n, color)
                }


                // SECOND triangle NOW
                run {
                    // first top point

                    p1[0] = v1[i * 3]
                    p1[1] = v1[i * 3 + 1]
                    p1[2] = v1[i * 3 + 2]

                }
                run {
                    // SECOND BOTTOM point

                    p3[0] = endVertexCache[(i + 1) * 3]
                    p3[1] = endVertexCache[(i + 1) * 3 + 1]
                    p3[2] = endVertexCache[(i + 1) * 3 + 2]
                }
                run {
                    // SECOND top point

                    p2[0] = v1[(i + 1) * 3]
                    p2[1] = v1[(i + 1) * 3 + 1]
                    p2[2] = v1[(i + 1) * 3 + 2]

                }

                // n = XYZ.getNormal(p1, p2, p3);
                n = XYZ.getNormal(p3, p2, p1)
                // putTri(p1, p2, p3, n, color);
                if (i == 0 && debugWhiteStripe) {
                    putTri(p1, p2, p3, n, whiteColor)
                } else {
                    putTri(p1, p2, p3, n, color)
                }
            }
            // reuse the 2nd array
            System.arraycopy(endVertexCache, 0, v1, 0, (numSlices + 1) * 3)
            cache2_valid = true
            BufferManager.floatArrayIndex = mOffset
        }
    }

    private fun putTri(p1: FloatArray, p2: FloatArray, p3: FloatArray, n: FloatArray, color: FloatArray) {

        /*
         * HACK: this adapts the normal size to the molecule size.
         *    The normals are scaled to fit the viewport as a side-effect
         *    of scaling the molecule coordinates.
         */
        val normalBrightnessFactor = molecule.maxPostCenteringVectorMagnitude / BRIGHTNESS_FACTOR

        n[0] *= -normalBrightnessFactor.toFloat()
        n[1] *= -normalBrightnessFactor.toFloat()
        n[2] *= -normalBrightnessFactor.toFloat()

        vertexData[mOffset++] = p1[0]
        vertexData[mOffset++] = p1[1]
        vertexData[mOffset++] = p1[2]
        vertexData[mOffset++] = n[0]
        vertexData[mOffset++] = n[1]
        vertexData[mOffset++] = n[2]
        vertexData[mOffset++] = color[0]
        vertexData[mOffset++] = color[1]
        vertexData[mOffset++] = color[2]
        vertexData[mOffset++] = color[3]

        vertexData[mOffset++] = p2[0]
        vertexData[mOffset++] = p2[1]
        vertexData[mOffset++] = p2[2]
        vertexData[mOffset++] = n[0]
        vertexData[mOffset++] = n[1]
        vertexData[mOffset++] = n[2]
        vertexData[mOffset++] = color[0]
        vertexData[mOffset++] = color[1]
        vertexData[mOffset++] = color[2]
        vertexData[mOffset++] = color[3]

        vertexData[mOffset++] = p3[0]
        vertexData[mOffset++] = p3[1]
        vertexData[mOffset++] = p3[2]
        vertexData[mOffset++] = n[0]
        vertexData[mOffset++] = n[1]
        vertexData[mOffset++] = n[2]
        vertexData[mOffset++] = color[0]
        vertexData[mOffset++] = color[1]
        vertexData[mOffset++] = color[2]
        vertexData[mOffset++] = color[3]
    }

    // float[] start_vertex_cache = new float[(numSlices + 1) * 3];

    private fun matchVertexCache(cache: FloatArray, cache_size: Int, v2: FloatArray, target_vector: MotmVector3): FloatArray {
        var i = 0
        var maxMatch = -1.0
        var dotResult: Double
        var bestMatch = 0

        val testV1 = MotmVector3()
        val testV2 = MotmVector3(v2[0].toDouble(), v2[1].toDouble(), v2[2].toDouble())
        val result = MotmVector3()
        target_vector.normalize()

        while (i < cache_size) {
            testV1.setAll(
                    cache[i * 3].toDouble(), cache[i * 3 + 1].toDouble(), cache[i * 3 + 2].toDouble())
            result.setAll(testV2)
            result.subtract(testV1)
            result.normalize()

            dotResult = result.dot(target_vector)
            if (dotResult > maxMatch) {
                maxMatch = dotResult
                bestMatch = i
            }
            i++
        }

        //        String max_match_string = String.format("%6.2f", max_match);
        //        Timber.i("                                  UGH     best match = " + best_match + " max match = " + max_match_string);
        val newCache = FloatArray(cache_size * 3)

        /*
         * copy the vertex array - with an offset.
         *    Note that the 1st and last vertex are copies to close the circle.
         *    The loop makes sure to duplicate the new first and last entries
         */
        var j = bestMatch
        i = 0
        while (i < cache_size) {
            if (j >= cache_size) {
                j = 1 // skip duplicated 1st entry at j=0
            }
            if (i < cache_size - 1) {
                newCache[i * 3] = cache[j * 3]
                newCache[i * 3 + 1] = cache[j * 3 + 1]
                newCache[i * 3 + 2] = cache[j * 3 + 2]
                j++
            } else { // last entry
                newCache[i * 3] = newCache[0]
                newCache[i * 3 + 1] = newCache[1]
                newCache[i * 3 + 2] = newCache[2]
            }
            i++
        }
        return newCache
    }

    // TODO: delete this obsolete section
    // 1st try - now obsolete
    private fun rotateVertexCache(cache: FloatArray, cache_size: Int, how_much: Double): FloatArray {

        // how_much is the dot product between the old normal and the new normal -
        // rotate the cache so they match

        var angle = acos(how_much)
        angle = angle / (Math.PI * 2.0) * 360.0
        val offsetInDegrees = 360 - angle.toInt()
        val offsetInCache = cache_size * offsetInDegrees / 360

        val newCache = FloatArray(cache_size * 3)

        /*
         * copy the vertex array - with an offset.
         *    Note that the 1st and last vertex are copies to close the circle.
         *    The loop makes sure to duplicate the new first and last entries
         */
        var j = offsetInCache
        for (i in 0 until cache_size) {
            if (j >= cache_size) {
                j = 1 // skip duplicated 1st entry at j=0
            }
            if (i < cache_size - 1) {
                newCache[i * 3] = cache[j * 3]
                newCache[i * 3 + 1] = cache[j * 3 + 1]
                newCache[i * 3 + 2] = cache[j * 3 + 2]
                j++
            } else { // last entry
                newCache[i * 3] = newCache[0]
                newCache[i * 3 + 1] = newCache[1]
                newCache[i * 3 + 2] = newCache[2]
            }
        }
        return newCache
    }

    companion object {

        private const val debug = false

        //private val LOG_TAG = RenderModal::class.java.simpleName
        private const val CSF = 10  // Curve scale factor, number of points between atoms

        private const val POSITION_DATA_SIZE_IN_ELEMENTS = 3
        private const val NORMAL_DATA_SIZE_IN_ELEMENTS = 3
        private const val COLOR_DATA_SIZE_IN_ELEMENTS = 4

        private const val BYTES_PER_FLOAT = 4
        private const val BYTES_PER_SHORT = 2

        private const val STRIDE_IN_FLOATS = POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS + COLOR_DATA_SIZE_IN_ELEMENTS
        private const val STRIDE_IN_BYTES = STRIDE_IN_FLOATS * BYTES_PER_FLOAT

        // private static final float ELLIPSE_X_FACTOR = 2f / 9f;
        private const val ELLIPSE_X_FACTOR = 1f
        private const val ELLIPSE_Z_FACTOR = 1f


        private const val INITIAL_SLICES = 20
        private const val RIBBON_INITIAL_SLICES = 30

        private const val ribbonSlices = INITIAL_SLICES


    }
}