/*
 * Copyright 2013 Dennis Ippel
 * Copyright 2018 Jim Andreas kotlin conversion
 *
 * Licensed under the Apache License, Version 2.0f (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.bammellab.mollib.common.math

import kotlin.math.*

/**
 * Encapsulates a quaternion.
 *
 * Ported from http://www.ogre3d.org/docs/api/html/classOgre_1_1Quaternion.html
 *
 * Rewritten July 27, 2013 by Jared Woolston with heavy influence from libGDX
 * @see [https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java)
 *
 *
 * @author dennis.ippel
 * @author Jared Woolston (jwoolston@tenkiv.com)
 * @author Dominic Cerisano (Quaternion camera lookAt)
 */
class Quaternion {

    //The Quaternion components
    var w: Float = 0.toFloat()
    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()
    var z: Float = 0.toFloat()

    //Scratch members
    private val tmpVec1 = MotmVector3()
    private val tmpVec2 = MotmVector3()
    private val tmpVec3 = MotmVector3()


    //--------------------------------------------------
    // Quaternion operation methods
    //--------------------------------------------------

    /**
     * Creates a [MotmVector3] which represents the x axis of this [Quaternion].
     *
     * @return [MotmVector3] The x axis of this [Quaternion].
     */
    private val xAxis: MotmVector3
        get() {
            val fTy = 2.0f * y
            val fTz = 2.0f * z
            val fTwy = fTy * w
            val fTwz = fTz * w
            val fTxy = fTy * x
            val fTxz = fTz * x
            val fTyy = fTy * y
            val fTzz = fTz * z

            return MotmVector3(1f - (fTyy + fTzz), fTxy + fTwz, fTxz - fTwy)
        }

    /**
     * Creates a [MotmVector3] which represents the y axis of this [Quaternion].
     *
     * @return [MotmVector3] The y axis of this [Quaternion].
     */
    private val yAxis: MotmVector3
        get() {
            val fTx = 2.0f * x
            val fTy = 2.0f * y
            val fTz = 2.0f * z
            val fTwx = fTx * w
            val fTwz = fTz * w
            val fTxx = fTx * x
            val fTxy = fTy * x
            val fTyz = fTz * y
            val fTzz = fTz * z

            return MotmVector3(fTxy - fTwz, 1 - (fTxx + fTzz), fTyz + fTwx)
        }

    /**
     * Creates a [MotmVector3] which represents the z axis of this [Quaternion].
     *
     * @return [MotmVector3] The z axis of this [Quaternion].
     */
    private val zAxis: MotmVector3
        get() {
            val fTx = 2.0f * x
            val fTy = 2.0f * y
            val fTz = 2.0f * z
            val fTwx = fTx * w
            val fTwy = fTy * w
            val fTxx = fTx * x
            val fTxz = fTz * x
            val fTyy = fTy * y
            val fTyz = fTz * y

            return MotmVector3(fTxz + fTwy, fTyz - fTwx, 1 - (fTxx + fTyy))
        }

    /**
     * Get the pole of the gimbal lock, if any.
     *
     * @return positive (+1) for north pole, negative (-1) for south pole, zero (0) when no gimbal lock
     * @see [
     * https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java)
     */
    private val gimbalPole: Int
        get() {
            val t = y * x + z * w
            return if (t > 0.499) 1 else if (t < -0.499) -1 else 0
        }

    /**
     * Gets the roll angle from this [Quaternion]. This is defined as the rotation about the Z axis.
     *
     * @return double The roll angle in radians.
     * @see [
     * https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java)
     */
    val roll: Float
        get() {
            normalize()
            val pole = gimbalPole
            return if (pole == 0) atan2(2.0f * (w * z + y * x), 1.0f - 2.0f * (x * x + z * z)) else pole.toFloat() * 2.0f * atan2(y, w)
        }

    /**
     * Gets the pitch angle from this [Quaternion]. This is defined as the rotation about the X axis.
     *
     * @return double The pitch angle in radians.
     * @see [
     * https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java)
     */
    val pitch: Float
        get() {
            normalize()
            val pole = gimbalPole
            return if (pole == 0) {
                asin(MathUtil.clamp(2.0f * (w * x - z * y), -1.0f, 1.0f))
            } else {
                pole.toFloat() * MathUtil.PI * 0.5f
            }
        }

    /**
     * Gets the yaw angle from this [Quaternion]. This is defined as the rotation about the Y axis.
     *
     * @return double The yaw angle in radians.
     * @see [
     * https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Quaternion.java)
     */
    val yaw: Float
        get() {
            normalize()
            return if (gimbalPole == 0) atan2(2.0f * (y * w + x * z), 1.0f - 2.0f * (y * y + x * x)) else 0.0f
        }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Default constructor. Creates an identity [Quaternion].
     */
    constructor() {
        identity()
    }

    /**
     * Creates a [Quaternion] with the specified components.
     *
     * @param w double The w component.
     * @param x double The x component.
     * @param y double The y component.
     * @param z double The z component.
     */
    constructor(w: Float, x: Float, y: Float, z: Float) {
        setAll(w, x, y, z)
    }

    /**
     * Creates a [Quaternion] with components initialized by the provided
     * [Quaternion].
     *
     * @param quat [Quaternion] to take values from.
     */
    constructor(quat: Quaternion) {
        setAll(quat)
    }

    /**
     * Creates a [Quaternion] from the given axis vector and the rotation
     * angle around the axis.
     *
     * @param axis [MotmVector3] The axis of rotation.
     * @param angle double The angle of rotation in degrees.
     */
    constructor(axis: MotmVector3, angle: Float) {
        fromAngleAxis(axis, angle)
    }


    //--------------------------------------------------
    // Modification methods
    //--------------------------------------------------

    /**
     * Sets the components of this [Quaternion].
     *
     * @param w double The w component.
     * @param x double The x component.
     * @param y double The y component.
     * @param z double The z component.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun setAll(w: Float, x: Float, y: Float, z: Float): Quaternion {
        this.w = w
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    /**
     * Sets the components of this [Quaternion] from those
     * of the provided [Quaternion].
     *
     * @param quat [Quaternion] to take values from.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun setAll(quat: Quaternion): Quaternion {
        return setAll(quat.w, quat.x, quat.y, quat.z)
    }

    /**
     * Sets this [Quaternion]'s components from the given axis and angle around the axis.
     *
     * @param axis [MotmVector3.Axis] The cardinal axis to set rotation on.
     * @param angle double The rotation angle in degrees.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun fromAngleAxis(axis: MotmVector3.Axis, angle: Float): Quaternion {
        fromAngleAxis(MotmVector3.getAxisVector(axis), angle)
        return this
    }

    /**
     * Sets this [Quaternion]'s components from the given axis vector and angle around the axis.
     *
     * @param axis [MotmVector3] The axis to set rotation on.
     * @param angle double The rotation angle in degrees.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun fromAngleAxis(axis: MotmVector3, angle: Float): Quaternion {
        if (!axis.isUnit) axis.normalize()
        val radian = Math.toRadians(angle.toDouble()).toFloat() //MathUtil.degreesToRadians(angle);
        val halfAngle = radian * .5f
        val halfAngleSin = sin(halfAngle)
        w = cos(halfAngle)
        x = halfAngleSin * axis.x
        y = halfAngleSin * axis.y
        z = halfAngleSin * axis.z
        return this
    }

    /**
     * Sets this [Quaternion]'s components from the given axis vector and angle around the axis.
     *
     * @param x double The x component of the axis.
     * @param y double The y component of the axis.
     * @param z double The z component of the axis.
     * @param angle double The rotation angle in degrees.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun fromAngleAxis(x: Float, y: Float, z: Float, angle: Float): Quaternion {
        var d = MotmVector3.length(x, y, z)
        if (d == 0.0f) {
            return identity()
        }
        d = 1.0f / d
        val radians = Math.toRadians(angle.toDouble()).toFloat()
        val theAngle = if (radians < 0) MathUtil.TWO_PI - -radians % MathUtil.TWO_PI else radians % MathUtil.TWO_PI
        val theSin = sin(theAngle * 0.5).toFloat()
        val theCos = cos(theAngle * 0.5).toFloat()
        return this.setAll(theCos, d * x * theSin, d * y * theSin, d * z * theSin)
    }

    /**
     * Sets this [Quaternion]'s components from the given x, y anx z axis [MotmVector3]s.
     * The inputs must be ortho-normal.
     *
     * @param xAxis [MotmVector3] The x axis.
     * @param yAxis [MotmVector3] The y axis.
     * @param zAxis [MotmVector3] The z axis.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    private fun fromAxes(xAxis: MotmVector3, yAxis: MotmVector3, zAxis: MotmVector3): Quaternion {
        return fromAxes(xAxis.x, xAxis.y, xAxis.z, yAxis.x, yAxis.y, yAxis.z, zAxis.x, zAxis.y, zAxis.z)
    }

    /**
     * Sets this [Quaternion]'s components from the give x, y and z axis vectors
     * which must be ortho-normal.
     *
     * This method taken from libGDX, which took it from the following:
     *
     *
     *
     * Taken from Bones framework for JPCT, see http://www.aptalkarga.com/bones/ which in turn took it from Graphics Gem code at
     * ftp://ftp.cis.upenn.edu/pub/graphics/shoemake/quatut.ps.Z.
     *
     *
     * @param xx double The x axis x coordinate.
     * @param xy double The x axis y coordinate.
     * @param xz double The x axis z coordinate.
     * @param yx double The y axis x coordinate.
     * @param yy double The y axis y coordinate.
     * @param yz double The y axis z coordinate.
     * @param zx double The z axis x coordinate.
     * @param zy double The z axis y coordinate.
     * @param zz double The z axis z coordniate.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    private fun fromAxes(xx: Float, xy: Float, xz: Float, yx: Float, yy: Float, yz: Float,
                         zx: Float, zy: Float, zz: Float): Quaternion {
        // The trace is the sum of the diagonal elements; see
        // http://mathworld.wolfram.com/MatrixTrace.html
        val t = xx + yy + zz

        //Protect the division by s by ensuring that s >= 1
        val x: Float
        val y: Float
        val z: Float
        val w: Float
        if (t >= 0) {
            var s = sqrt(t + 1) // |s| >= 1
            w = 0.5f * s // |w| >= 0.5f 
            s = 0.5f / s //<- This division cannot be bad
            x = (zy - yz) * s
            y = (xz - zx) * s
            z = (yx - xy) * s
        } else if (xx > yy && xx > zz) {
            var s = sqrt(1.0f + xx - yy - zz) // |s| >= 1
            x = s * 0.5f // |x| >= 0.5f 
            s = 0.5f / s
            y = (yx + xy) * s
            z = (xz + zx) * s
            w = (zy - yz) * s
        } else if (yy > zz) {
            var s = sqrt(1.0f + yy - xx - zz) // |s| >= 1
            y = s * 0.5f // |y| >= 0.5f 
            s = 0.5f / s
            x = (yx + xy) * s
            z = (zy + yz) * s
            w = (xz - zx) * s
        } else {
            var s = sqrt(1.0f + zz - xx - yy) // |s| >= 1
            z = s * 0.5f // |z| >= 0.5f 
            s = 0.5f / s
            x = (xz + zx) * s
            y = (zy + yz) * s
            w = (yx - xy) * s
        }
        return setAll(w, x, y, z)
    }

    /**
     * Sets this [Quaternion]'s components from the given matrix.
     *
     * @param matrix [Matrix4] The rotation matrix.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun fromMatrix(matrix: Matrix4): Quaternion {
        val value = FloatArray(16)
        matrix.toArray(value)
        fromAxes(value[Matrix4.M00], value[Matrix4.M01], value[Matrix4.M02],
                value[Matrix4.M10], value[Matrix4.M11], value[Matrix4.M12],
                value[Matrix4.M20], value[Matrix4.M21], value[Matrix4.M22])
        return this
    }

    /**
     * Sets this [Quaternion] from the given Euler angles.
     *
     * @param yaw double The yaw angle in degrees.
     * @param pitch double The pitch angle in degrees.
     * @param roll double The roll angle in degrees.
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun fromEuler(yaw: Float, pitch: Float, roll: Float): Quaternion {
        var yawI = yaw
        var pitchI = pitch
        var rollI = roll
        yawI = Math.toRadians(yawI.toDouble()).toFloat()
        pitchI = Math.toRadians(pitchI.toDouble()).toFloat()
        rollI = Math.toRadians(rollI.toDouble()).toFloat()
        val num9 = rollI * 0.5f 
        val num6 = sin(num9)
        val num5 = cos(num9)
        val num8 = pitchI * 0.5f 
        val num4 = sin(num8)
        val num3 = cos(num8)
        val num7 = yawI * 0.5f 
        val num2 = sin(num7)
        val num = cos(num7)
        val f1 = num * num4
        val f2 = num2 * num3
        val f3 = num * num3
        val f4 = num2 * num4

        x = f1 * num5 + f2 * num6
        y = f2 * num5 - f1 * num6
        z = f3 * num6 - f4 * num5
        w = f3 * num5 + f4 * num6
        return this
    }

    /**
     * Sets this [Quaternion]'s components from the given input matrix.
     *
     * @param rotMatrix double[] The rotation matrix. 4x4 column major order.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    @Deprecated("")
    fun fromRotationMatrix(rotMatrix: FloatArray): Quaternion {
        // Algorithm in Ken Shoemake's article in 1987 SIGGRAPH course notes
        // article "Quaternion Calculus and Fast Animation".

        val fTrace = rotMatrix[0] + rotMatrix[5] + rotMatrix[10]
        var fRoot: Float

        if (fTrace > 0.0f) {
            // |w| > 1/2, may as well choose w > 1/2
            fRoot = sqrt(fTrace + 1.0f) // 2w
            w = 0.5f * fRoot
            fRoot = 0.5f / fRoot // 1/(4w)
            x = (rotMatrix[9] - rotMatrix[6]) * fRoot
            y = (rotMatrix[2] - rotMatrix[8]) * fRoot
            z = (rotMatrix[4] - rotMatrix[1]) * fRoot
        } else {
            // |w| <= 1/2
            val theNext = intArrayOf(1, 2, 0)
            var i = 0
            if (rotMatrix[5] > rotMatrix[0])
                i = 1
            if (rotMatrix[10] > rotMatrix[i * 4 + i])
                i = 2
            val j = theNext[i]
            val k = theNext[j]

            fRoot = sqrt(rotMatrix[i * 4 + i] - rotMatrix[j * 4 + j] - rotMatrix[k * 4 + k] + 1.0f)
            val apkQuat = floatArrayOf(x, y, z)
            apkQuat[i] = 0.5f * fRoot
            fRoot = 0.5f / fRoot
            w = (rotMatrix[k * 4 + j] - rotMatrix[j * 4 + k]) * fRoot
            apkQuat[j] = (rotMatrix[j * 4 + i] + rotMatrix[i * 4 + j]) * fRoot
            apkQuat[k] = (rotMatrix[k * 4 + i] + rotMatrix[i * 4 + k]) * fRoot

            x = apkQuat[0]
            y = apkQuat[1]
            z = apkQuat[2]
        }
        return this
    }

    /**
     * Set this [Quaternion]'s components to the rotation between the given
     * two [MotmVector3]s. This will fail if the two vectors are parallel.
     *
     * @param v1 [MotmVector3] The base vector, should be normalized.
     * @param v2 [MotmVector3] The target vector, should be normalized.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun fromRotationBetween(v1: MotmVector3, v2: MotmVector3): Quaternion {
        val dot = MathUtil.clamp(v1.dot(v2), -1.0f, 1.0f)
        val dotError = 1.0f - abs(dot)
        //        if (dotError <= 1e-6) {
        //            // The look and up vectors are parallel/anti-parallel
        //            if (dot < 0) {
        //                // The look and up vectors are parallel but opposite direction
        //                tmpVec3.crossAndSet(WorldParameters.RIGHT_AXIS, v1);
        //                if (tmpVec3.length() < 1e-6) {
        //                    // Vectors were co-linear, pick another
        //                    tmpVec3.crossAndSet(WorldParameters.UP_AXIS, v1);
        //                }
        //                tmpVec3.normalize();
        //                return fromAngleAxis(tmpVec3, 180.0);
        //            } else {
        //                // The look and up vectors are parallel in the same direction
        //                return identity();
        //            }
        //        }

        val angle = Math.toDegrees(acos(dot.toDouble())).toFloat()
        return fromAngleAxis(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x, angle)
    }

    /**
     * Sets this [Quaternion]'s components to the rotation between the given
     * two vectors. The incoming vectors should be normalized. This will fail if the two
     * vectors are parallel.
     *
     * @param x1 double The base vector's x component.
     * @param y1 double The base vector's y component.
     * @param z1 double The base vector's z component.
     * @param x2 double The target vector's x component.
     * @param y2 double The target vector's y component.
     * @param z2 double The target vector's z component.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun fromRotationBetween(x1: Float, y1: Float, z1: Float, x2: Float,
                            y2: Float, z2: Float): Quaternion {
        tmpVec1.setAll(x1, y1, z1).normalize()
        tmpVec2.setAll(x2, y2, z2).normalize()
        return fromRotationBetween(tmpVec1, tmpVec2)
    }

    /**
     * Adds the provided [Quaternion] to this one.
     *
     * @param quat [Quaternion] to be added to this one.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun add(quat: Quaternion): Quaternion {
        w += quat.w
        x += quat.x
        y += quat.y
        z += quat.z
        return this
    }

    /**
     * Subtracts the provided [Quaternion] from this one.
     *
     * @param quat [Quaternion] to be subtracted from this one.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun subtract(quat: Quaternion): Quaternion {
        w -= quat.w
        x -= quat.x
        y -= quat.y
        z -= quat.z
        return this
    }

    /**
     * Multiplies each component of this [Quaternion] by the input
     * value.
     *
     * @param scalar double The value to multiply by.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun multiply(scalar: Float): Quaternion {
        w *= scalar
        x *= scalar
        y *= scalar
        z *= scalar
        return this
    }

    /**
     * Multiplies this [Quaternion] with another one.
     *
     * @param quat [Quaternion] The other [Quaternion].
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun multiply(quat: Quaternion): Quaternion {
        val tW = w
        val tX = x
        val tY = y
        val tZ = z

        w = tW * quat.w - tX * quat.x - tY * quat.y - tZ * quat.z
        x = tW * quat.x + tX * quat.w + tY * quat.z - tZ * quat.y
        y = tW * quat.y + tY * quat.w + tZ * quat.x - tX * quat.z
        z = tW * quat.z + tZ * quat.w + tX * quat.y - tY * quat.x
        return this
    }

    /**
     * Multiplies this [Quaternion] by a [MotmVector3].
     * Note that if you plan on using the returned [MotmVector3],
     * you should clone it immediately as it is an internal scratch
     * member of this [Quaternion] and may be modified at any
     * time.
     *
     * @param vector [MotmVector3] to multiply by.
     * @return [MotmVector3] The result of the multiplication.
     */
    fun multiply(vector: MotmVector3): MotmVector3 {
        tmpVec3.setAll(x, y, z)
        tmpVec1.crossAndSet(tmpVec3, vector)
        tmpVec2.crossAndSet(tmpVec3, tmpVec1)
        tmpVec1.multiply(2.0f * w)
        tmpVec2.multiply(2.0f)

        tmpVec1.add(tmpVec2)
        tmpVec1.add(vector)

        return tmpVec1
    }

    /**
     * Multiplies this [Quaternion] with another in the form of quat * this.
     *
     * @param quat [Quaternion] The other [Quaternion].
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun multiplyLeft(quat: Quaternion): Quaternion {
        val newX = quat.w * x + quat.x * w + quat.y * z - quat.z * y
        val newY = quat.w * y + quat.y * w + quat.z * x - quat.x * z
        val newZ = quat.w * z + quat.z * w + quat.x * y - quat.y * x
        val newW = quat.w * w - quat.x * x + quat.y * y - quat.z * z
        return setAll(newW, newX, newY, newZ)
    }

    /**
     * Normalizes this [Quaternion] to unit length.
     *
     * @return double The scaling factor used to normalize this [Quaternion].
     */
    fun normalize(): Float {
        val len = length2()
        if (len != 0.0f && abs(len - 1.0f) > NORMALIZATION_TOLERANCE) {
            val factor = 1.0f / sqrt(len)
            multiply(factor)
        }
        return len
    }

    /**
     * Conjugate this [Quaternion].
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun conjugate(): Quaternion {
        x = -x
        y = -y
        z = -z
        return this
    }

    /**
     * Set this [Quaternion] to the normalized inverse of itself.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun inverse(): Quaternion {
        val norm = length2()
        if (norm > 0) {
            val invNorm = 1.0f / norm
            setAll(w * invNorm, -x * invNorm, -y * invNorm, -z * invNorm)
        }
        return this
    }

    /**
     * Create a new [Quaternion] set to the normalized inverse of this one.
     *
     * @return [Quaternion] The new inverted [Quaternion].
     */
    fun invertAndCreate(): Quaternion? {
        val norm = length2()
        return if (norm > 0) {
            val invNorm = 1.0f / norm
            Quaternion(w * invNorm, -x * invNorm, -y * invNorm, -z * invNorm)
        } else {
            null
        }
    }

    /**
     * Computes and sets w on this [Quaternion] based on x,y,z components such
     * that this [Quaternion] is of unit length.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun computeW(): Quaternion {
        val t = 1.0f - x * x - y * y - z * z
        w = if (t < 0.0f) {
            0.0f
        } else {
            -sqrt(t)
        }
        return this
    }

    /**
     * Creates a [MotmVector3] which represents the specified axis of this [Quaternion].
     *
     * @param axis The axis of this [Quaternion] to be returned.
     * @return [MotmVector3] The z axis of this [Quaternion].
     */
    fun getAxis(axis: MotmVector3.Axis): MotmVector3 {
        return when (axis) {
            MotmVector3.Axis.X -> xAxis
            MotmVector3.Axis.Y -> yAxis
            else -> zAxis
        }
    }

    /**
     * Calculates the Euclidean length of this [Quaternion].
     *
     * @return double The Euclidean length.
     */
    fun length(): Float {
        return sqrt(length2())
    }

    /**
     * Calculates the square of the Euclidean length of this [Quaternion].
     *
     * @return double The square of the Euclidean length.
     */
    private fun length2(): Float {
        return w * w + x * x + y * y + z * z
    }

    /**
     * Calculates the dot product between this and another [Quaternion].
     *
     * @return double The dot product.
     */
    fun dot(other: Quaternion): Float {
        return w * other.w + x * other.x + y * other.y + z * other.z
    }

    /**
     * Sets this [Quaternion] to an identity.
     *
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    private fun identity(): Quaternion {
        w = 1.0f
        x = 0.0f
        y = 0.0f
        z = 0.0f
        return this
    }

    /**
     * Sets this [Quaternion] to the value of q = e^this.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun exp(): Quaternion {
        val angle = sqrt(x * x + y * y + z * z)
        val sin = sin(angle)
        w = cos(angle)
        if (abs(sin) >= F_EPSILON) {
            val coeff = sin / angle
            x *= coeff
            y *= coeff
            z *= coeff
        }
        return this
    }

    /**
     * Creates a new [Quaternion] q initialized to the value of q = e^this.
     *
     * @return [Quaternion] The new [Quaternion] set to e^this.
     */
    fun expAndCreate(): Quaternion {
        val angle = sqrt(x * x + y * y + z * z)
        val sin = sin(angle)
        val result = Quaternion()
        result.w = cos(angle)
        if (abs(sin) >= F_EPSILON) {
            val coeff = sin / angle
            result.x = coeff * x
            result.y = coeff * y
            result.z = coeff * z
        } else {
            result.x = x
            result.y = y
            result.z = z
        }
        return result
    }

    /**
     * Sets this [Quaternion] to the value of q = log(this).
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun log(): Quaternion {
        if (abs(w) < 1.0f) {
            val angle = acos(w)
            val sin = sin(angle)
            if (abs(sin) >= F_EPSILON) {
                val fCoeff = angle / sin
                x *= fCoeff
                y *= fCoeff
                z *= fCoeff
            }
        }
        w = 0.0f
        return this
    }

    /**
     * Creates a new [Quaternion] q initialized to the value of q = log(this).
     *
     * @return [Quaternion] The new [Quaternion] set to log(q).
     */
    fun logAndCreate(): Quaternion {
        val result = Quaternion()
        result.w = 0.0f
        if (abs(w) < 1.0f) {
            val angle = acos(w)
            val sin = sin(angle)
            if (abs(sin) >= F_EPSILON) {
                val fCoeff = angle / sin
                result.x = fCoeff * x
                result.y = fCoeff * y
                result.z = fCoeff * z
                return result
            }
        }
        result.x = x
        result.y = y
        result.z = z
        return result
    }

    /**
     * Performs spherical linear interpolation between this and the provided [Quaternion]
     * and sets this [Quaternion] to the result.
     *
     * @param end [Quaternion] The destination point.
     * @param t double The interpolation value. [0-1] Where 0 represents this and 1 represents end.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun slerp(end: Quaternion, t: Float): Quaternion {
        val dot = dot(end)
        val absDot = if (dot < 0) -dot else dot

        //Set the first and second scale for the interpolation
        var scale0 = 1 - t
        var scale1 = t

        //Check if the angle between the 2 quaternions was big enough to warrant calculations
        if (1 - absDot > 0.1) {
            val angle = acos(absDot)
            val invSinTheta = 1 / sin(angle)
            //Calculate the scale for q1 and q2 according to the angle and its sine value
            scale0 = sin((1 - t) * angle) * invSinTheta
            scale1 = sin(t * angle * invSinTheta)
        }

        if (dot < 0) scale1 = -scale1

        //Calculate the x,y,z and w values for the quaternion by using a special form of
        //linear interpolation for quaternions.
        x = scale0 * x + scale1 * end.x
        y = scale0 * y + scale1 * end.y
        z = scale0 * z + scale1 * end.z
        w = scale0 * w + scale1 * end.w
        return this
    }

    /**
     * Performs spherical linear interpolation between the provided [Quaternion]s and
     * sets this [Quaternion] to the result.
     *
     * @param q1 [Quaternion] The starting point.
     * @param q2 [Quaternion] The destination point.
     * @param t double The interpolation value. [0-1] Where 0 represents q1 and 1 represents q2.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun slerp(q1: Quaternion, q2: Quaternion, t: Float): Quaternion {
        if (q1.x == q2.x && q1.y == q2.y && q1.z == q2.z && q1.w == q2.w) {
            setAll(q1)
            return this
        }

        var result = (q1.x * q2.x + q1.y * q2.y + q1.z * q2.z
                + q1.w * q2.w)

        if (result < 0.0f) {
            q2.x = -q2.x
            q2.y = -q2.y
            q2.z = -q2.z
            q2.w = -q2.w
            result = -result
        }

        var scale0 = 1 - t
        var scale1 = t

        if (1 - result > 0.1) {
            val theta = acos(result)
            val invSinTheta = 1 / sin(theta)

            scale0 = sin((1 - t) * theta) * invSinTheta
            scale1 = sin(t * theta) * invSinTheta
        }

        x = scale0 * q1.x + scale1 * q2.x
        y = scale0 * q1.y + scale1 * q2.y
        z = scale0 * q1.z + scale1 * q2.z
        w = scale0 * q1.w + scale1 * q2.w
        return this
    }

    /**
     * Creates a [Matrix4] representing this [Quaternion].
     *
     * @return [Matrix4] representing this [Quaternion].
     */
    fun toRotationMatrix(): Matrix4 {
        val matrix = Matrix4()
        toRotationMatrix(matrix)
        return matrix
    }

    /**
     * Sets the provided [Matrix4] to represent this [Quaternion].
     */
    fun toRotationMatrix(matrix: Matrix4) {
        toRotationMatrix(matrix.floatArray)
    }

    /**
     * Sets the provided double[] to be a 4x4 rotation matrix representing this [Quaternion].
     *
     * @param matrix double[] representing a 4x4 rotation matrix in column major order.
     */
    fun toRotationMatrix(matrix: FloatArray) {
        val x2 = x * x
        val y2 = y * y
        val z2 = z * z
        val xy = x * y
        val xz = x * z
        val yz = y * z
        val wx = w * x
        val wy = w * y
        val wz = w * z

        matrix[Matrix4.M00] = 1.0f - 2.0f * (y2 + z2)
        matrix[Matrix4.M10] = 2.0f * (xy - wz)
        matrix[Matrix4.M20] = 2.0f * (xz + wy)
        matrix[Matrix4.M30] = 0.0f

        matrix[Matrix4.M01] = 2.0f * (xy + wz)
        matrix[Matrix4.M11] = 1.0f - 2.0f * (x2 + z2)
        matrix[Matrix4.M21] = 2.0f * (yz - wx)
        matrix[Matrix4.M31] = 0.0f

        matrix[Matrix4.M02] = 2.0f * (xz - wy)
        matrix[Matrix4.M12] = 2.0f * (yz + wx)
        matrix[Matrix4.M22] = 1.0f - 2.0f * (x2 + y2)
        matrix[Matrix4.M32] = 0.0f

        matrix[Matrix4.M03] = 0.0f
        matrix[Matrix4.M13] = 0.0f
        matrix[Matrix4.M23] = 0.0f
        matrix[Matrix4.M33] = 1.0f
    }

    /**
     * Sets this [Quaternion] to be oriented to a target [MotmVector3].
     * It is safe to use the input vectors for other things as they are cloned internally.
     *
     * @param lookAt [MotmVector3] The point to look at.
     * @param upDirection [MotmVector3] to use as the up direction.
     * @return A reference to this [Quaternion] to facilitate chaining.
     */
    fun lookAt(lookAt: MotmVector3, upDirection: MotmVector3): Quaternion {
        tmpVec1.setAll(lookAt)
        tmpVec2.setAll(upDirection)
        // Vectors are parallel/anti-parallel if their dot product magnitude and length product are equal
        val dotProduct = MotmVector3.dot(lookAt, upDirection)
        val dotError = abs(abs(dotProduct) - lookAt.length() * upDirection.length())
        //        if (dotError <= 1e-6) {
        //            // The look and up vectors are parallel
        //            tmpVec2.normalize();
        //            if (dotProduct < 0) tmpVec1.inverse();
        //            fromRotationBetween(WorldParameters.FORWARD_AXIS, tmpVec1);
        //            return this;
        //        }
        MotmVector3.orthoNormalize(tmpVec1, tmpVec2) // Find the forward and up vectors
        tmpVec3.crossAndSet(tmpVec1, tmpVec2) // Create the right vector
        fromAxes(tmpVec3, tmpVec2, tmpVec1)
        return this
    }


    //--------------------------------------------------
    // Utility methods
    //--------------------------------------------------

    /**
     * Clones this [Quaternion].
     *
     * @return [Quaternion] A copy of this [Quaternion].
     */
    fun clone(): Quaternion {
        return Quaternion(w, x, y, z)
    }

    /*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Quaternion) {
            return false
        }
        val comp = other as Quaternion?
        return x == comp!!.x && y == comp.y && z == comp.z && w == comp.w
    }

    /**
     * Compares this [Quaternion] to another with a tolerance.
     *
     * @param other [Quaternion] The other [Quaternion].
     * @param tolerance double The tolerance for equality.
     * @return boolean True if the two [Quaternion]s equate within the specified tolerance.
     */
    fun equals(other: Quaternion, tolerance: Float): Boolean {
        val fCos = dot(other)
        if (fCos > 1.0f && fCos - 1.0f < tolerance) return true
        val angle = acos(fCos)
        return abs(angle) <= tolerance || MathUtil.realEqual(angle, MathUtil.PI, tolerance)
    }

    /**
     * Measures the angle between this [Quaternion] and another.
     *
     * @param other [Quaternion] The other [Quaternion].
     * @returns double with the angle between the two [Quaternion]s.
     */
    fun angleBetween(other: Quaternion): Float {
        var fCos = dot(other)
        fCos = max(-1.0f, min(1.0f, fCos))
        val angle = acos(fCos)
        return if (angle > MathUtil.PI / 2) {
            abs(MathUtil.PI - angle)
        } else {
            abs(angle)
        }
    }

    /*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    override fun toString(): String {
        val sb = StringBuffer()
        sb.append("Quaternion <w, x, y, z>: <")
                .append(w)
                .append(", ")
                .append(x)
                .append(", ")
                .append(y)
                .append(", ")
                .append(z)
                .append(">")
        return sb.toString()
    }

    override fun hashCode(): Int {
        var result = w.hashCode()
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + tmpVec1.hashCode()
        result = 31 * result + tmpVec2.hashCode()
        result = 31 * result + tmpVec3.hashCode()
        return result
    }

    companion object {
        //Tolerances
        const val F_EPSILON = .001
        const val NORMALIZATION_TOLERANCE = 0.00001
        private val sTmp1 = Quaternion(0.0f, 0.0f, 0.0f, 0.0f)
        private val sTmp2 = Quaternion(0.0f, 0.0f, 0.0f, 0.0f)

        /**
         * Creates a new [Quaternion] and sets its components to the rotation between the given
         * two vectors. The incoming vectors should be normalized.
         *
         * @param v1 [MotmVector3] The source vector.
         * @param v2 [MotmVector3] The destination vector.
         * @return [Quaternion] The new [Quaternion].
         */
        fun createFromRotationBetween(v1: MotmVector3, v2: MotmVector3): Quaternion {
            val q = Quaternion()
            q.fromRotationBetween(v1, v2)
            return q
        }

        /**
         * Retrieves a new [Quaternion] initialized to identity.
         *
         * @return A new identity [Quaternion].
         */
        val identity: Quaternion
            get() = Quaternion(1.0f, 0.0f, 0.0f, 0.0f)

        /**
         * Performs spherical linear interpolation between the provided [Quaternion]s and
         * creates a new [Quaternion] for the result.
         *
         * @param q1 [Quaternion] The starting point.
         * @param q2 [Quaternion] The destination point.
         * @param t double The interpolation value. [0-1] Where 0 represents q1 and 1 represents q2.
         * @return A reference to this [Quaternion] to facilitate chaining.
         */
        fun slerpAndCreate(q1: Quaternion, q2: Quaternion, t: Float): Quaternion {
            val q = Quaternion()
            q.slerp(q1, q2, t)
            return q
        }

        /**
         * Performs linear interpolation between two [Quaternion]s and creates a new one
         * for the result.
         *
         * @param rkP [Quaternion] The starting point.
         * @param rkQ [Quaternion] The destination point.
         * @param t double The interpolation value. [0-1] Where 0 represents q1 and 1 represents q2.
         * @param shortestPath boolean indicating if the shortest path should be used.
         * @return [Quaternion] The interpolated [Quaternion].
         */
        private fun lerp(rkP: Quaternion, rkQ: Quaternion, t: Float, shortestPath: Boolean): Quaternion {
            sTmp1.setAll(rkP)
            sTmp2.setAll(rkQ)
            val fCos = sTmp1.dot(sTmp2)
            if (fCos < 0.0f && shortestPath) {
                sTmp2.inverse()
                sTmp2.subtract(sTmp1)
                sTmp2.multiply(t)
                sTmp1.add(sTmp2)
            } else {
                sTmp2.subtract(sTmp1)
                sTmp2.multiply(t)
                sTmp1.add(sTmp2)
            }
            return sTmp1
        }

        /**
         * Performs normalized linear interpolation between two [Quaternion]s and creates a new one
         * for the result.
         *
         * @param rkP [Quaternion] The starting point.
         * @param rkQ [Quaternion] The destination point.
         * @param t double The interpolation value. [0-1] Where 0 represents q1 and 1 represents q2.
         * @param shortestPath boolean indicating if the shortest path should be used.
         * @return [Quaternion] The normalized interpolated [Quaternion].
         */
        fun nlerp(rkP: Quaternion, rkQ: Quaternion, t: Float, shortestPath: Boolean): Quaternion {
            val result = lerp(rkP, rkQ, t, shortestPath)
            result.normalize()
            return result
        }

        /**
         * Creates a new [Quaternion] which is oriented to a target [MotmVector3].
         * It is safe to use the input vectors for other things as they are cloned internally.
         *
         * @param lookAt [MotmVector3] The point to look at.
         * @param upDirection [MotmVector3] to use as the up direction.
         * @return [Quaternion] The new [Quaternion] representing the requested orientation.
         */
        fun lookAtAndCreate(lookAt: MotmVector3, upDirection: MotmVector3): Quaternion {
            val ret = Quaternion()
            return ret.lookAt(lookAt, upDirection)
        }
    }
}