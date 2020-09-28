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

import com.bammellab.mollib.common.math.MathUtil
import com.bammellab.mollib.common.math.Vector3
import com.bammellab.mollib.protein.AtomInfo
import com.bammellab.mollib.protein.Molecule
import com.bammellab.mollib.protein.PdbAtom

/*  Memory manager:
   "Reserve" from current float array
      Underlying manager builds float arrays in smaller blocks
      to avoid one large allocation and copying.   Blocks
      are retained.   All rendering is triangles with Stride.

      One float[] array is used for accumulation.  When full
      a new (or recycled) FloatBuffer is allocated and
      the float[] array is copied into it.

 */
class SegmentAtomToAtomBond(private val mMol: Molecule) {

    private lateinit var mVertexData: FloatArray
    private var mOffset: Int = 0

    private val mBufMgr: BufferManager = mMol.mBufMgr

    fun genBondCylinders(
            numSlices: Int,
            radius: Float,
            atom1: PdbAtom,
            atom2: PdbAtom,
            color: FloatArray,
            atomInfo: ParserAtomInfo) {

        /*
         * TODO: scaling of brightness relative to size (normals are scaled down with the molecule!!
         */
        normal_brightness_factor = mMol.dcOffset / 3

        var i = 0
        val startColor: FloatArray
        val endColor: FloatArray
        /* start is atom1, end is atom2 */
        val positionStart = atom1.atomPosition
        val positionEnd = atom2.atomPosition
        val positionMid = Vector3()

        positionMid.x = (positionEnd.x + positionStart.x) / 2.0
        positionMid.y = (positionEnd.y + positionStart.y) / 2.0
        positionMid.z = (positionEnd.z + positionStart.z) / 2.0


        var ai: AtomInfo?
        var elementSymbol: String = atom1.elementSymbol
        ai = atomInfo.atomNameToAtomInfoHash[elementSymbol]
        startColor = ai?.color ?: color
        elementSymbol = atom2.elementSymbol
        ai = atomInfo.atomNameToAtomInfoHash[elementSymbol]
        endColor = ai?.color ?: color
        

        // debugging
        //        position_start.x = 0;
        //        position_start.y = 0;
        //        position_start.z = 0;
        //
        //        position_end.x = 0;
        //        position_end.y = 1;
        //        position_end.z = 0;

        /*
         * calculate two vectors that are normal to our bond segment - R and S.
         */
        //        Vector3 p1p2 = new Vector3();
        //        p1p2.setAll(position_end);
        //        p1p2.subtract(position_start);
        //        Vector3 P = new Vector3(Math.random(), Math.random(), Math.random());
        //        Vector3 R = new Vector3(p1p2);
        //        R.cross(P);
        //        Vector3 S = new Vector3(R);
        //        S.cross(p1p2);
        //        R.normalize();
        //        S.normalize();

        mMol.p1p2.setAll(positionEnd)
        mMol.p1p2.subtract(positionStart)
        mMol.P.x = Math.random()
        mMol.P.y = Math.random()
        mMol.P.z = Math.random()
        // Vector3 R = new Vector3(p1p2);
        mMol.R.setAll(mMol.p1p2)
        mMol.R.cross(mMol.P)
        // Vector3 S = new Vector3(R);
        mMol.S.setAll(mMol.R)
        // S.cross(p1p2);
        mMol.S.cross(mMol.p1p2)
        //        R.normalize();
        //        S.normalize();
        mMol.R.normalize()
        mMol.S.normalize()

        var x1: Double
        var y1: Double
        var z1: Double
        var x2: Double
        var y2: Double
        var z2: Double

        /*
         * math: two tris per slice, wrapping for numSlices+1 (hit original again to close)
         *    doubled for two colors on each half
         */
        mVertexData = mBufMgr.getFloatArray(2 * 6 * (numSlices + 1) * STRIDE_IN_FLOATS)
        mOffset = mBufMgr.floatArrayIndex

        /*
         * first generate the points, then map them to the position
         * (1) version 1
         * (2) version 2 - should transform the plane
         * (3) version 3 - based on a spline calculation
         */

        //        float mMol.p1[] = new float[3];
        //        float mMol.p2[] = new float[3];
        //        float mMol.p3[] = new float[3];
        var n: FloatArray
        while (i <= numSlices) {

            val angleInRadians1 = (i.toDouble() / numSlices.toDouble()
                    * Math.PI * 2.0)
            val angleInRadians2 = ((i + 1).toDouble() / numSlices.toDouble()
                    * Math.PI * 2.0)

            val s1 = radius * MathUtil.sin(angleInRadians1)
            val s2 = radius * MathUtil.sin(angleInRadians2)
            val c1 = radius * MathUtil.cos(angleInRadians1)
            val c2 = radius * MathUtil.cos(angleInRadians2)

            /*
             * first the START to MID portion of the bond
             */

            // first top point

            x1 = mMol.R.x * c1 + mMol.S.x * s1
            y1 = mMol.R.y * c1 + mMol.S.y * s1
            z1 = mMol.R.z * c1 + mMol.S.z * s1

            mMol.p1[0] = (x1 + positionMid.x).toFloat()
            mMol.p1[1] = (y1 + positionMid.y).toFloat()
            mMol.p1[2] = (z1 + positionMid.z).toFloat()


            // first bottom point

            mMol.p2[0] = (x1 + positionStart.x).toFloat()
            mMol.p2[1] = (y1 + positionStart.y).toFloat()
            mMol.p2[2] = (z1 + positionStart.z).toFloat()


            // SECOND BOTTOM point
            x2 = mMol.R.x * c2 + mMol.S.x * s2
            y2 = mMol.R.y * c2 + mMol.S.y * s2
            z2 = mMol.R.z * c2 + mMol.S.z * s2

            mMol.p3[0] = (x2 + positionStart.x).toFloat()
            mMol.p3[1] = (y2 + positionStart.y).toFloat()
            mMol.p3[2] = (z2 + positionStart.z).toFloat()
            // OK that is one triangle.

            n = XYZ.getNormal(mMol.p1, mMol.p2, mMol.p3)
            putTri(n, startColor)

            // SECOND triangle NOW

            // first top point

            mMol.p1[0] = (x1 + positionMid.x).toFloat()
            mMol.p1[1] = (y1 + positionMid.y).toFloat()
            mMol.p1[2] = (z1 + positionMid.z).toFloat()


            // SECOND BOTTOM point

            mMol.p2[0] = (x2 + positionStart.x).toFloat()
            mMol.p2[1] = (y2 + positionStart.y).toFloat()
            mMol.p2[2] = (z2 + positionStart.z).toFloat()


            // SECOND top point

            x2 = mMol.R.x * c2 + mMol.S.x * s2
            y2 = mMol.R.y * c2 + mMol.S.y * s2
            z2 = mMol.R.z * c2 + mMol.S.z * s2

            mMol.p3[0] = (x2 + positionMid.x).toFloat()
            mMol.p3[1] = (y2 + positionMid.y).toFloat()
            mMol.p3[2] = (z2 + positionMid.z).toFloat()



            n = XYZ.getNormal(mMol.p1, mMol.p2, mMol.p3)
            putTri(n, startColor)

            /*
             * now the ending 1/2 of the bond - from MID to END
             */

            // first top point

            x1 = mMol.R.x * c1 + mMol.S.x * s1
            y1 = mMol.R.y * c1 + mMol.S.y * s1
            z1 = mMol.R.z * c1 + mMol.S.z * s1

            mMol.p1[0] = (x1 + positionEnd.x).toFloat()
            mMol.p1[1] = (y1 + positionEnd.y).toFloat()
            mMol.p1[2] = (z1 + positionEnd.z).toFloat()


            // first bottom point

            mMol.p2[0] = (x1 + positionMid.x).toFloat()
            mMol.p2[1] = (y1 + positionMid.y).toFloat()
            mMol.p2[2] = (z1 + positionMid.z).toFloat()


            // SECOND BOTTOM point
            x2 = mMol.R.x * c2 + mMol.S.x * s2
            y2 = mMol.R.y * c2 + mMol.S.y * s2
            z2 = mMol.R.z * c2 + mMol.S.z * s2

            mMol.p3[0] = (x2 + positionMid.x).toFloat()
            mMol.p3[1] = (y2 + positionMid.y).toFloat()
            mMol.p3[2] = (z2 + positionMid.z).toFloat()
            // OK that is one triangle.

            n = XYZ.getNormal(mMol.p1, mMol.p2, mMol.p3)
            putTri(n, endColor)

            // SECOND triangle NOW

            // first top point

            mMol.p1[0] = (x1 + positionEnd.x).toFloat()
            mMol.p1[1] = (y1 + positionEnd.y).toFloat()
            mMol.p1[2] = (z1 + positionEnd.z).toFloat()


            // SECOND BOTTOM point

            mMol.p2[0] = (x2 + positionMid.x).toFloat()
            mMol.p2[1] = (y2 + positionMid.y).toFloat()
            mMol.p2[2] = (z2 + positionMid.z).toFloat()

            // SECOND top point

            x2 = mMol.R.x * c2 + mMol.S.x * s2
            y2 = mMol.R.y * c2 + mMol.S.y * s2
            z2 = mMol.R.z * c2 + mMol.S.z * s2

            mMol.p3[0] = (x2 + positionEnd.x).toFloat()
            mMol.p3[1] = (y2 + positionEnd.y).toFloat()
            mMol.p3[2] = (z2 + positionEnd.z).toFloat()



            n = XYZ.getNormal(mMol.p1, mMol.p2, mMol.p3)
            putTri(n, endColor)
            i++

        }  // end for loop for body

        //mBufMgr.setFloatArrayIndex(mOffset);
        mBufMgr.floatArrayIndex = mOffset
    }

    private fun putTri(n: FloatArray, color: FloatArray) {

        n[0] *= normal_brightness_factor
        n[1] *= normal_brightness_factor
        n[2] *= normal_brightness_factor

        mVertexData[mOffset++] = mMol.p1[0]
        mVertexData[mOffset++] = mMol.p1[1]
        mVertexData[mOffset++] = mMol.p1[2]
        mVertexData[mOffset++] = n[0]
        mVertexData[mOffset++] = n[1]
        mVertexData[mOffset++] = n[2]
        mVertexData[mOffset++] = color[0]
        mVertexData[mOffset++] = color[1]
        mVertexData[mOffset++] = color[2]
        mVertexData[mOffset++] = color[3]

        mVertexData[mOffset++] = mMol.p2[0]
        mVertexData[mOffset++] = mMol.p2[1]
        mVertexData[mOffset++] = mMol.p2[2]
        mVertexData[mOffset++] = n[0]
        mVertexData[mOffset++] = n[1]
        mVertexData[mOffset++] = n[2]
        mVertexData[mOffset++] = color[0]
        mVertexData[mOffset++] = color[1]
        mVertexData[mOffset++] = color[2]
        mVertexData[mOffset++] = color[3]

        mVertexData[mOffset++] = mMol.p3[0]
        mVertexData[mOffset++] = mMol.p3[1]
        mVertexData[mOffset++] = mMol.p3[2]
        mVertexData[mOffset++] = n[0]
        mVertexData[mOffset++] = n[1]
        mVertexData[mOffset++] = n[2]
        mVertexData[mOffset++] = color[0]
        mVertexData[mOffset++] = color[1]
        mVertexData[mOffset++] = color[2]
        mVertexData[mOffset++] = color[3]
    }

    companion object {
        private const val POSITION_DATA_SIZE_IN_ELEMENTS = 3
        private const val NORMAL_DATA_SIZE_IN_ELEMENTS = 3
        private const val COLOR_DATA_SIZE_IN_ELEMENTS = 4

        private const val BYTES_PER_FLOAT = 4
        private const val BYTES_PER_SHORT = 2

        private const val STRIDE_IN_FLOATS = POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS + COLOR_DATA_SIZE_IN_ELEMENTS
        private const val STRIDE_IN_BYTES = STRIDE_IN_FLOATS * BYTES_PER_FLOAT

        private var normal_brightness_factor = 7f
    }
}


/*
         * DEBUG:
         * optional vertex printout
         */
//        float nvx, nvy, nvz;
//
//        for (i = 0; i < offset; i += STRIDE_IN_FLOATS) {
//            vx = vertexData[i + 0];
//            vy = vertexData[i + 1];
//            vz = vertexData[i + 2];
//            String svx = String.format("%6.2f", vx);
//            String svy = String.format("%6.2f", vy);
//            String svz = String.format("%6.2f", vz);
//
//            nvx = vertexData[i + 3];
//            nvy = vertexData[i + 4];
//            nvz = vertexData[i + 5];
//            String snvx = String.format("%6.2f", nvx);
//            String snvy = String.format("%6.2f", nvy);
//            String snvz = String.format("%6.2f", nvz);
//
//            Log.w("vert ", i + " x y z nx ny nz "
//                            + svx + " " + svy + " " + svz + " and " + snvx + " " + snvy + " " + snvz
////                    + " clr "
////                    + vertexData[i + 6] + " " + vertexData[i + 7] + " "
////                    + vertexData[i + 8] + " " + vertexData[i + 8]
//            );
//        }


//        FloatBuffer vertexDataBuffer = ByteBuffer
//                .allocateDirect(offset /* intead of the buffer length */ * BYTES_PER_FLOAT)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer();
//        // public FloatBuffer put(float[] src, int srcOffset, int floatCount) {
//        vertexDataBuffer
//                .put(vertexData, 0, offset)
//                .position(0);
//
//        GLES20.glGenBuffers(1, vboBody, 0);
//
//        if (vboBody[0] > 0) {
//            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboBody[0]);
//            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexDataBuffer.capacity() * BYTES_PER_FLOAT,
//                    vertexDataBuffer, GLES20.GL_STATIC_DRAW);
//
//            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
//        } else {
//            // errorHandler.handleError(ErrorHandler.ErrorType.BUFFER_CREATION_ERROR, "glGenBuffers");
//            throw new RuntimeException("error on buffer gen");
//        }
//        vertexDataBuffer.limit(0);