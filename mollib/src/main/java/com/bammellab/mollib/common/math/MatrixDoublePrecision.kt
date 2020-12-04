/*
 * Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2018 Jim Andreas kotlin conversion
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
 * limitations under the License.
 */

@file:Suppress("unused", "SameParameterValue")

package com.bammellab.mollib.common.math

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * NOTE: This class taken from Android Open Source Project source code
 * and modified to support double precision matrices.
 *
 * Matrix math utilities. These methods operate on OpenGL ES format
 * matrices and vectors stored in double arrays.
 *
 * Matrices are 4 x 4 column-vector matrices stored in column-major
 * order:
 * <pre>
 * m[offset +  0] m[offset +  4] m[offset +  8] m[offset + 12]
 * m[offset +  1] m[offset +  5] m[offset +  9] m[offset + 13]
 * m[offset +  2] m[offset +  6] m[offset + 10] m[offset + 14]
 * m[offset +  3] m[offset +  7] m[offset + 11] m[offset + 15]
</pre> *
 *
 * Vectors are 4 row x 1 column column-vectors stored in order:
 * <pre>
 * v[offset + 0]
 * v[offset + 1]
 * v[offset + 2]
 * v[offset + 3]
</pre> *
 *
 */
object MatrixDoublePrecision {

    /** Temporary memory for operations that need temporary matrix data.  */
    private val sTemp = FloatArray(32)

    /**
     * Multiply two 4x4 matrices together and store the result in a third 4x4
     * matrix. In matrix notation: result = lhs x rhs. Due to the way
     * matrix multiplication works, the result matrix will have the same
     * effect as first multiplying by the rhs matrix, then multiplying by
     * the lhs matrix. This is the opposite of what you might expect.
     *
     * The same double array may be passed for result, lhs, and/or rhs. However,
     * the result element values are undefined if the result elements overlap
     * either the lhs or rhs elements.
     *
     * @param result The double array that holds the result.
     * @param resultOffset The offset into the result array where the result is
     * stored.
     * @param lhs The double array that holds the left-hand-side matrix.
     * @param lhsOffset The offset into the lhs array where the lhs is stored
     * @param rhs The double array that holds the right-hand-side matrix.
     * @param rhsOffset The offset into the rhs array where the rhs is stored.
     *
     * @throws IllegalArgumentException if result, lhs, or rhs are null, or if
     * resultOffset + 16 > result.length or lhsOffset + 16 > lhs.length or
     * rhsOffset + 16 > rhs.length.
     */
    fun multiplyMM(result: FloatArray, resultOffset: Int,
                   lhs: FloatArray, lhsOffset: Int, rhs: FloatArray, rhsOffset: Int) {

        var message: String? = null//Column

        when {
            resultOffset + 16 > result.size -> message = "Specified result offset would overflow the passed result matrix."
            lhsOffset + 16 > lhs.size -> message = "Specified left hand side offset would overflow the passed lhs matrix."
            rhsOffset + 16 > rhs.size -> message = "Specified right hand side offset would overflow the passed rhs matrix."
        }
        if (message != null) {
            throw IllegalArgumentException(message)
        }

        var sum: Float
        for (i in 0..3) { //Row
            for (j in 0..3) { //Column
                sum = 0.0f
                for (k in 0..3) {
                    sum += lhs[i + 4 * k + lhsOffset] * rhs[4 * j + k + rhsOffset]
                }
                result[i + 4 * j + resultOffset] = sum
            }
        }
    }

    /**
     * Multiply a 4 element vector by a 4x4 matrix and store the result in a 4
     * element column vector. In matrix notation: result = lhs x rhs
     *
     * The same double array may be passed for resultVec, lhsMat, and/or rhsVec.
     * However, the resultVec element values are undefined if the resultVec
     * elements overlap either the lhsMat or rhsVec elements.
     *
     * @param resultVec The double array that holds the result vector.
     * @param resultVecOffset The offset into the result array where the result
     * vector is stored.
     * @param lhsMat The double array that holds the left-hand-side matrix.
     * @param lhsMatOffset The offset into the lhs array where the lhs is stored
     * @param rhsVec The double array that holds the right-hand-side vector.
     * @param rhsVecOffset The offset into the rhs vector where the rhs vector
     * is stored.
     *
     * @throws IllegalArgumentException if resultVec, lhsMat,
     * or rhsVec are null, or if resultVecOffset + 4 > resultVec.length
     * or lhsMatOffset + 16 > lhsMat.length or
     * rhsVecOffset + 4 > rhsVec.length.
     */
    fun multiplyMV(resultVec: FloatArray, resultVecOffset: Int,
                   lhsMat: FloatArray, lhsMatOffset: Int, rhsVec: FloatArray, rhsVecOffset: Int) {

        var message: String? = null

        when {
            resultVecOffset + 4 > resultVec.size -> message = "Specified result offset would overflow the passed result vector."
            lhsMatOffset + 16 > lhsMat.size -> message = "Specified left hand side offset would overflow the passed lhs matrix."
            rhsVecOffset + 4 > rhsVec.size -> message = "Specified right hand side offset would overflow the passed rhs vector."
        }
        if (message != null) {
            throw IllegalArgumentException(message)
        }

        for (i in 0..3) { //Row
            var sum = 0.0f
            for (k in 0..3) {
                sum += lhsMat[i + 4 * k + lhsMatOffset] * rhsVec[k + rhsVecOffset]
            }
            resultVec[i + resultVecOffset] = sum
        }
    }

    /**
     * Transposes a 4 x 4 matrix.
     *
     * @param mTrans the array that holds the output inverted matrix
     * @param transOffset an offset into mInv where the inverted matrix is
     * stored.
     * @param m the input array
     * @param mOffset an offset into m where the matrix is stored.
     */
    fun transposeM(mTrans: FloatArray, transOffset: Int, m: FloatArray,
                   mOffset: Int) {
        for (i in 0..3) {
            val base = i * 4 + mOffset
            mTrans[i + transOffset] = m[base]
            mTrans[i + 4 + transOffset] = m[base + 1]
            mTrans[i + 8 + transOffset] = m[base + 2]
            mTrans[i + 12 + transOffset] = m[base + 3]
        }
    }

    /**
     * Inverts a 4 x 4 matrix.
     *
     * @param mInv the array that holds the output inverted matrix
     * @param invOffset an offset into mInv where the inverted matrix is
     * stored.
     * @param m the input array
     * @param mOffset an offset into m where the matrix is stored.
     * @return true if the matrix could be inverted, false if it could not.
     */
    fun invertM(mInv: FloatArray, invOffset: Int, m: FloatArray,
                mOffset: Int): Boolean {
        // Invert a 4 x 4 matrix using Cramer's Rule

        // transpose matrix
        val src0 = m[mOffset + 0]
        val src4 = m[mOffset + 1]
        val src8 = m[mOffset + 2]
        val src12 = m[mOffset + 3]

        val src1 = m[mOffset + 4]
        val src5 = m[mOffset + 5]
        val src9 = m[mOffset + 6]
        val src13 = m[mOffset + 7]

        val src2 = m[mOffset + 8]
        val src6 = m[mOffset + 9]
        val src10 = m[mOffset + 10]
        val src14 = m[mOffset + 11]

        val src3 = m[mOffset + 12]
        val src7 = m[mOffset + 13]
        val src11 = m[mOffset + 14]
        val src15 = m[mOffset + 15]

        // calculate pairs for first 8 elements (cofactors)
        val atmp0 = src10 * src15
        val atmp1 = src11 * src14
        val atmp2 = src9 * src15
        val atmp3 = src11 * src13
        val atmp4 = src9 * src14
        val atmp5 = src10 * src13
        val atmp6 = src8 * src15
        val atmp7 = src11 * src12
        val atmp8 = src8 * src14
        val atmp9 = src10 * src12
        val atmp10 = src8 * src13
        val atmp11 = src9 * src12

        // calculate first 8 elements (cofactors)
        val dst0 = atmp0 * src5 + atmp3 * src6 + atmp4 * src7 - (atmp1 * src5 + atmp2 * src6 + atmp5 * src7)
        val dst1 = atmp1 * src4 + atmp6 * src6 + atmp9 * src7 - (atmp0 * src4 + atmp7 * src6 + atmp8 * src7)
        val dst2 = atmp2 * src4 + atmp7 * src5 + atmp10 * src7 - (atmp3 * src4 + atmp6 * src5 + atmp11 * src7)
        val dst3 = atmp5 * src4 + atmp8 * src5 + atmp11 * src6 - (atmp4 * src4 + atmp9 * src5 + atmp10 * src6)
        val dst4 = atmp1 * src1 + atmp2 * src2 + atmp5 * src3 - (atmp0 * src1 + atmp3 * src2 + atmp4 * src3)
        val dst5 = atmp0 * src0 + atmp7 * src2 + atmp8 * src3 - (atmp1 * src0 + atmp6 * src2 + atmp9 * src3)
        val dst6 = atmp3 * src0 + atmp6 * src1 + atmp11 * src3 - (atmp2 * src0 + atmp7 * src1 + atmp10 * src3)
        val dst7 = atmp4 * src0 + atmp9 * src1 + atmp10 * src2 - (atmp5 * src0 + atmp8 * src1 + atmp11 * src2)

        // calculate pairs for second 8 elements (cofactors)
        val btmp0 = src2 * src7
        val btmp1 = src3 * src6
        val btmp2 = src1 * src7
        val btmp3 = src3 * src5
        val btmp4 = src1 * src6
        val btmp5 = src2 * src5
        val btmp6 = src0 * src7
        val btmp7 = src3 * src4
        val btmp8 = src0 * src6
        val btmp9 = src2 * src4
        val btmp10 = src0 * src5
        val btmp11 = src1 * src4

        // calculate second 8 elements (cofactors)
        val dst8 = btmp0 * src13 + btmp3 * src14 + btmp4 * src15 - (btmp1 * src13 + btmp2 * src14 + btmp5 * src15)
        val dst9 = btmp1 * src12 + btmp6 * src14 + btmp9 * src15 - (btmp0 * src12 + btmp7 * src14 + btmp8 * src15)
        val dst10 = btmp2 * src12 + btmp7 * src13 + btmp10 * src15 - (btmp3 * src12 + btmp6 * src13 + btmp11 * src15)
        val dst11 = btmp5 * src12 + btmp8 * src13 + btmp11 * src14 - (btmp4 * src12 + btmp9 * src13 + btmp10 * src14)
        val dst12 = btmp2 * src10 + btmp5 * src11 + btmp1 * src9 - (btmp4 * src11 + btmp0 * src9 + btmp3 * src10)
        val dst13 = btmp8 * src11 + btmp0 * src8 + btmp7 * src10 - (btmp6 * src10 + btmp9 * src11 + btmp1 * src8)
        val dst14 = btmp6 * src9 + btmp11 * src11 + btmp3 * src8 - (btmp10 * src11 + btmp2 * src8 + btmp7 * src9)
        val dst15 = btmp10 * src10 + btmp4 * src8 + btmp9 * src9 - (btmp8 * src9 + btmp11 * src10 + btmp5 * src8)

        // calculate determinant
        val det = src0 * dst0 + src1 * dst1 + src2 * dst2 + src3 * dst3

        if (det == 0.0f) {
            return false
        }

        // calculate matrix inverse
        val invdet = 1.0f / det
        mInv[invOffset] = dst0 * invdet
        mInv[1 + invOffset] = dst1 * invdet
        mInv[2 + invOffset] = dst2 * invdet
        mInv[3 + invOffset] = dst3 * invdet

        mInv[4 + invOffset] = dst4 * invdet
        mInv[5 + invOffset] = dst5 * invdet
        mInv[6 + invOffset] = dst6 * invdet
        mInv[7 + invOffset] = dst7 * invdet

        mInv[8 + invOffset] = dst8 * invdet
        mInv[9 + invOffset] = dst9 * invdet
        mInv[10 + invOffset] = dst10 * invdet
        mInv[11 + invOffset] = dst11 * invdet

        mInv[12 + invOffset] = dst12 * invdet
        mInv[13 + invOffset] = dst13 * invdet
        mInv[14 + invOffset] = dst14 * invdet
        mInv[15 + invOffset] = dst15 * invdet

        return true
    }

    /*
     * Computes an orthographic projection matrix.
     *
     */
    fun orthoM(m: FloatArray, mOffset: Int,
               left: Float, right: Float, bottom: Float, top: Float,
               near: Float, far: Float) {
        if (left == right) {
            throw IllegalArgumentException("left == right")
        }
        if (bottom == top) {
            throw IllegalArgumentException("bottom == top")
        }
        if (near == far) {
            throw IllegalArgumentException("near == far")
        }

        val theWidth = 1.0f / (right - left)
        val theHeight = 1.0f / (top - bottom)
        val theDepth = 1.0f / (far - near)
        val x = 2.0f * theWidth
        val y = 2.0f * theHeight
        val z = -2.0f * theDepth
        val tx = -(right + left) * theWidth
        val ty = -(top + bottom) * theHeight
        val tz = -(far + near) * theDepth
        m[mOffset + 0] = x
        m[mOffset + 5] = y
        m[mOffset + 10] = z
        m[mOffset + 12] = tx
        m[mOffset + 13] = ty
        m[mOffset + 14] = tz
        m[mOffset + 15] = 1.0f
        m[mOffset + 1] = 0.0f
        m[mOffset + 2] = 0.0f
        m[mOffset + 3] = 0.0f
        m[mOffset + 4] = 0.0f
        m[mOffset + 6] = 0.0f
        m[mOffset + 7] = 0.0f
        m[mOffset + 8] = 0.0f
        m[mOffset + 9] = 0.0f
        m[mOffset + 11] = 0.0f
    }


    /**
     * Define a projection matrix in terms of six clip planes
     * @param m the double array that holds the perspective matrix
     * @param offset the offset into double array m where the perspective
     * matrix data is written
     */
    fun frustumM(m: FloatArray, offset: Int,
                 left: Float, right: Float, bottom: Float, top: Float,
                 near: Float, far: Float) {
        if (left == right) {
            throw IllegalArgumentException("left == right")
        }
        if (top == bottom) {
            throw IllegalArgumentException("top == bottom")
        }
        if (near == far) {
            throw IllegalArgumentException("near == far")
        }
        if (near <= 0.0f) {
            throw IllegalArgumentException("near <= 0.0")
        }
        if (far <= 0.0f) {
            throw IllegalArgumentException("far <= 0.0")
        }
        val theWidth = 1.0f / (right - left)
        val theHeight = 1.0f / (top - bottom)
        val theDepth = 1.0f / (near - far)
        val x = 2.0f * (near * theWidth)
        val y = 2.0f * (near * theHeight)
        val a = (right + left) * theWidth
        val b = (top + bottom) * theHeight
        val c = (far + near) * theDepth
        val d = 2.0f * (far * near * theDepth)
        m[offset + 0] = x
        m[offset + 5] = y
        m[offset + 8] = a
        m[offset + 9] = b
        m[offset + 10] = c
        m[offset + 14] = d
        m[offset + 11] = -1.0f
        m[offset + 1] = 0.0f
        m[offset + 2] = 0.0f
        m[offset + 3] = 0.0f
        m[offset + 4] = 0.0f
        m[offset + 6] = 0.0f
        m[offset + 7] = 0.0f
        m[offset + 12] = 0.0f
        m[offset + 13] = 0.0f
        m[offset + 15] = 0.0f
    }

    /*
     * Define a projection matrix in terms of a field of view angle, an
     * aspect ratio, and z clip planes
     * @param m the double array that holds the perspective matrix
     * @param offset the offset into double array m where the perspective
     * matrix data is written
     * @param fovy field of view in y direction, in degrees
     * @param aspect width to height aspect ratio of the viewport
     * @param zNear
     * @param zFar
     */
    fun perspectiveM(m: FloatArray, offset: Int,
                     fovy: Float, aspect: Float, zNear: Float, zFar: Float) {
        val eff = 1.0f / tan(fovy * (Math.PI / 360.0f).toFloat())
        val rangeReciprocal = 1.0f / (zNear - zFar)

        m[offset + 0] = eff / aspect
        m[offset + 1] = 0.0f
        m[offset + 2] = 0.0f
        m[offset + 3] = 0.0f

        m[offset + 4] = 0.0f
        m[offset + 5] = eff
        m[offset + 6] = 0.0f
        m[offset + 7] = 0.0f

        m[offset + 8] = 0.0f
        m[offset + 9] = 0.0f
        m[offset + 10] = (zFar + zNear) * rangeReciprocal
        m[offset + 11] = -1.0f

        m[offset + 12] = 0.0f
        m[offset + 13] = 0.0f
        m[offset + 14] = 2.0f * zFar * zNear * rangeReciprocal
        m[offset + 15] = 0.0f
    }

    /**
     * Computes the length of a vector
     *
     * @param x x coordinate of a vector
     * @param y y coordinate of a vector
     * @param z z coordinate of a vector
     * @return the length of a vector
     */
    fun length(x: Float, y: Float, z: Float): Float {
        return sqrt(x * x + y * y + z * z)
    }

    /**
     * Sets matrix m to the identity matrix.
     * @param sm returns the result
     * @param smOffset index into sm where the result matrix starts
     */
    fun setIdentityM(sm: FloatArray, smOffset: Int) {
        for (i in 0..15) {
            sm[smOffset + i] = 0.0f
        }
        var i = 0
        while (i < 16) {
            sm[smOffset + i] = 1.0f
            i += 5
        }
    }

    /**
     * Scales matrix  m by x, y, and z, putting the result in sm
     * @param sm returns the result
     * @param smOffset index into sm where the result matrix starts
     * @param m source matrix
     * @param mOffset index into m where the source matrix starts
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    fun scaleM(sm: FloatArray, smOffset: Int,
               m: FloatArray, mOffset: Int,
               x: Float, y: Float, z: Float) {
        for (i in 0..3) {
            val smi = smOffset + i
            val mi = mOffset + i
            sm[smi] = m[mi] * x
            sm[4 + smi] = m[4 + mi] * y
            sm[8 + smi] = m[8 + mi] * z
            sm[12 + smi] = m[12 + mi]
        }
    }

    /**
     * Scales matrix m in place by sx, sy, and sz
     * @param m matrix to scale
     * @param mOffset index into m where the matrix starts
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    fun scaleM(m: FloatArray, mOffset: Int,
               x: Float, y: Float, z: Float) {
        for (i in 0..3) {
            val mi = mOffset + i
            m[mi] *= x
            m[4 + mi] *= y
            m[8 + mi] *= z
        }
    }

    /**
     * Translates matrix m by x, y, and z, putting the result in tm
     * @param tm returns the result
     * @param tmOffset index into sm where the result matrix starts
     * @param m source matrix
     * @param mOffset index into m where the source matrix starts
     * @param x translation factor x
     * @param y translation factor y
     * @param z translation factor z
     */
    fun translateM(tm: FloatArray, tmOffset: Int,
                   m: FloatArray, mOffset: Int,
                   x: Float, y: Float, z: Float) {
        for (i in 0..11) {
            tm[tmOffset + i] = m[mOffset + i]
        }
        for (i in 0..3) {
            val tmi = tmOffset + i
            val mi = mOffset + i
            tm[12 + tmi] = m[mi] * x + m[4 + mi] * y + m[8 + mi] * z +
                    m[12 + mi]
        }
    }

    /**
     * Translates matrix m by x, y, and z in place.
     * @param m matrix
     * @param mOffset index into m where the matrix starts
     * @param x translation factor x
     * @param y translation factor y
     * @param z translation factor z
     */
    private fun translateM(
            m: FloatArray, mOffset: Int,
            x: Float, y: Float, z: Float) {
        for (i in 0..3) {
            val mi = mOffset + i
            m[12 + mi] += m[mi] * x + m[4 + mi] * y + m[8 + mi] * z
        }
    }

    /**
     * Rotates matrix m by angle a (in degrees) around the axis (x, y, z)
     * @param rm returns the result
     * @param rmOffset index into rm where the result matrix starts
     * @param m source matrix
     * @param mOffset index into m where the source matrix starts
     * @param a angle to rotate in degrees
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    fun rotateM(rm: FloatArray, rmOffset: Int,
                m: FloatArray, mOffset: Int,
                a: Float, x: Float, y: Float, z: Float) {
        synchronized(sTemp) {
            setRotateM(sTemp, 0, a, x, y, z)
            multiplyMM(rm, rmOffset, m, mOffset, sTemp, 0)
        }
    }

    /**
     * Rotates matrix m in place by angle a (in degrees)
     * around the axis (x, y, z)
     * @param m source matrix
     * @param mOffset index into m where the matrix starts
     * @param a angle to rotate in degrees
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    fun rotateM(m: FloatArray, mOffset: Int,
                a: Float, x: Float, y: Float, z: Float) {
        synchronized(sTemp) {
            setRotateM(sTemp, 0, a, x, y, z)
            multiplyMM(sTemp, 16, m, mOffset, sTemp, 0)
            System.arraycopy(sTemp, 16, m, mOffset, 16)
        }
    }

    /**
     * Rotates matrix m by angle a (in degrees) around the axis (x, y, z)
     * @param rm returns the result
     * @param rmOffset index into rm where the result matrix starts
     * @param aIn angle to rotate in degrees
     * @param xIn scale factor x
     * @param yIn scale factor y
     * @param zIn scale factor z
     */
    private fun setRotateM(rm: FloatArray, rmOffset: Int,
                           aIn: Float, xIn: Float, yIn: Float, zIn: Float) {
        var a = aIn
        var x = xIn
        var y = yIn
        var z = zIn
        rm[rmOffset + 3] = 0.0f
        rm[rmOffset + 7] = 0.0f
        rm[rmOffset + 11] = 0.0f
        rm[rmOffset + 12] = 0.0f
        rm[rmOffset + 13] = 0.0f
        rm[rmOffset + 14] = 0.0f
        rm[rmOffset + 15] = 1.0f
        a *= Math.PI.toFloat() / 180.0f
        val s = sin(a)
        val c = cos(a)
        if (1.0f == x && 0.0f == y && 0.0f == z) {
            rm[rmOffset + 5] = c
            rm[rmOffset + 10] = c
            rm[rmOffset + 6] = s
            rm[rmOffset + 9] = -s
            rm[rmOffset + 1] = 0.0f
            rm[rmOffset + 2] = 0.0f
            rm[rmOffset + 4] = 0.0f
            rm[rmOffset + 8] = 0.0f
            rm[rmOffset + 0] = 1.0f
        } else if (0.0f == x && 1.0f == y && 0.0f == z) {
            rm[rmOffset + 0] = c
            rm[rmOffset + 10] = c
            rm[rmOffset + 8] = s
            rm[rmOffset + 2] = -s
            rm[rmOffset + 1] = 0.0f
            rm[rmOffset + 4] = 0.0f
            rm[rmOffset + 6] = 0.0f
            rm[rmOffset + 9] = 0.0f
            rm[rmOffset + 5] = 1.0f
        } else if (0.0f == x && 0.0f == y && 1.0f == z) {
            rm[rmOffset + 0] = c
            rm[rmOffset + 5] = c
            rm[rmOffset + 1] = s
            rm[rmOffset + 4] = -s
            rm[rmOffset + 2] = 0.0f
            rm[rmOffset + 6] = 0.0f
            rm[rmOffset + 8] = 0.0f
            rm[rmOffset + 9] = 0.0f
            rm[rmOffset + 10] = 1.0f
        } else {
            val len = length(x, y, z)
            if (1.0f != len) {
                val recipLen = 1.0f / len
                x *= recipLen
                y *= recipLen
                z *= recipLen
            }
            val nc = 1.0f - c
            val xy = x * y
            val yz = y * z
            val zx = z * x
            val xs = x * s
            val ys = y * s
            val zs = z * s
            rm[rmOffset + 0] = x * x * nc + c
            rm[rmOffset + 4] = xy * nc - zs
            rm[rmOffset + 8] = zx * nc + ys
            rm[rmOffset + 1] = xy * nc + zs
            rm[rmOffset + 5] = y * y * nc + c
            rm[rmOffset + 9] = yz * nc - xs
            rm[rmOffset + 2] = zx * nc - ys
            rm[rmOffset + 6] = yz * nc + xs
            rm[rmOffset + 10] = z * z * nc + c
        }
    }

    /**
     * Converts Euler angles to a rotation matrix
     * @param rm returns the result
     * @param rmOffset index into rm where the result matrix starts
     * @param xIn angle of rotation, in degrees
     * @param yIn angle of rotation, in degrees
     * @param zIn angle of rotation, in degrees
     */
    fun setRotateEulerM(rm: FloatArray, rmOffset: Int,
                        xIn: Float, yIn: Float, zIn: Float) {
        var x = xIn
        var y = yIn
        var z = zIn
        x *= Math.PI.toFloat() / 180.0f
        y *= Math.PI.toFloat() / 180.0f
        z *= Math.PI.toFloat() / 180.0f
        val cx = cos(x)
        val sx = sin(x)
        val cy = cos(y)
        val sy = sin(y)
        val cz = cos(z)
        val sz = sin(z)
        val cxsy = cx * sy
        val sxsy = sx * sy

        rm[rmOffset + 0] = cy * cz
        rm[rmOffset + 1] = -cy * sz
        rm[rmOffset + 2] = sy
        rm[rmOffset + 3] = 0.0f

        rm[rmOffset + 4] = cxsy * cz + cx * sz
        rm[rmOffset + 5] = -cxsy * sz + cx * cz
        rm[rmOffset + 6] = -sx * cy
        rm[rmOffset + 7] = 0.0f

        rm[rmOffset + 8] = -sxsy * cz + sx * sz
        rm[rmOffset + 9] = sxsy * sz + sx * cz
        rm[rmOffset + 10] = cx * cy
        rm[rmOffset + 11] = 0.0f

        rm[rmOffset + 12] = 0.0f
        rm[rmOffset + 13] = 0.0f
        rm[rmOffset + 14] = 0.0f
        rm[rmOffset + 15] = 1.0f
    }

    /**
     * Define a viewing transformation in terms of an eye point, a center of
     * view, and an up vector.
     *
     * @param rm returns the result
     * @param rmOffset index into rm where the result matrix starts
     * @param eyeX eye point X
     * @param eyeY eye point Y
     * @param eyeZ eye point Z
     * @param centerX center of view X
     * @param centerY center of view Y
     * @param centerZ center of view Z
     * @param upX up vector X
     * @param upY up vector Y
     * @param upZ up vector Z
     */
    fun setLookAtM(rm: FloatArray, rmOffset: Int,
                   eyeX: Float, eyeY: Float, eyeZ: Float,
                   centerX: Float, centerY: Float, centerZ: Float, upX: Float, upY: Float,
                   upZ: Float) {

        // See the OpenGL GLUT documentation for gluLookAt for a description
        // of the algorithm. We implement it in a straightforward way:

        var fx = centerX - eyeX
        var fy = centerY - eyeY
        var fz = centerZ - eyeZ

        // Normalize f
        val rlf = 1.0f / length(fx, fy, fz)
        fx *= rlf
        fy *= rlf
        fz *= rlf

        // compute s = f x up (x means "cross product")
        var sx = fy * upZ - fz * upY
        var sy = fz * upX - fx * upZ
        var sz = fx * upY - fy * upX

        // and normalize s
        val rls = 1.0f / length(sx, sy, sz)
        sx *= rls
        sy *= rls
        sz *= rls

        // compute u = s x f
        val ux = sy * fz - sz * fy
        val uy = sz * fx - sx * fz
        val uz = sx * fy - sy * fx

        rm[rmOffset + 0] = sx
        rm[rmOffset + 1] = ux
        rm[rmOffset + 2] = -fx
        rm[rmOffset + 3] = 0.0f

        rm[rmOffset + 4] = sy
        rm[rmOffset + 5] = uy
        rm[rmOffset + 6] = -fy
        rm[rmOffset + 7] = 0.0f

        rm[rmOffset + 8] = sz
        rm[rmOffset + 9] = uz
        rm[rmOffset + 10] = -fz
        rm[rmOffset + 11] = 0.0f

        rm[rmOffset + 12] = 0.0f
        rm[rmOffset + 13] = 0.0f
        rm[rmOffset + 14] = 0.0f
        rm[rmOffset + 15] = 1.0f

        translateM(rm, rmOffset, -eyeX, -eyeY, -eyeZ)
    }
}

