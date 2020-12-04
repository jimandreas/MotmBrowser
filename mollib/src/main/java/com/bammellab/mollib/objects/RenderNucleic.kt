/*
 *  Copyright 2020 Bammellab / James Andreas
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

@file:Suppress("unused")

package com.bammellab.mollib.objects

import com.bammellab.mollib.common.math.MotmVector3
import com.bammellab.mollib.objects.GlobalObject.BRIGHTNESS_FACTOR
import com.kotmol.pdbParser.ChainRenderingDescriptor
import com.kotmol.pdbParser.Molecule

/*
 * draw nice rectangular boxes representing nucleic acids (the ladder)
 */
class RenderNucleic(private val molecule: Molecule) {

    private var numIndices = 0
    private val cylinderIndexCount: Int = 0
    private lateinit var vertexData: FloatArray
    private var mOffset: Int = 0
    internal val vboTopAndBottom = IntArray(1)
    internal val vboBody = IntArray(1)
    internal val ibo = IntArray(1)

    private val p1p2 = MotmVector3()
    private val pvec = MotmVector3()
    private val rvec = MotmVector3()
    private val svec = MotmVector3()
    private val p1 = FloatArray(3)
    private val p2 = FloatArray(3)
    private val p3 = FloatArray(3)

    private fun getColor(crd: ChainRenderingDescriptor): FloatArray {
        val anAtom = crd.nucleicCornerAtom ?: return white_color
        return when (anAtom.residueName) {
            "DC" -> C_color
            "DT" -> T_color
            "C" -> C_color
            "T" -> T_color
            "U" -> U_color
            "DA" -> A_color
            "DG" -> G_color
            "A" -> A_color
            "G" -> G_color
            // it is something else.  Flag it with white
            else -> white_color
        }
    }

    fun renderNucleic() {

        /*
         * TODO: scaling of brightness relative to size (normals are scaled down with the molecule!!
         */
        normal_brightness_factor = (molecule.maxPostCenteringVectorMagnitude / BRIGHTNESS_FACTOR).toFloat()

        var pdbBackboneList: List<*>
        var chainEntry: ChainRenderingDescriptor

        for (i in 0 until molecule.listofChainDescriptorLists.size) {
            pdbBackboneList = molecule.listofChainDescriptorLists[i]
            for (j in pdbBackboneList.indices) {
                chainEntry = pdbBackboneList[j]
                if (chainEntry.nucleicType == ChainRenderingDescriptor.NucleicType.PURINE) {
                    // Log.d("LOG_TAG", "purine: " + atom.atom_number);
                    render(chainEntry, 3)
                }
                if (chainEntry.nucleicType == ChainRenderingDescriptor.NucleicType.PYRIMIDINE) {
                    // Log.d("LOG_TAG", "pyrimidine: " + atom.atom_number);
                    render(chainEntry, 5)
                }
            }
        }
        BufferManager.transferToGl()
    }

    /*
     * Draw a Nucleic ladder rectangle
     *
     *                P vec
     *   corner2 ------------ corner1 (corner atom)
     *   +                          +
     *   +                          +  R vec
     *   +                          +
     *   corner4 ------------ corner3
     */

    private fun render(crd: ChainRenderingDescriptor, scaler: Int) {

        val theColor = getColor(crd)
        // not all nucleic residues specify their ladder element (5CCW)
        // other challenges - 1FKA (corners atoms not specified)

        if (crd.nucleicCornerAtom == null
                || crd.nucleicGuideAtom == null
                || crd.nucleicPlanarAtom == null) {
            return
        }

        vertexData = BufferManager.getFloatArray(36 * STRIDE_IN_FLOATS)
        mOffset = BufferManager.floatArrayIndex

        corner1.setAll(MotmVector3(crd.nucleicCornerAtom!!.atomPosition))

        // set P to vector defining side of rectangle
        //    vector is corner to guide atom
        pvec.setAll(MotmVector3(crd.nucleicGuideAtom!!.atomPosition))
        pvec.subtract(corner1)
        pvec.normalize()

        // fix corner2 along P - and set its length,
        //   based at corner1
        corner2.setAll(pvec)
        corner2.multiply(scaler.toFloat())
        corner2.add(corner1)

        // set R to vector across the rectangle
        //    vector is corner atom to planar atom
        rvec.setAll(MotmVector3(crd.nucleicPlanarAtom!!.atomPosition))
        rvec.subtract(corner1)
        rvec.normalize()

        // S - normal - now we can get the normal to the plane
        svec.setAll(pvec)
        svec.cross(rvec)

        // now cross back to get the vector for our other corner

        corner3.setAll(svec)
        corner3.cross(pvec)
        // corner3.multiply((double) scaler * 0.75);
        corner3.multiply(2.0f)

        // corner3 is still a vector based at the origin.
        //  borrow it for a minute to figure out corner4

        corner4.setAll(corner3)
        corner4.add(corner2)

        // now offset corner3 by corner1

        corner3.add(corner1)

        /*
         *  TOP
         */

        svec.multiply(0.25f)

        corner1t.setAll(corner1)
        corner2t.setAll(corner2)
        corner3t.setAll(corner3)
        corner4t.setAll(corner4)
        corner1t.add(svec)
        corner2t.add(svec)
        corner3t.add(svec)
        corner4t.add(svec)

        // OK we should have all 4 corners now
        //  do the triangles

        p1[0] = corner1t.x.toFloat()
        p1[1] = corner1t.y.toFloat()
        p1[2] = corner1t.z.toFloat()

        p2[0] = corner3t.x.toFloat()
        p2[1] = corner3t.y.toFloat()
        p2[2] = corner3t.z.toFloat()

        p3[0] = corner2t.x.toFloat()
        p3[1] = corner2t.y.toFloat()
        p3[2] = corner2t.z.toFloat()
        // OK that is one triangle.

        n = XYZ.getNormal(p3, p2, p1)
        putTri(p1, p2, p3, n, theColor)

        p1[0] = corner3t.x.toFloat()
        p1[1] = corner3t.y.toFloat()
        p1[2] = corner3t.z.toFloat()

        p2[0] = corner4t.x.toFloat()
        p2[1] = corner4t.y.toFloat()
        p2[2] = corner4t.z.toFloat()

        p3[0] = corner2t.x.toFloat()
        p3[1] = corner2t.y.toFloat()
        p3[2] = corner2t.z.toFloat()
        // OK that is one triangle.

        n = XYZ.getNormal(p3, p2, p1)
        putTri(p1, p2, p3, n, theColor)


        /*
         *  BOTTOM
         */
        corner1b.setAll(corner1)
        corner2b.setAll(corner2)
        corner3b.setAll(corner3)
        corner4b.setAll(corner4)
        corner1b.subtract(svec)
        corner2b.subtract(svec)
        corner3b.subtract(svec)
        corner4b.subtract(svec)

        // OK we should have all 4 corners now
        //  do the triangles

        p1[0] = corner1b.x.toFloat()
        p1[1] = corner1b.y.toFloat()
        p1[2] = corner1b.z.toFloat()

        p2[0] = corner2b.x.toFloat()
        p2[1] = corner2b.y.toFloat()
        p2[2] = corner2b.z.toFloat()

        p3[0] = corner3b.x.toFloat()
        p3[1] = corner3b.y.toFloat()
        p3[2] = corner3b.z.toFloat()
        // OK that is one triangle.

        n = XYZ.getNormal(p3, p2, p1)
        putTri(p1, p2, p3, n, theColor)

        p1[0] = corner3b.x.toFloat()
        p1[1] = corner3b.y.toFloat()
        p1[2] = corner3b.z.toFloat()

        p2[0] = corner2b.x.toFloat()
        p2[1] = corner2b.y.toFloat()
        p2[2] = corner2b.z.toFloat()

        p3[0] = corner4b.x.toFloat()
        p3[1] = corner4b.y.toFloat()
        p3[2] = corner4b.z.toFloat()
        // OK that is one triangle.

        n = XYZ.getNormal(p3, p2, p1)
        putTri(p1, p2, p3, n, theColor)

        numIndices = mOffset
        BufferManager.floatArrayIndex = mOffset

        /* **************************
         * END number 1
         */
        p1[0] = corner1t.x.toFloat()
        p1[1] = corner1t.y.toFloat()
        p1[2] = corner1t.z.toFloat()

        p2[0] = corner1b.x.toFloat()
        p2[1] = corner1b.y.toFloat()
        p2[2] = corner1b.z.toFloat()

        p3[0] = corner3b.x.toFloat()
        p3[1] = corner3b.y.toFloat()
        p3[2] = corner3b.z.toFloat()

        n = XYZ.getNormal(p3, p2, p1)
        putTri(p1, p2, p3, n, theColor)

        p1[0] = corner1t.x.toFloat()
        p1[1] = corner1t.y.toFloat()
        p1[2] = corner1t.z.toFloat()

        p2[0] = corner3b.x.toFloat()
        p2[1] = corner3b.y.toFloat()
        p2[2] = corner3b.z.toFloat()

        p3[0] = corner3t.x.toFloat()
        p3[1] = corner3t.y.toFloat()
        p3[2] = corner3t.z.toFloat()

        n = XYZ.getNormal(p3, p2, p1)
        putTri(p1, p2, p3, n, theColor)


        /* **************************
         * END number 2
         */
        p1[0] = corner3t.x.toFloat()
        p1[1] = corner3t.y.toFloat()
        p1[2] = corner3t.z.toFloat()

        p2[0] = corner3b.x.toFloat()
        p2[1] = corner3b.y.toFloat()
        p2[2] = corner3b.z.toFloat()

        p3[0] = corner4b.x.toFloat()
        p3[1] = corner4b.y.toFloat()
        p3[2] = corner4b.z.toFloat()

        n = XYZ.getNormal(p3, p2, p1)
        putTri(p1, p2, p3, n, theColor)

        p1[0] = corner3t.x.toFloat()
        p1[1] = corner3t.y.toFloat()
        p1[2] = corner3t.z.toFloat()

        p2[0] = corner4b.x.toFloat()
        p2[1] = corner4b.y.toFloat()
        p2[2] = corner4b.z.toFloat()

        p3[0] = corner4t.x.toFloat()
        p3[1] = corner4t.y.toFloat()
        p3[2] = corner4t.z.toFloat()

        n = XYZ.getNormal(p3, p2, p1)
        putTri(p1, p2, p3, n, theColor)

        /* **************************
         * END number 3
         */
        p1[0] = corner4t.x.toFloat()
        p1[1] = corner4t.y.toFloat()
        p1[2] = corner4t.z.toFloat()

        p2[0] = corner4b.x.toFloat()
        p2[1] = corner4b.y.toFloat()
        p2[2] = corner4b.z.toFloat()

        p3[0] = corner2b.x.toFloat()
        p3[1] = corner2b.y.toFloat()
        p3[2] = corner2b.z.toFloat()

        n = XYZ.getNormal(p3, p2, p1)
        putTri(p1, p2, p3, n, theColor)

        p1[0] = corner4t.x.toFloat()
        p1[1] = corner4t.y.toFloat()
        p1[2] = corner4t.z.toFloat()

        p2[0] = corner2b.x.toFloat()
        p2[1] = corner2b.y.toFloat()
        p2[2] = corner2b.z.toFloat()

        p3[0] = corner2t.x.toFloat()
        p3[1] = corner2t.y.toFloat()
        p3[2] = corner2t.z.toFloat()

        n = XYZ.getNormal(p3, p2, p1)
        putTri(p1, p2, p3, n, theColor)

        /* **************************
         * END number 4
         */
        p1[0] = corner2t.x.toFloat()
        p1[1] = corner2t.y.toFloat()
        p1[2] = corner2t.z.toFloat()

        p2[0] = corner2b.x.toFloat()
        p2[1] = corner2b.y.toFloat()
        p2[2] = corner2b.z.toFloat()

        p3[0] = corner1b.x.toFloat()
        p3[1] = corner1b.y.toFloat()
        p3[2] = corner1b.z.toFloat()

        n = XYZ.getNormal(p3, p2, p1)
        putTri(p1, p2, p3, n, theColor)

        p1[0] = corner2t.x.toFloat()
        p1[1] = corner2t.y.toFloat()
        p1[2] = corner2t.z.toFloat()

        p2[0] = corner1b.x.toFloat()
        p2[1] = corner1b.y.toFloat()
        p2[2] = corner1b.z.toFloat()

        p3[0] = corner1t.x.toFloat()
        p3[1] = corner1t.y.toFloat()
        p3[2] = corner1t.z.toFloat()

        n = XYZ.getNormal(p3, p2, p1)
        putTri(p1, p2, p3, n, theColor)

        numIndices = mOffset
        BufferManager.floatArrayIndex = mOffset
    }

    private fun putTri(p1: FloatArray, p2: FloatArray, p3: FloatArray, n: FloatArray, color: FloatArray) {

        n[0] *= -normal_brightness_factor
        n[1] *= -normal_brightness_factor
        n[2] *= -normal_brightness_factor

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

    companion object {

        private val LOG_TAG = RenderNucleic::class.java.simpleName
        private val corner1 = MotmVector3()
        private val corner2 = MotmVector3()
        private val corner3 = MotmVector3()
        private val corner4 = MotmVector3()
        private val corner1t = MotmVector3()
        private val corner2t = MotmVector3()
        private val corner3t = MotmVector3()
        private val corner4t = MotmVector3()
        private val corner1b = MotmVector3()
        private val corner2b = MotmVector3()
        private val corner3b = MotmVector3()
        private val corner4b = MotmVector3()
        private var n = FloatArray(3)

        // see:  http://jmol.sourceforge.net/jscolors/#Residues: amino acids, nucleotides
        private val A_color = floatArrayOf(160f / 255f, 160f / 255f, 255f / 255f, 1.0f)
        private val G_color = floatArrayOf(255f / 255f, 112f / 255f, 112f / 255f, 1.0f)
        private val I_color = floatArrayOf(128f / 255f, 255f / 255f, 255f / 255f, 1.0f)
        private val C_color = floatArrayOf(255f / 255f, 140f / 255f, 75f / 255f, 1.0f)
        private val T_color = floatArrayOf(160f / 255f, 255f / 255f, 160f / 255f, 1.0f)
        private val U_color = floatArrayOf(255f / 255f, 128f / 255f, 160f / 128f, 1.0f)

        private val white_color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)

        private const val POSITION_DATA_SIZE_IN_ELEMENTS = 3
        private const val NORMAL_DATA_SIZE_IN_ELEMENTS = 3
        private const val COLOR_DATA_SIZE_IN_ELEMENTS = 4

        private const val BYTES_PER_FLOAT = 4
        private const val BYTES_PER_SHORT = 2

        private const val STRIDE_IN_FLOATS = POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS + COLOR_DATA_SIZE_IN_ELEMENTS
        private const val STRIDE_IN_BYTES = STRIDE_IN_FLOATS * BYTES_PER_FLOAT

        private var normal_brightness_factor = 7f
        private const val ELLIPSE_X_FACTOR = 2f / 9f
        private const val ELLIPSE_Z_FACTOR = 1f
    }
}
