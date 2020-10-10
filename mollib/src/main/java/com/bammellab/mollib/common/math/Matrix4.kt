/*
 * Copyright 2013 Dennis Ippel
 * Copyright 2018 Jim Andreas - Kotlin conversion
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
@file:Suppress("unused")
package com.bammellab.mollib.common.math

import kotlin.math.sqrt

/**
 * Encapsulates a column major 4x4 Matrix.
 *
 * This class is not thread safe and must be confined to a single thread or by
 * some external locking mechanism if necessary. All static methods are thread safe.
 *
 * Rewritten August 8, 2013 by Jared Woolston (jwoolston@tenkiv.com) with heavy influence from libGDX
 * @see [
 * https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Matrix4.java](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Matrix4.java)
 *
 *
 * @author dennis.ippel
 * @author Jared Woolston (jwoolston@tenkiv.com)
 */
class Matrix4 {

    /**
     * Returns the backing array of this [Matrix4].
     *
     * @return double array containing the backing array. The returned array is owned
     * by this [Matrix4] and is subject to change as the implementation sees fit.
     */
    val doubleValues = DoubleArray(16) //The matrix values

    //The following scratch variables are intentionally left as members
    //and not static to ensure that this class can be utilized by multiple threads
    //in a safe manner without the overhead of synchronization. This is a tradeoff of
    //speed for memory and it is considered a small enough memory increase to be acceptable.
    private val tmp = DoubleArray(16) //A scratch matrix
    private val mFloat = FloatArray(16) //A float copy of the values, used for sending to GL.
    private val mQuat = Quaternion() //A scratch quaternion.
    private val vec1 = MotmVector3() //A scratch MotmVector3
    private val vec2 = MotmVector3() //A scratch MotmVector3
    private val vec3 = MotmVector3() //A scratch MotmVector3
    private var mMatrix: Matrix4? = null //A scratch Matrix4


    //--------------------------------------------------
    // Component fetch methods
    //--------------------------------------------------

    /**
     * Creates a new [MotmVector3] representing the translation component
     * of this [Matrix4].
     *
     * @return [MotmVector3] representing the translation.
     */
    val translation: MotmVector3
        get() = getTranslation(MotmVector3())

    /**
     * Creates a new [MotmVector3] representing the scaling component
     * of this [Matrix4].
     *
     * @return [MotmVector3] representing the scaling.
     */
    val scaling: MotmVector3
        get() {
            val x = sqrt(doubleValues[M00] * doubleValues[M00] + doubleValues[M01] * doubleValues[M01] + doubleValues[M02] * doubleValues[M02])
            val y = sqrt(doubleValues[M10] * doubleValues[M10] + doubleValues[M11] * doubleValues[M11] + doubleValues[M12] * doubleValues[M12])
            val z = sqrt(doubleValues[M20] * doubleValues[M20] + doubleValues[M21] * doubleValues[M21] + doubleValues[M22] * doubleValues[M22])
            return MotmVector3(x, y, z)
        }


    //--------------------------------------------------
    // Utility methods
    //--------------------------------------------------

    /**
     * Copies the backing array of this [Matrix4] into a float array and returns it.
     *
     * @return float array containing a copy of the backing array. The returned array is owned
     * by this [Matrix4] and is subject to change as the implementation sees fit.
     */
    val floatValues: FloatArray
        get() {
            ArrayUtils.convertDoublesToFloats(doubleValues, mFloat)
            return mFloat
        }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructs a default identity [Matrix4].
     */
    constructor() {
        identity()
    }

    /**
     * Constructs a new [Matrix4] based on the given matrix.
     *
     * @param matrix [Matrix4] The matrix to clone.
     */
    constructor(matrix: Matrix4) {
        setAll(matrix)
    }

    /**
     * Constructs a new [Matrix4] based on the provided double array. The array length
     * must be greater than or equal to 16 and the array will be copied from the 0 index.
     *
     * @param matrix double array containing the values for the matrix in column major order.
     * The array is not modified or referenced after this constructor completes.
     */
    constructor(matrix: DoubleArray) {
        setAll(matrix)
    }

    /**
     * Constructs a new [Matrix4] based on the provided float array. The array length
     * must be greater than or equal to 16 and the array will be copied from the 0 index.
     *
     * @param matrix float array containing the values for the matrix in column major order.
     * The array is not modified or referenced after this constructor completes.
     */
    constructor(matrix: FloatArray) : this(ArrayUtils.convertFloatsToDoubles(matrix))

    /**
     * Constructs a [Matrix4] based on the rotation represented by the provided [Quaternion].
     *
     * @param quat [Quaternion] The [Quaternion] to be copied.
     */
    constructor(quat: Quaternion) {
        setAll(quat)
    }


    //--------------------------------------------------
    // Modification methods
    //--------------------------------------------------

    /**
     * Sets the elements of this [Matrix4] based on the elements of the provided [Matrix4].
     *
     * @param matrix [Matrix4] to copy.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    private fun setAll(matrix: Matrix4): Matrix4 {
        matrix.toArray(doubleValues)
        return this
    }

    /**
     * Sets the elements of this [Matrix4] based on the provided double array.
     * The array length must be greater than or equal to 16 and the array will be copied
     * from the 0 index.
     *
     * @param matrix double array containing the values for the matrix in column major order.
     * The array is not modified or referenced after this constructor completes.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    private fun setAll(matrix: DoubleArray): Matrix4 {
        System.arraycopy(matrix, 0, doubleValues, 0, 16)
        return this
    }

    fun setAll(matrix: FloatArray): Matrix4 {
        doubleValues[0] = matrix[0].toDouble()
        doubleValues[1] = matrix[1].toDouble()
        doubleValues[2] = matrix[2].toDouble()
        doubleValues[3] = matrix[3].toDouble()
        doubleValues[4] = matrix[4].toDouble()
        doubleValues[5] = matrix[5].toDouble()
        doubleValues[6] = matrix[6].toDouble()
        doubleValues[7] = matrix[7].toDouble()
        doubleValues[8] = matrix[8].toDouble()
        doubleValues[9] = matrix[9].toDouble()
        doubleValues[10] = matrix[10].toDouble()
        doubleValues[11] = matrix[11].toDouble()
        doubleValues[12] = matrix[12].toDouble()
        doubleValues[13] = matrix[13].toDouble()
        doubleValues[14] = matrix[14].toDouble()
        doubleValues[15] = matrix[15].toDouble()
        return this
    }

    /**
     * Sets the elements of this [Matrix4] based on the rotation represented by
     * the provided [Quaternion].
     *
     * @param quat [Quaternion] The [Quaternion] to represent.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    private fun setAll(quat: Quaternion): Matrix4 {
        quat.toRotationMatrix(doubleValues)
        return this
    }

    /**
     * Sets the elements of this [Matrix4] based on the rotation represented by
     * the provided quaternion elements.
     *
     * @param w double The w component of the quaternion.
     * @param x double The x component of the quaternion.
     * @param y double The y component of the quaternion.
     * @param z double The z component of the quaternion.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setAll(w: Double, x: Double, y: Double, z: Double): Matrix4 {
        return setAll(mQuat.setAll(w, x, y, z))
    }

    /**
     * Sets the four columns of this [Matrix4] which correspond to the x-, y-, and z-
     * axis of the vector space this [Matrix4] creates as well as the 4th column representing
     * the translation of any point that is multiplied by this [Matrix4].
     *
     * @param xAxis [MotmVector3] The x axis.
     * @param yAxis [MotmVector3] The y axis.
     * @param zAxis [MotmVector3] The z axis.
     * @param pos [MotmVector3] The translation vector.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    private fun setAll(xAxis: MotmVector3, yAxis: MotmVector3, zAxis: MotmVector3, pos: MotmVector3): Matrix4 {
        doubleValues[M00] = xAxis.x
        doubleValues[M01] = yAxis.x
        doubleValues[M02] = zAxis.x
        doubleValues[M03] = pos.x
        doubleValues[M10] = xAxis.y
        doubleValues[M11] = yAxis.y
        doubleValues[M12] = zAxis.y
        doubleValues[M13] = pos.y
        doubleValues[M20] = xAxis.z
        doubleValues[M21] = yAxis.z
        doubleValues[M22] = zAxis.z
        doubleValues[M23] = pos.z
        doubleValues[M30] = 0.0
        doubleValues[M31] = 0.0
        doubleValues[M32] = 0.0
        doubleValues[M33] = 1.0
        return this
    }

    /**
     * Sets the values of this [Matrix4] to the values corresponding to a Translation x Scale x Rotation.
     * This is useful for composing a model matrix as efficiently as possible, eliminating any extraneous calculations.
     *
     * @param position [MotmVector3] representing the translation.
     * @param scale [MotmVector3] representing the scaling.
     * @param rotation [Quaternion] representing the rotation.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setAll(position: MotmVector3, scale: MotmVector3, rotation: Quaternion): Matrix4 {
        // Precompute these factors for speed
        val x2 = rotation.x * rotation.x
        val y2 = rotation.y * rotation.y
        val z2 = rotation.z * rotation.z
        val xy = rotation.x * rotation.y
        val xz = rotation.x * rotation.z
        val yz = rotation.y * rotation.z
        val wx = rotation.w * rotation.x
        val wy = rotation.w * rotation.y
        val wz = rotation.w * rotation.z

        // Column 0
        doubleValues[M00] = scale.x * (1.0 - 2.0 * (y2 + z2))
        doubleValues[M10] = 2.0 * scale.y * (xy - wz)
        doubleValues[M20] = 2.0 * scale.z * (xz + wy)
        doubleValues[M30] = 0.0

        // Column 1
        doubleValues[M01] = 2.0 * scale.x * (xy + wz)
        doubleValues[M11] = scale.y * (1.0 - 2.0 * (x2 + z2))
        doubleValues[M21] = 2.0 * scale.z * (yz - wx)
        doubleValues[M31] = 0.0

        // Column 2
        doubleValues[M02] = 2.0 * scale.x * (xz - wy)
        doubleValues[M12] = 2.0 * scale.y * (yz + wx)
        doubleValues[M22] = scale.z * (1.0 - 2.0 * (x2 + y2))
        doubleValues[M32] = 0.0

        // Column 3
        doubleValues[M03] = position.x
        doubleValues[M13] = position.y
        doubleValues[M23] = position.z
        doubleValues[M33] = 1.0
        return this
    }

    /**
     * Sets this [Matrix4] to an identity matrix.
     *
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    private fun identity(): Matrix4 {
        doubleValues[M00] = 1.0
        doubleValues[M10] = 0.0
        doubleValues[M20] = 0.0
        doubleValues[M30] = 0.0
        doubleValues[M01] = 0.0
        doubleValues[M11] = 1.0
        doubleValues[M21] = 0.0
        doubleValues[M31] = 0.0
        doubleValues[M02] = 0.0
        doubleValues[M12] = 0.0
        doubleValues[M22] = 1.0
        doubleValues[M32] = 0.0
        doubleValues[M03] = 0.0
        doubleValues[M13] = 0.0
        doubleValues[M23] = 0.0
        doubleValues[M33] = 1.0
        return this
    }

    /**
     * Sets all elements of this [Matrix4] to zero.
     *
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun zero(): Matrix4 {
        for (i in 0..15) {
            doubleValues[i] = 0.0
        }
        return this
    }

    /**
     * Calculate the determinant of this [Matrix4].
     *
     * @return double The determinant.
     */
    fun determinant(): Double {
        return doubleValues[M30] * doubleValues[M21] * doubleValues[M12] * doubleValues[M03] -
                doubleValues[M20] * doubleValues[M31] * doubleValues[M12] * doubleValues[M03] -
                doubleValues[M30] * doubleValues[M11] * doubleValues[M22] * doubleValues[M03] +
                doubleValues[M10] * doubleValues[M31] * doubleValues[M22] * doubleValues[M03] +

                doubleValues[M20] * doubleValues[M11] * doubleValues[M32] * doubleValues[M03] -
                doubleValues[M10] * doubleValues[M21] * doubleValues[M32] * doubleValues[M03] -
                doubleValues[M30] * doubleValues[M21] * doubleValues[M02] * doubleValues[M13] +
                doubleValues[M20] * doubleValues[M31] * doubleValues[M02] * doubleValues[M13] +

                doubleValues[M30] * doubleValues[M01] * doubleValues[M22] * doubleValues[M13] -
                doubleValues[M00] * doubleValues[M31] * doubleValues[M22] * doubleValues[M13] -
                doubleValues[M20] * doubleValues[M01] * doubleValues[M32] * doubleValues[M13] +
                doubleValues[M00] * doubleValues[M21] * doubleValues[M32] * doubleValues[M13] +

                doubleValues[M30] * doubleValues[M11] * doubleValues[M02] * doubleValues[M23] -
                doubleValues[M10] * doubleValues[M31] * doubleValues[M02] * doubleValues[M23] -
                doubleValues[M30] * doubleValues[M01] * doubleValues[M12] * doubleValues[M23] +
                doubleValues[M00] * doubleValues[M31] * doubleValues[M12] * doubleValues[M23] +

                doubleValues[M10] * doubleValues[M01] * doubleValues[M32] * doubleValues[M23] -
                doubleValues[M00] * doubleValues[M11] * doubleValues[M32] * doubleValues[M23] -
                doubleValues[M20] * doubleValues[M11] * doubleValues[M02] * doubleValues[M33] +
                doubleValues[M10] * doubleValues[M21] * doubleValues[M02] * doubleValues[M33] +

                doubleValues[M20] * doubleValues[M01] * doubleValues[M12] * doubleValues[M33] -
                doubleValues[M00] * doubleValues[M21] * doubleValues[M12] * doubleValues[M33] -
                doubleValues[M10] * doubleValues[M01] * doubleValues[M22] * doubleValues[M33] + doubleValues[M00] * doubleValues[M11] * doubleValues[M22] * doubleValues[M33]
    }


            /**
     * Inverts this [Matrix4].
     *
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    @Suppress("UNUSED_VARIABLE")
    fun inverse(): Matrix4 {
        val success = MatrixDoublePrecision.invertM(tmp, 0, doubleValues, 0)
        System.arraycopy(tmp, 0, doubleValues, 0, 16)
        return this
    }


            /**
     * Transposes this [Matrix4].
     *
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun transpose(): Matrix4 {
        MatrixDoublePrecision.transposeM(tmp, 0, doubleValues, 0)
        System.arraycopy(tmp, 0, doubleValues, 0, 16)
        return this
    }

    /**
     * Adds the given [Matrix4] to this one.
     *
     * @param matrix [Matrix4] The matrix to add.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun add(matrix: Matrix4): Matrix4 {
        matrix.toArray(tmp)
        doubleValues[0] += tmp[0]
        doubleValues[1] += tmp[1]
        doubleValues[2] += tmp[2]
        doubleValues[3] += tmp[3]
        doubleValues[4] += tmp[4]
        doubleValues[5] += tmp[5]
        doubleValues[6] += tmp[6]
        doubleValues[7] += tmp[7]
        doubleValues[8] += tmp[8]
        doubleValues[9] += tmp[9]
        doubleValues[10] += tmp[10]
        doubleValues[11] += tmp[11]
        doubleValues[12] += tmp[12]
        doubleValues[13] += tmp[13]
        doubleValues[14] += tmp[14]
        doubleValues[15] += tmp[15]
        return this
    }

    /**
     * Subtracts the given [Matrix4] to this one.
     *
     * @param matrix [Matrix4] The matrix to subtract.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun subtract(matrix: Matrix4): Matrix4 {
        matrix.toArray(tmp)
        doubleValues[0] -= tmp[0]
        doubleValues[1] -= tmp[1]
        doubleValues[2] -= tmp[2]
        doubleValues[3] -= tmp[3]
        doubleValues[4] -= tmp[4]
        doubleValues[5] -= tmp[5]
        doubleValues[6] -= tmp[6]
        doubleValues[7] -= tmp[7]
        doubleValues[8] -= tmp[8]
        doubleValues[9] -= tmp[9]
        doubleValues[10] -= tmp[10]
        doubleValues[11] -= tmp[11]
        doubleValues[12] -= tmp[12]
        doubleValues[13] -= tmp[13]
        doubleValues[14] -= tmp[14]
        doubleValues[15] -= tmp[15]
        return this
    }

    /**
     * Multiplies this [Matrix4] with the given one, storing the result in this [MatrixDoublePrecision].
     * <pre>
     * A.multiply(B) results in A = AB.
    </pre> *
     *
     * @param matrix [Matrix4] The RHS [Matrix4].
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    private fun multiply(matrix: Matrix4): Matrix4 {
        System.arraycopy(doubleValues, 0, tmp, 0, 16)
        MatrixDoublePrecision.multiplyMM(doubleValues, 0, tmp, 0, matrix.doubleValues, 0)
        return this
    }

    /**
     * Left multiplies this [Matrix4] with the given one, storing the result in this [MatrixDoublePrecision].
     * <pre>
     * A.leftMultiply(B) results in A = BA.
    </pre> *
     *
     * @param matrix [Matrix4] The LHS [Matrix4].
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun leftMultiply(matrix: Matrix4): Matrix4 {
        System.arraycopy(doubleValues, 0, tmp, 0, 16)
        MatrixDoublePrecision.multiplyMM(doubleValues, 0, matrix.doubleValues, 0, tmp, 0)
        return this
    }

    /**
     * Multiplies each element of this [Matrix4] by the provided factor.
     *
     * @param value double The multiplication factor.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun multiply(value: Double): Matrix4 {
        for (i in doubleValues.indices) doubleValues[i] *= value
        return this
    }

    /**
     * Adds a translation to this [Matrix4] based on the provided [MotmVector3].
     *
     * @param vec [MotmVector3] describing the translation components.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun translate(vec: MotmVector3): Matrix4 {
        doubleValues[M03] += vec.x
        doubleValues[M13] += vec.y
        doubleValues[M23] += vec.z
        return this
    }

    /**
     * Adds a translation to this [Matrix4] based on the provided components.
     *
     * @param x double The x component of the translation.
     * @param y double The y component of the translation.
     * @param z double The z component of the translation.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun translate(x: Double, y: Double, z: Double): Matrix4 {
        doubleValues[M03] += x
        doubleValues[M13] += y
        doubleValues[M23] += z
        return this
    }

    /**
     * Subtracts a translation to this [Matrix4] based on the provided [MotmVector3].
     *
     * @param vec [MotmVector3] describing the translation components.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun negTranslate(vec: MotmVector3): Matrix4 {
        return translate(-vec.x, -vec.y, -vec.z)
    }

    /**
     * Scales this [Matrix4] based on the provided components.
     *
     * @param vec [MotmVector3] describing the scaling on each axis.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun scale(vec: MotmVector3): Matrix4 {
        return scale(vec.x, vec.y, vec.z)
    }

    /**
     * Scales this [Matrix4] based on the provided components.
     *
     * @param x double The x component of the scaling.
     * @param y double The y component of the scaling.
     * @param z double The z component of the scaling.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun scale(x: Double, y: Double, z: Double): Matrix4 {
        MatrixDoublePrecision.scaleM(doubleValues, 0, x, y, z)
        return this
    }

    /**
     * Scales this [Matrix4] along all three axis by the provided value.
     *
     * @param s double The scaling factor.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun scale(s: Double): Matrix4 {
        return scale(s, s, s)
    }

    /**
     * Post multiplies this [Matrix4] with the rotation specified by the provided [Quaternion].
     *
     * @param quat [Quaternion] describing the rotation to apply.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    private fun rotate(quat: Quaternion): Matrix4 {
        if (mMatrix == null) {
            mMatrix = quat.toRotationMatrix()
        } else {
            quat.toRotationMatrix(mMatrix!!)
        }
        return multiply(mMatrix!!)
    }

    /**
     * Post multiplies this [Matrix4] with the rotation specified by the provided
     * axis and angle.
     *
     * @param axis [MotmVector3] The axis of rotation.
     * @param angle double The angle of rotation in degrees.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun rotate(axis: MotmVector3, angle: Double): Matrix4 {
        return if (angle == 0.0) this else rotate(mQuat.fromAngleAxis(axis, angle))
    }

    /**
     * Post multiplies this [Matrix4] with the rotation specified by the provided
     * cardinal axis and angle.
     *
     * @param axis [MotmVector3.Axis] The cardinal axis of rotation.
     * @param angle double The angle of rotation in degrees.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun rotate(axis: MotmVector3.Axis, angle: Double): Matrix4 {
        return if (angle == 0.0) this else rotate(mQuat.fromAngleAxis(axis, angle))
    }

    /**
     * Post multiplies this [Matrix4] with the rotation specified by the provided
     * axis and angle.
     *
     * @param x double The x component of the axis of rotation.
     * @param y double The y component of the axis of rotation.
     * @param z double The z component of the axis of rotation.
     * @param angle double The angle of rotation in degrees.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun rotate(x: Double, y: Double, z: Double, angle: Double): Matrix4 {
        return if (angle == 0.0) this else rotate(mQuat.fromAngleAxis(x, y, z, angle))
    }

    /**
     * Post multiplies this [Matrix4] with the rotation between the two provided
     * [MotmVector3]s.
     *
     * @param v1 [MotmVector3] The base vector.
     * @param v2 [MotmVector3] The target vector.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun rotate(v1: MotmVector3, v2: MotmVector3): Matrix4 {
        return rotate(mQuat.fromRotationBetween(v1, v2))
    }

    /**
     * Sets the translation of this [Matrix4] based on the provided [MotmVector3].
     *
     * @param vec [MotmVector3] describing the translation components.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setTranslation(vec: MotmVector3): Matrix4 {
        doubleValues[M03] = vec.x
        doubleValues[M13] = vec.y
        doubleValues[M23] = vec.z
        return this
    }

    /**
     * Sets the homogenous scale of this [Matrix4].
     *
     * @param zoom double The zoom value. 1 = no zoom.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setCoordinateZoom(zoom: Double): Matrix4 {
        doubleValues[M33] = zoom
        return this
    }

    /**
     * Rotates the given [MotmVector3] by the rotation specified by this [Matrix4].
     *
     * @param vec [MotmVector3] The vector to rotate.
     */
    fun rotateVector(vec: MotmVector3) {
        val x = vec.x * doubleValues[M00] + vec.y * doubleValues[M01] + vec.z * doubleValues[M02]
        val y = vec.x * doubleValues[M10] + vec.y * doubleValues[M11] + vec.z * doubleValues[M12]
        val z = vec.x * doubleValues[M20] + vec.y * doubleValues[M21] + vec.z * doubleValues[M22]
        vec.setAll(x, y, z)
    }

    /**
     * Projects a give [MotmVector3] with this [Matrix4] storing
     * the result in the given [MotmVector3].
     *
     * @param vec [MotmVector3] The vector to multiply by.
     * @return [MotmVector3] The resulting vector.
     */
    fun projectVector(vec: MotmVector3): MotmVector3 {
        val inv = 1.0 / (doubleValues[M03] * vec.x + doubleValues[M13] * vec.y + doubleValues[M23] * vec.z + doubleValues[M33])
        val x = (doubleValues[M00] * vec.x + doubleValues[M01] * vec.y + doubleValues[M02] * vec.z + doubleValues[M03]) * inv
        val y = (doubleValues[M10] * vec.x + doubleValues[M11] * vec.y + doubleValues[M12] * vec.z + doubleValues[M13]) * inv
        val z = (doubleValues[M20] * vec.x + doubleValues[M21] * vec.y + doubleValues[M22] * vec.z + doubleValues[M23]) * inv
        return vec.setAll(x, y, z)
    }

    /**
     * Projects a give [MotmVector3] with this [Matrix4] storing
     * the result in a new [MotmVector3].
     *
     * @param vec [MotmVector3] The vector to multiply by.
     * @return [MotmVector3] The resulting vector.
     */
    fun projectAndCreateVector(vec: MotmVector3): MotmVector3 {
        val r = MotmVector3()
        val inv = 1.0 / (doubleValues[M03] * vec.x + doubleValues[M13] * vec.y + doubleValues[M23] * vec.z + doubleValues[M33])
        r.x = (doubleValues[M00] * vec.x + doubleValues[M01] * vec.y + doubleValues[M02] * vec.z + doubleValues[M03]) * inv
        r.y = (doubleValues[M10] * vec.x + doubleValues[M11] * vec.y + doubleValues[M12] * vec.z + doubleValues[M13]) * inv
        r.z = (doubleValues[M20] * vec.x + doubleValues[M21] * vec.y + doubleValues[M22] * vec.z + doubleValues[M23]) * inv
        return r
    }

    /**
     * Sets translation of this [Matrix4] based on the provided components.
     *
     * @param x double The x component of the translation.
     * @param y double The y component of the translation.
     * @param z double The z component of the translation.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setTranslation(x: Double, y: Double, z: Double): Matrix4 {
        doubleValues[M03] = x
        doubleValues[M13] = y
        doubleValues[M23] = z
        return this
    }

    /**
     * Linearly interpolates between this [Matrix4] and the given [Matrix4] by
     * the given factor.
     *
     * @param matrix [Matrix4] The other matrix.
     * @param t `double` The interpolation ratio. The result is weighted to this value on the [Matrix4].
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun lerp(matrix: Matrix4, t: Double): Matrix4 {
        matrix.toArray(tmp)
        for (i in 0..15) doubleValues[i] = doubleValues[i] * (1.0 - t) + t * tmp[i]
        return this
    }


    //--------------------------------------------------
    // Set to methods
    //--------------------------------------------------

    /**
     * Sets this [Matrix4] to a Normal matrix.
     *
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToNormalMatrix(): Matrix4 {
        doubleValues[M03] = 0.0
        doubleValues[M13] = 0.0
        doubleValues[M23] = 0.0
        return inverse().transpose()
    }

    /**
     * Sets this [Matrix4] to a perspective projection matrix.
     *
     * @param near double The near plane.
     * @param far double The far plane.
     * @param fov double The field of view in degrees.
     * @param aspect double The aspect ratio. Defined as width/height.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToPerspective(near: Double, far: Double, fov: Double, aspect: Double): Matrix4 {
        identity()
        MatrixDoublePrecision.perspectiveM(doubleValues, 0, fov, aspect, near, far)
        return this
    }

    /**
     * Sets this [Matrix4] to an orthographic projection matrix with the origin at (x,y)
     * extended to the specified width and height. The near plane is at 0 and the far plane is at 1.
     *
     * @param x double The x coordinate of the origin.
     * @param y double The y coordinate of the origin.
     * @param width double The width.
     * @param height double The height.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToOrthographic2D(x: Double, y: Double, width: Double, height: Double): Matrix4 {
        return setToOrthographic(x, x + width, y, y + height, 0.0, 1.0)
    }

    /**
     * Sets this [Matrix4] to an orthographic projection matrix with the origin at (x,y)
     * extended to the specified width and height.
     *
     * @param x double The x coordinate of the origin.
     * @param y double The y coordinate of the origin.
     * @param width double The width.
     * @param height double The height.
     * @param near double The near plane.
     * @param far double The far plane.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToOrthographic2D(x: Double, y: Double, width: Double, height: Double, near: Double, far: Double): Matrix4 {
        return setToOrthographic(x, x + width, y, y + height, near, far)
    }

    /**
     *
     * @param left double The left plane.
     * @param right double The right plane.
     * @param bottom double The bottom plane.
     * @param top double The top plane.
     * @param near double The near plane.
     * @param far double The far plane.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    private fun setToOrthographic(left: Double, right: Double, bottom: Double, top: Double, near: Double, far: Double): Matrix4 {
        MatrixDoublePrecision.orthoM(doubleValues, 0, left, right, bottom, top, near, far)
        return this
    }

    /**
     * Sets this [Matrix4] to a translation matrix based on the provided [MotmVector3].
     *
     * @param vec [MotmVector3] describing the translation components.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToTranslation(vec: MotmVector3): Matrix4 {
        identity()
        doubleValues[M03] = vec.x
        doubleValues[M13] = vec.y
        doubleValues[M23] = vec.z
        return this
    }

    /**
     * Sets this [Matrix4] to a translation matrix based on the provided components.
     *
     * @param x double The x component of the translation.
     * @param y double The y component of the translation.
     * @param z double The z component of the translation.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToTranslation(x: Double, y: Double, z: Double): Matrix4 {
        identity()
        doubleValues[M03] = x
        doubleValues[M13] = y
        doubleValues[M23] = z
        return this
    }

    /**
     * Sets this [Matrix4] to a scale matrix based on the provided [MotmVector3].
     *
     * @param vec [MotmVector3] describing the scaling components.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToScale(vec: MotmVector3): Matrix4 {
        identity()
        doubleValues[M00] = vec.x
        doubleValues[M11] = vec.y
        doubleValues[M22] = vec.z
        return this
    }

    /**
     * Sets this [Matrix4] to a scale matrix based on the provided components.
     *
     * @param x double The x component of the translation.
     * @param y double The y component of the translation.
     * @param z double The z component of the translation.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToScale(x: Double, y: Double, z: Double): Matrix4 {
        identity()
        doubleValues[M00] = x
        doubleValues[M11] = y
        doubleValues[M22] = z
        return this
    }

    /**
     * Sets this [Matrix4] to a translation and scaling matrix.
     *
     * @param translation [MotmVector3] specifying the translation components.
     * @param scaling [MotmVector3] specifying the scaling components.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToTranslationAndScaling(translation: MotmVector3, scaling: MotmVector3): Matrix4 {
        identity()
        doubleValues[M03] = translation.x
        doubleValues[M13] = translation.y
        doubleValues[M23] = translation.z
        doubleValues[M00] = scaling.x
        doubleValues[M11] = scaling.y
        doubleValues[M22] = scaling.z
        return this
    }

    /**
     * Sets this [Matrix4] to a translation and scaling matrix.
     *
     * @param tx double The x component of the translation.
     * @param ty double The y component of the translation.
     * @param tz double The z component of the translation.
     * @param sx double The x component of the scaling.
     * @param sy double The y component of the scaling.
     * @param sz double The z component of the scaling.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToTranslationAndScaling(tx: Double, ty: Double, tz: Double, sx: Double, sy: Double, sz: Double): Matrix4 {
        identity()
        doubleValues[M03] = tx
        doubleValues[M13] = ty
        doubleValues[M23] = tz
        doubleValues[M00] = sx
        doubleValues[M11] = sy
        doubleValues[M22] = sz
        return this
    }

    /**
     * Sets this [Matrix4] to the specified rotation around the specified axis.
     *
     * @param axis [MotmVector3] The axis of rotation.
     * @param angle double The rotation angle in degrees.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToRotation(axis: MotmVector3, angle: Double): Matrix4 {
        return if (angle == 0.0) identity() else setAll(mQuat.fromAngleAxis(axis, angle))
    }

    /**
     * Sets this [Matrix4] to the specified rotation around the specified cardinal axis.
     *
     * @param axis [MotmVector3.Axis] The axis of rotation.
     * @param angle double The rotation angle in degrees.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToRotation(axis: MotmVector3.Axis, angle: Double): Matrix4 {
        return if (angle == 0.0) identity() else setAll(mQuat.fromAngleAxis(axis, angle))
    }

    /**
     * Sets this [Matrix4] to the specified rotation around the specified axis.
     *
     * @param x double The x component of the axis of rotation.
     * @param y double The y component of the axis of rotation.
     * @param z double The z component of the axis of rotation.
     * @param angle double The rotation angle.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToRotation(x: Double, y: Double, z: Double, angle: Double): Matrix4 {
        return if (angle == 0.0) identity() else setAll(mQuat.fromAngleAxis(x, y, z, angle))
    }

    /**
     * Sets this [Matrix4] to the rotation between two [MotmVector3] objects.
     *
     * @param v1 [MotmVector3] The base vector. Should be normalized.
     * @param v2 [MotmVector3] The target vector. Should be normalized.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToRotation(v1: MotmVector3, v2: MotmVector3): Matrix4 {
        return setAll(mQuat.fromRotationBetween(v1, v2))
    }

    /**
     * Sets this [Matrix4] to the rotation between two vectors. The
     * incoming vectors should be normalized.
     *
     * @param x1 double The x component of the base vector.
     * @param y1 double The y component of the base vector.
     * @param z1 double The z component of the base vector.
     * @param x2 double The x component of the target vector.
     * @param y2 double The y component of the target vector.
     * @param z2 double The z component of the target vector.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToRotation(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Matrix4 {
        return setAll(mQuat.fromRotationBetween(x1, y1, z1, x2, y2, z2))
    }

    /**
     * Sets this [Matrix4] to the rotation specified by the provided Euler angles.
     *
     * @param yaw double The yaw angle in degrees.
     * @param pitch double The pitch angle in degrees.
     * @param roll double The roll angle in degrees.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToRotation(yaw: Double, pitch: Double, roll: Double): Matrix4 {
        return setAll(mQuat.fromEuler(yaw, pitch, roll))
    }

    /**
     * Sets this [Matrix4] to a look at matrix with a direction and up [MotmVector3].
     * You can multiply this with a translation [Matrix4] to get a camera Model-View matrix.
     *
     * @param direction [MotmVector3] The look direction.
     * @param up [MotmVector3] The up axis.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToLookAt(direction: MotmVector3, up: MotmVector3): Matrix4 {
        vec3.setAll(direction).normalize()
        vec1.setAll(direction).normalize()
        vec1.cross(up).normalize()
        vec2.setAll(vec1).cross(vec3).normalize()
        identity()
        doubleValues[M00] = vec1.x
        doubleValues[M01] = vec1.y
        doubleValues[M02] = vec1.z
        doubleValues[M10] = vec2.x
        doubleValues[M11] = vec2.y
        doubleValues[M12] = vec2.z
        doubleValues[M20] = vec3.x
        doubleValues[M21] = vec3.y
        doubleValues[M22] = vec3.z
        return this
    }

    /**
     * Sets this [Matrix4] to a look at matrix with the given position, target and up [MotmVector3]s.
     *
     * @param position [MotmVector3] The eye position.
     * @param target [MotmVector3] The target position.
     * @param up [MotmVector3] The up axis.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToLookAt(position: MotmVector3, target: MotmVector3, up: MotmVector3): Matrix4 {
        MatrixDoublePrecision.setLookAtM(doubleValues, 0, position.x, position.y, position.z,
                target.x, target.y, target.z, up.x, up.y, up.z)
        return this
    }

    /**
     * Sets this [Matrix4] to a world matrix with the specified cardinal axis and the origin at the
     * provided position.
     *
     * @param position [MotmVector3] The position to use as the origin of the world coordinates.
     * @param forward [MotmVector3] The direction of the forward (z) vector.
     * @param up [MotmVector3] The direction of the up (y) vector.
     * @return A reference to this [Matrix4] to facilitate chaining.
     */
    fun setToWorld(position: MotmVector3, forward: MotmVector3, up: MotmVector3): Matrix4 {
        vec1.setAll(forward).normalize()
        vec2.setAll(vec1).cross(up).normalize()
        vec3.setAll(vec2).cross(vec1).normalize()
        return setAll(vec2, vec3, vec1, position)
    }

    private fun getTranslation(vec: MotmVector3): MotmVector3 {
        return vec.setAll(doubleValues[M03], doubleValues[M13], doubleValues[M23])
    }

    /**
     * Sets the components of the provided [MotmVector3] representing the scaling component
     * of this [Matrix4].
     *
     * @param vec [MotmVector3] to store the result in.
     * @return [MotmVector3] representing the scaling.
     */
    fun getScaling(vec: MotmVector3): MotmVector3 {
        val x = sqrt(doubleValues[M00] * doubleValues[M00] + doubleValues[M01] * doubleValues[M01] + doubleValues[M02] * doubleValues[M02])
        val y = sqrt(doubleValues[M10] * doubleValues[M10] + doubleValues[M11] * doubleValues[M11] + doubleValues[M12] * doubleValues[M12])
        val z = sqrt(doubleValues[M20] * doubleValues[M20] + doubleValues[M21] * doubleValues[M21] + doubleValues[M22] * doubleValues[M22])
        return vec.setAll(x, y, z)
    }

    /**
     * Create and return a copy of this [Matrix4].
     *
     * @return [Matrix4] The copy.
     */
    fun clone(): Matrix4 {
        return Matrix4(this)
    }

    /**
     * Copies the backing array of this [Matrix4] into the provided double array.
     *
     * @param doubleArray double array to store the copy in. Must be at least 16 elements long.
     * Entries will be placed starting at the 0 index.
     */
    fun toArray(doubleArray: DoubleArray) {
        System.arraycopy(doubleValues, 0, doubleArray, 0, 16)
    }

    fun toFloatArray(floatArray: FloatArray) {
        floatArray[0] = doubleValues[0].toFloat()
        floatArray[1] = doubleValues[1].toFloat()
        floatArray[2] = doubleValues[2].toFloat()
        floatArray[3] = doubleValues[3].toFloat()
        floatArray[4] = doubleValues[4].toFloat()
        floatArray[5] = doubleValues[5].toFloat()
        floatArray[6] = doubleValues[6].toFloat()
        floatArray[7] = doubleValues[7].toFloat()
        floatArray[8] = doubleValues[8].toFloat()
        floatArray[9] = doubleValues[9].toFloat()
        floatArray[10] = doubleValues[10].toFloat()
        floatArray[11] = doubleValues[11].toFloat()
        floatArray[12] = doubleValues[12].toFloat()
        floatArray[13] = doubleValues[13].toFloat()
        floatArray[14] = doubleValues[14].toFloat()
        floatArray[15] = doubleValues[15].toFloat()
    }

    /*
     * Determines if this [Matrix4] is equivalent to the provided [Matrix4]. For this
     * to be true each element must match exactly between the two.
     *
     * @param m2 [Matrix4] the other matrix.
     * @return boolean True if they are an exact match.
     */
    /*fun equals(m2: Matrix4): Boolean {
        m2.toArray(tmp)
        return !(
            doubleValues[0] != tmp[0]
            || doubleValues[1] != tmp[1]
            || doubleValues[2] != tmp[2]
            || doubleValues[3] != tmp[3]
            || doubleValues[4] != tmp[4]
            || doubleValues[5] != tmp[5]
            || doubleValues[6] != tmp[6]
            || doubleValues[7] != tmp[7]
            || doubleValues[8] != tmp[8]
            || doubleValues[9] != tmp[9]
            || doubleValues[10] != tmp[10]
            || doubleValues[11] != tmp[11]
            || doubleValues[12] != tmp[12]
            || doubleValues[13] != tmp[13]
            || doubleValues[14] != tmp[14]
            || doubleValues[15] != tmp[15])
    }*/

    /*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    override fun toString(): String {
        return ("[" + doubleValues[M00] + "|" + doubleValues[M01] + "|" + doubleValues[M02] + "|" + doubleValues[M03] + "]\n["
                + doubleValues[M10] + "|" + doubleValues[M11] + "|" + doubleValues[M12] + "|" + doubleValues[M13] + "]\n["
                + doubleValues[M20] + "|" + doubleValues[M21] + "|" + doubleValues[M22] + "|" + doubleValues[M23] + "]\n["
                + doubleValues[M30] + "|" + doubleValues[M31] + "|" + doubleValues[M32] + "|" + doubleValues[M33] + "]\n")
    }

    companion object {

        //Matrix indices as column major notation (Row x Column)
        const val M00 = 0  // 0;
        const val M01 = 4  // 1;
        const val M02 = 8  // 2;
        const val M03 = 12 // 3;
        const val M10 = 1  // 4;
        const val M11 = 5  // 5;
        const val M12 = 9  // 6;
        const val M13 = 13 // 7;
        const val M20 = 2  // 8;
        const val M21 = 6  // 9;
        const val M22 = 10 // 10;
        const val M23 = 14 // 11;
        const val M30 = 3  // 12;
        const val M31 = 7  // 13;
        const val M32 = 11 // 14;
        const val M33 = 15 // 15;

        //--------------------------------------------------
        // Creation methods
        //--------------------------------------------------

        /**
         * Creates a new [Matrix4] representing a rotation.
         *
         * @param quat [Quaternion] representing the rotation.
         * @return [Matrix4] The new matrix.
         */
        fun createRotationMatrix(quat: Quaternion): Matrix4 {
            return Matrix4(quat)
        }

        /**
         * Creates a new [Matrix4] representing a rotation.
         *
         * @param axis [MotmVector3] The axis of rotation.
         * @param angle double The rotation angle in degrees.
         * @return [Matrix4] The new matrix.
         */
        fun createRotationMatrix(axis: MotmVector3, angle: Double): Matrix4 {
            return Matrix4().setToRotation(axis, angle)
        }

        /**
         * Creates a new [Matrix4] representing a rotation.
         *
         * @param axis [MotmVector3.Axis] The axis of rotation.
         * @param angle double The rotation angle in degrees.
         * @return [Matrix4] The new matrix.
         */
        fun createRotationMatrix(axis: MotmVector3.Axis, angle: Double): Matrix4 {
            return Matrix4().setToRotation(axis, angle)
        }

        /**
         * Creates a new [Matrix4] representing a rotation.
         *
         * @param x double The x component of the axis of rotation.
         * @param y double The y component of the axis of rotation.
         * @param z double The z component of the axis of rotation.
         * @param angle double The rotation angle in degrees.
         * @return [Matrix4] The new matrix.
         */
        fun createRotationMatrix(x: Double, y: Double, z: Double, angle: Double): Matrix4 {
            return Matrix4().setToRotation(x, y, z, angle)
        }

        /**
         * Creates a new [Matrix4] representing a rotation by Euler angles.
         *
         * @param yaw double The yaw Euler angle.
         * @param pitch double The pitch Euler angle.
         * @param roll double The roll Euler angle.
         * @return [Matrix4] The new matrix.
         */
        fun createRotationMatrix(yaw: Double, pitch: Double, roll: Double): Matrix4 {
            return Matrix4().setToRotation(yaw, pitch, roll)
        }

        /**
         * Creates a new [Matrix4] representing a translation.
         *
         * @param vec [MotmVector3] describing the translation components.
         * @return A new [Matrix4] representing the translation only.
         */
        fun createTranslationMatrix(vec: MotmVector3): Matrix4 {
            return Matrix4().translate(vec)
        }

        /**
         * Creates a new [Matrix4] representing a translation.
         *
         * @param x double The x component of the translation.
         * @param y double The y component of the translation.
         * @param z double The z component of the translation.
         * @return A new [Matrix4] representing the translation only.
         */
        fun createTranslationMatrix(x: Double, y: Double, z: Double): Matrix4 {
            return Matrix4().translate(x, y, z)
        }

        /**
         * Creates a new [Matrix4] representing a scaling.
         *
         * @param vec [MotmVector3] describing the scaling components.
         * @return A new [Matrix4] representing the scaling only.
         */
        fun createScaleMatrix(vec: MotmVector3): Matrix4 {
            return Matrix4().setToScale(vec)
        }

        /**
         * Creates a new [Matrix4] representing a scaling.
         *
         * @param x double The x component of the scaling.
         * @param y double The y component of the scaling.
         * @param z double The z component of the scaling.
         * @return A new [Matrix4] representing the scaling only.
         */
        fun createScaleMatrix(x: Double, y: Double, z: Double): Matrix4 {
            return Matrix4().setToScale(x, y, z)
        }
    }
}