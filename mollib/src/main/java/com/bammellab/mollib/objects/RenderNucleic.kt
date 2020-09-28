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

@file:Suppress("unused")
package com.bammellab.mollib.objects

import com.bammellab.mollib.common.math.Vector3
import com.bammellab.mollib.protein.ChainRenderingDescriptor
import com.bammellab.mollib.protein.Molecule


/*
 * draw nice rectangular boxes representing nucleic acids (the ladder)
 */
class RenderNucleic(private val mMol: Molecule) {
    private val mBufMgr: BufferManager = mMol.mBufMgr

    private var mNumIndices = 0
    private val mCylinderIndexCount: Int = 0
    private lateinit var mVertexData: FloatArray
    private var mOffset: Int = 0
    internal val vboTopAndBottom = IntArray(1)
    internal val vboBody = IntArray(1)
    internal val ibo = IntArray(1)

    private fun getColor(crd: ChainRenderingDescriptor): FloatArray {
        val anAtom = crd.nucleicCornerAtom
        return when {
            anAtom.residueName == "DC" -> C_color
            anAtom.residueName == "DT" -> T_color
            anAtom.residueName == "C" -> C_color
            anAtom.residueName == "T" -> T_color
            anAtom.residueName == "U" -> U_color
            anAtom.residueName == "DA" -> A_color
            anAtom.residueName == "DG" -> G_color
            anAtom.residueName == "A" -> A_color
            anAtom.residueName == "G" -> G_color
            // it is something else.  Flag it with white
            else -> white_color
        }
    }

    fun renderNucleic() {

        /*
         * TODO: scaling of brightness relative to size (normals are scaled down with the molecule!!
         */
        normal_brightness_factor = mMol.dcOffset / 3

        var pdbBackboneList: List<*>
        var chainEntry: ChainRenderingDescriptor

        for (i in 0 until mMol.listofChainDescriptorLists.size) {
            pdbBackboneList = mMol.listofChainDescriptorLists[i]
            for (j in pdbBackboneList.indices) {
                chainEntry = pdbBackboneList[j] as ChainRenderingDescriptor
                if (chainEntry.nucleicType == ChainRenderingDescriptor.PURINE) {
                    // Log.d("LOG_TAG", "purine: " + atom.atom_number);
                    render(chainEntry, 3)
                }
                if (chainEntry.nucleicType == ChainRenderingDescriptor.PYRIMIDINE) {
                    // Log.d("LOG_TAG", "pyrimidine: " + atom.atom_number);
                    render(chainEntry, 5)
                }
            }
        }
        mBufMgr.transferToGl()
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

        mVertexData = mBufMgr.getFloatArray(36 * STRIDE_IN_FLOATS)
        mOffset = mBufMgr.floatArrayIndex

        corner1.setAll(crd.nucleicCornerAtom.atomPosition)

        // set P to vector defining side of rectangle
        //    vector is corner to guide atom
        mMol.P.setAll(crd.nucleicGuideAtom.atomPosition)
        mMol.P.subtract(corner1)
        mMol.P.normalize()

        // fix corner2 along P - and set its length,
        //   based at corner1
        corner2.setAll(mMol.P)
        corner2.multiply(scaler.toDouble())
        corner2.add(corner1)

        // set R to vector across the rectangle
        //    vector is corner atom to planar atom
        mMol.R.setAll(crd.nucleicPlanarAtom.atomPosition)
        mMol.R.subtract(corner1)
        mMol.R.normalize()

        // S - normal - now we can get the normal to the plane
        mMol.S.setAll(mMol.P)
        mMol.S.cross(mMol.R)

        // now cross back to get the vector for our other corner

        corner3.setAll(mMol.S)
        corner3.cross(mMol.P)
        // corner3.multiply((double) scaler * 0.75);
        corner3.multiply(2.0)

        // corner3 is still a vector based at the origin.
        //  borrow it for a minute to figure out corner4

        corner4.setAll(corner3)
        corner4.add(corner2)

        // now offset corner3 by corner1

        corner3.add(corner1)

        /*
         *  TOP
         */

        mMol.S.multiply(0.25)

        corner1t.setAll(corner1)
        corner2t.setAll(corner2)
        corner3t.setAll(corner3)
        corner4t.setAll(corner4)
        corner1t.add(mMol.S)
        corner2t.add(mMol.S)
        corner3t.add(mMol.S)
        corner4t.add(mMol.S)

        // OK we should have all 4 corners now
        //  do the triangles

        mMol.p1[0] = corner1t.x.toFloat()
        mMol.p1[1] = corner1t.y.toFloat()
        mMol.p1[2] = corner1t.z.toFloat()

        mMol.p2[0] = corner3t.x.toFloat()
        mMol.p2[1] = corner3t.y.toFloat()
        mMol.p2[2] = corner3t.z.toFloat()

        mMol.p3[0] = corner2t.x.toFloat()
        mMol.p3[1] = corner2t.y.toFloat()
        mMol.p3[2] = corner2t.z.toFloat()
        // OK that is one triangle.

        n = XYZ.getNormal(mMol.p3, mMol.p2, mMol.p1)
        putTri(mMol.p1, mMol.p2, mMol.p3, n, theColor)

        mMol.p1[0] = corner3t.x.toFloat()
        mMol.p1[1] = corner3t.y.toFloat()
        mMol.p1[2] = corner3t.z.toFloat()

        mMol.p2[0] = corner4t.x.toFloat()
        mMol.p2[1] = corner4t.y.toFloat()
        mMol.p2[2] = corner4t.z.toFloat()

        mMol.p3[0] = corner2t.x.toFloat()
        mMol.p3[1] = corner2t.y.toFloat()
        mMol.p3[2] = corner2t.z.toFloat()
        // OK that is one triangle.

        n = XYZ.getNormal(mMol.p3, mMol.p2, mMol.p1)
        putTri(mMol.p1, mMol.p2, mMol.p3, n, theColor)


        /*
         *  BOTTOM
         */
        corner1b.setAll(corner1)
        corner2b.setAll(corner2)
        corner3b.setAll(corner3)
        corner4b.setAll(corner4)
        corner1b.subtract(mMol.S)
        corner2b.subtract(mMol.S)
        corner3b.subtract(mMol.S)
        corner4b.subtract(mMol.S)

        // OK we should have all 4 corners now
        //  do the triangles

        mMol.p1[0] = corner1b.x.toFloat()
        mMol.p1[1] = corner1b.y.toFloat()
        mMol.p1[2] = corner1b.z.toFloat()

        mMol.p2[0] = corner2b.x.toFloat()
        mMol.p2[1] = corner2b.y.toFloat()
        mMol.p2[2] = corner2b.z.toFloat()

        mMol.p3[0] = corner3b.x.toFloat()
        mMol.p3[1] = corner3b.y.toFloat()
        mMol.p3[2] = corner3b.z.toFloat()
        // OK that is one triangle.

        n = XYZ.getNormal(mMol.p3, mMol.p2, mMol.p1)
        putTri(mMol.p1, mMol.p2, mMol.p3, n, theColor)

        mMol.p1[0] = corner3b.x.toFloat()
        mMol.p1[1] = corner3b.y.toFloat()
        mMol.p1[2] = corner3b.z.toFloat()

        mMol.p2[0] = corner2b.x.toFloat()
        mMol.p2[1] = corner2b.y.toFloat()
        mMol.p2[2] = corner2b.z.toFloat()

        mMol.p3[0] = corner4b.x.toFloat()
        mMol.p3[1] = corner4b.y.toFloat()
        mMol.p3[2] = corner4b.z.toFloat()
        // OK that is one triangle.

        n = XYZ.getNormal(mMol.p3, mMol.p2, mMol.p1)
        putTri(mMol.p1, mMol.p2, mMol.p3, n, theColor)

        mNumIndices = mOffset
        mBufMgr.floatArrayIndex = mOffset

        /* **************************
         * END number 1
         */
        mMol.p1[0] = corner1t.x.toFloat()
        mMol.p1[1] = corner1t.y.toFloat()
        mMol.p1[2] = corner1t.z.toFloat()

        mMol.p2[0] = corner1b.x.toFloat()
        mMol.p2[1] = corner1b.y.toFloat()
        mMol.p2[2] = corner1b.z.toFloat()

        mMol.p3[0] = corner3b.x.toFloat()
        mMol.p3[1] = corner3b.y.toFloat()
        mMol.p3[2] = corner3b.z.toFloat()

        n = XYZ.getNormal(mMol.p3, mMol.p2, mMol.p1)
        putTri(mMol.p1, mMol.p2, mMol.p3, n, theColor)

        mMol.p1[0] = corner1t.x.toFloat()
        mMol.p1[1] = corner1t.y.toFloat()
        mMol.p1[2] = corner1t.z.toFloat()

        mMol.p2[0] = corner3b.x.toFloat()
        mMol.p2[1] = corner3b.y.toFloat()
        mMol.p2[2] = corner3b.z.toFloat()

        mMol.p3[0] = corner3t.x.toFloat()
        mMol.p3[1] = corner3t.y.toFloat()
        mMol.p3[2] = corner3t.z.toFloat()

        n = XYZ.getNormal(mMol.p3, mMol.p2, mMol.p1)
        putTri(mMol.p1, mMol.p2, mMol.p3, n, theColor)


        /* **************************
         * END number 2
         */
        mMol.p1[0] = corner3t.x.toFloat()
        mMol.p1[1] = corner3t.y.toFloat()
        mMol.p1[2] = corner3t.z.toFloat()

        mMol.p2[0] = corner3b.x.toFloat()
        mMol.p2[1] = corner3b.y.toFloat()
        mMol.p2[2] = corner3b.z.toFloat()

        mMol.p3[0] = corner4b.x.toFloat()
        mMol.p3[1] = corner4b.y.toFloat()
        mMol.p3[2] = corner4b.z.toFloat()

        n = XYZ.getNormal(mMol.p3, mMol.p2, mMol.p1)
        putTri(mMol.p1, mMol.p2, mMol.p3, n, theColor)

        mMol.p1[0] = corner3t.x.toFloat()
        mMol.p1[1] = corner3t.y.toFloat()
        mMol.p1[2] = corner3t.z.toFloat()

        mMol.p2[0] = corner4b.x.toFloat()
        mMol.p2[1] = corner4b.y.toFloat()
        mMol.p2[2] = corner4b.z.toFloat()

        mMol.p3[0] = corner4t.x.toFloat()
        mMol.p3[1] = corner4t.y.toFloat()
        mMol.p3[2] = corner4t.z.toFloat()

        n = XYZ.getNormal(mMol.p3, mMol.p2, mMol.p1)
        putTri(mMol.p1, mMol.p2, mMol.p3, n, theColor)

        /* **************************
         * END number 3
         */
        mMol.p1[0] = corner4t.x.toFloat()
        mMol.p1[1] = corner4t.y.toFloat()
        mMol.p1[2] = corner4t.z.toFloat()

        mMol.p2[0] = corner4b.x.toFloat()
        mMol.p2[1] = corner4b.y.toFloat()
        mMol.p2[2] = corner4b.z.toFloat()

        mMol.p3[0] = corner2b.x.toFloat()
        mMol.p3[1] = corner2b.y.toFloat()
        mMol.p3[2] = corner2b.z.toFloat()

        n = XYZ.getNormal(mMol.p3, mMol.p2, mMol.p1)
        putTri(mMol.p1, mMol.p2, mMol.p3, n, theColor)

        mMol.p1[0] = corner4t.x.toFloat()
        mMol.p1[1] = corner4t.y.toFloat()
        mMol.p1[2] = corner4t.z.toFloat()

        mMol.p2[0] = corner2b.x.toFloat()
        mMol.p2[1] = corner2b.y.toFloat()
        mMol.p2[2] = corner2b.z.toFloat()

        mMol.p3[0] = corner2t.x.toFloat()
        mMol.p3[1] = corner2t.y.toFloat()
        mMol.p3[2] = corner2t.z.toFloat()

        n = XYZ.getNormal(mMol.p3, mMol.p2, mMol.p1)
        putTri(mMol.p1, mMol.p2, mMol.p3, n, theColor)

        /* **************************
         * END number 4
         */
        mMol.p1[0] = corner2t.x.toFloat()
        mMol.p1[1] = corner2t.y.toFloat()
        mMol.p1[2] = corner2t.z.toFloat()

        mMol.p2[0] = corner2b.x.toFloat()
        mMol.p2[1] = corner2b.y.toFloat()
        mMol.p2[2] = corner2b.z.toFloat()

        mMol.p3[0] = corner1b.x.toFloat()
        mMol.p3[1] = corner1b.y.toFloat()
        mMol.p3[2] = corner1b.z.toFloat()

        n = XYZ.getNormal(mMol.p3, mMol.p2, mMol.p1)
        putTri(mMol.p1, mMol.p2, mMol.p3, n, theColor)

        mMol.p1[0] = corner2t.x.toFloat()
        mMol.p1[1] = corner2t.y.toFloat()
        mMol.p1[2] = corner2t.z.toFloat()

        mMol.p2[0] = corner1b.x.toFloat()
        mMol.p2[1] = corner1b.y.toFloat()
        mMol.p2[2] = corner1b.z.toFloat()

        mMol.p3[0] = corner1t.x.toFloat()
        mMol.p3[1] = corner1t.y.toFloat()
        mMol.p3[2] = corner1t.z.toFloat()

        n = XYZ.getNormal(mMol.p3, mMol.p2, mMol.p1)
        putTri(mMol.p1, mMol.p2, mMol.p3, n, theColor)

        mNumIndices = mOffset
        mBufMgr.floatArrayIndex = mOffset
    }

    private fun putTri(p1: FloatArray, p2: FloatArray, p3: FloatArray, n: FloatArray, color: FloatArray) {

        n[0] *= -normal_brightness_factor
        n[1] *= -normal_brightness_factor
        n[2] *= -normal_brightness_factor

        mVertexData[mOffset++] = p1[0]
        mVertexData[mOffset++] = p1[1]
        mVertexData[mOffset++] = p1[2]
        mVertexData[mOffset++] = n[0]
        mVertexData[mOffset++] = n[1]
        mVertexData[mOffset++] = n[2]
        mVertexData[mOffset++] = color[0]
        mVertexData[mOffset++] = color[1]
        mVertexData[mOffset++] = color[2]
        mVertexData[mOffset++] = color[3]

        mVertexData[mOffset++] = p2[0]
        mVertexData[mOffset++] = p2[1]
        mVertexData[mOffset++] = p2[2]
        mVertexData[mOffset++] = n[0]
        mVertexData[mOffset++] = n[1]
        mVertexData[mOffset++] = n[2]
        mVertexData[mOffset++] = color[0]
        mVertexData[mOffset++] = color[1]
        mVertexData[mOffset++] = color[2]
        mVertexData[mOffset++] = color[3]

        mVertexData[mOffset++] = p3[0]
        mVertexData[mOffset++] = p3[1]
        mVertexData[mOffset++] = p3[2]
        mVertexData[mOffset++] = n[0]
        mVertexData[mOffset++] = n[1]
        mVertexData[mOffset++] = n[2]
        mVertexData[mOffset++] = color[0]
        mVertexData[mOffset++] = color[1]
        mVertexData[mOffset++] = color[2]
        mVertexData[mOffset++] = color[3]
    }

    companion object {

        private val LOG_TAG = RenderNucleic::class.java.simpleName
        private val corner1 = Vector3()
        private val corner2 = Vector3()
        private val corner3 = Vector3()
        private val corner4 = Vector3()
        private val corner1t = Vector3()
        private val corner2t = Vector3()
        private val corner3t = Vector3()
        private val corner4t = Vector3()
        private val corner1b = Vector3()
        private val corner2b = Vector3()
        private val corner3b = Vector3()
        private val corner4b = Vector3()
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
