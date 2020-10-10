/*
 * Copyright 2013 Dennis Ippel
 * Copyright 2018 Jim Andreas kotlin conversion
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

import com.kotmol.pdbParser.KotmolVector3
import kotlin.math.*

/*
 * Encapsulates a 3D point/vector.
 *
 *
 * This class borrows heavily from the implementation.
 *
 * @author dennis.ippel
 * @author Jared Woolston (jwoolston@tenkiv.com)
 * @author Dominic Cerisano (Gram-Schmidt orthonormalization)
 * @see [libGDX->MotmVector3](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/MotmVector3.java)
 *
 *
 * This class is not thread safe and must be confined to a single thread or by
 * some external locking mechanism if necessary. All static methods are thread safe.
 */
class MotmVector3 {
    //The vector components
    var x: Double = 0.toDouble()
    var y: Double = 0.toDouble()
    var z: Double = 0.toDouble()

    //Scratch vector. We use lazy loading here.
    private var temp: MotmVector3? = null

    /*
     * Checks if this [MotmVector3] is of unit length with a default
     * margin of error of 1e-8.
     *
     * @return boolean True if this [MotmVector3] is of unit length.
     */
    val isUnit: Boolean
        get() = isUnit(1e-8)

    /*
     * Checks if this [MotmVector3] is a zero vector.
     *
     * @return boolean True if all 3 components are equal to zero.
     */
    val isZero: Boolean
        get() = x == 0.0 && y == 0.0 && z == 0.0

    /*
     * Enumeration for the 3 component axes.
     */
    enum class Axis {
        X, Y, Z
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /*
     * Constructs a new [MotmVector3] at (0, 0, 0).
     */
    constructor() {
        //They are technically zero, but we wont rely on the uninitialized state here.
        x = 0.0
        y = 0.0
        z = 0.0
    }

    /*
     * Constructs a new [MotmVector3] at <from></from>, from, from>,>.
     *
     * @param from double which all components will be initialized to.
     */
    constructor(from: Double) {
        x = from
        y = from
        z = from
    }

    /*
     * Constructs a new [MotmVector3] with components matching the input [MotmVector3].
     *
     * @param from [MotmVector3] to initialize the components with.
     */
    constructor(from: MotmVector3) {
        x = from.x
        y = from.y
        z = from.z
    }

    /*
     * NEW: pull in the Atom coordinates from the Kotmol pdb parser.
     * Constructs a new [MotmVector3] with components matching the input [MotmVector3].
     *
     * @param from [MotmVector3] to initialize the components with.
     */
    constructor(from: KotmolVector3) {
        x = from.x
        y = from.y
        z = from.z
    }

    /*
     * Constructs a new {@link MotmVector3} with components initialized from the input {@link String} array.
     *
     * @param values A {@link String} array of values to be parsed for each component.
     *
     * @throws {@link IllegalArgumentException} if there are fewer than 3 values in the array.
     * @throws {@link NumberFormatException} if there is a problem parsing the {@link String} values into doubles.
     */
    @Throws(IllegalArgumentException::class, NumberFormatException::class)
    constructor(values: Array<String>) : this(
            java.lang.Float.parseFloat(values[0]).toDouble(),
            java.lang.Float.parseFloat(values[1]).toDouble(),
            java.lang.Float.parseFloat(values[2]).toDouble()
    )

    /*
     * Constructs a new {@link MotmVector3} with components initialized from the input double array.
     *
     * @param values A double array of values to be parsed for each component.
     *
     * @throws {@link IllegalArgumentException} if there are fewer than 3 values in the array.
     */
    @Throws(IllegalArgumentException::class)
    constructor(values: DoubleArray) {
        if (values.size < 3) throw IllegalArgumentException("MotmVector3 must be initialized with an array length of at least 3.")
        x = values[0]
        y = values[1]
        z = values[2]
    }

    /*
     * Constructs a new [MotmVector3] object with components initialized to the specified values.
     *
     * @param x double The x component.
     * @param y double The y component.
     * @param z double The z component.
     */
    constructor(x: Double, y: Double, z: Double) {
        this.x = x
        this.y = y
        this.z = z
    }


    //--------------------------------------------------
    // Modification methods
    //--------------------------------------------------

    /*
     * Sets all components of this [MotmVector3] to the specified values.
     *
     * @param x double The x component.
     * @param y double The y component.
     * @param z double The z component.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun setAll(x: Double, y: Double, z: Double): MotmVector3 {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    /*
     * Sets all components of this [MotmVector3] to the values provided
     * by the input [MotmVector3].
     *
     * @param other [MotmVector3] The vector to copy.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun setAll(other: MotmVector3): MotmVector3 {
        x = other.x
        y = other.y
        z = other.z
        return this
    }

    /*
     * Sets all components of this [MotmVector3] to the values provided representing
     * the input [Axis].
     *
     * @param axis [Axis] The cardinal axis to set the values to.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun setAll(axis: Axis): MotmVector3 {
        return setAll(getAxisVector(axis))
    }

    /*
     * Adds the provided [MotmVector3] to this one.
     *
     * @param v [MotmVector3] to be added to this one.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun add(v: MotmVector3): MotmVector3 {
        x += v.x
        y += v.y
        z += v.z
        return this
    }

    /*
     * Adds the given values to the respective components of this [MotmVector3].
     *
     * @param x The value to add to the x component.
     * @param y The value to add to the y component.
     * @param z The value to add to the z component.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun add(x: Double, y: Double, z: Double): MotmVector3 {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    /*
     * Adds the given value to each component of this [MotmVector3].
     *
     * @param value double value to add.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun add(value: Double): MotmVector3 {
        x += value
        y += value
        z += value
        return this
    }

    /*
     * Adds two input [MotmVector3] objects and sets this one to the result.
     *
     * @param a [MotmVector3] The first vector.
     * @param b [MotmVector3] The second vector.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun addAndSet(a: MotmVector3, b: MotmVector3): MotmVector3 {
        x = a.x + b.x
        y = a.y + b.y
        z = a.z + b.z
        return this
    }

    /*
     * Subtracts the provided [MotmVector3] from this one.
     *
     * @param v [MotmVector3] to be subtracted from this one.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun subtract(v: MotmVector3): MotmVector3 {
        x -= v.x
        y -= v.y
        z -= v.z
        return this
    }

    /*
     * Subtracts the given values from the respective components of this [MotmVector3].
     *
     * @param x The value to subtract to the x component.
     * @param y The value to subtract to the y component.
     * @param z The value to subtract to the z component.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun subtract(x: Double, y: Double, z: Double): MotmVector3 {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    /*
     * Subtracts the given value from each component of this [MotmVector3].
     *
     * @param value double value to subtract.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun subtract(value: Double): MotmVector3 {
        x -= value
        y -= value
        z -= value
        return this
    }

    /*
     * Subtracts two input [MotmVector3] objects and sets this one to the result.
     *
     * @param a [MotmVector3] The first vector.
     * @param b [MotmVector3] The second vector.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun subtractAndSet(a: MotmVector3, b: MotmVector3): MotmVector3 {
        x = a.x - b.x
        y = a.y - b.y
        z = a.z - b.z
        return this
    }

    /*
     * Scales each component of this [MotmVector3] by the specified value.
     *
     * @param value double The value to scale each component by.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun multiply(value: Double): MotmVector3 {
        x *= value
        y *= value
        z *= value
        return this
    }

    /*
     * Scales each component of this [MotmVector3] by the corresponding components
     * of the provided [MotmVector3].
     *
     * @param v [MotmVector3] containing the values to scale by.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun multiply(v: MotmVector3): MotmVector3 {
        x *= v.x
        y *= v.y
        z *= v.z
        return this
    }


    /*
     * Multiplies two input [MotmVector3] objects and sets this one to the result.
     *
     * @param a [MotmVector3] The first vector.
     * @param b [MotmVector3] The second vector.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun multiplyAndSet(a: MotmVector3, b: MotmVector3): MotmVector3 {
        x = a.x * b.x
        y = a.y * b.y
        z = a.z * b.z
        return this
    }

    /*
     * Divide each component of this [MotmVector3] by the specified value.
     *
     * @param value double The value to divide each component by.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun divide(value: Double): MotmVector3 {
        x /= value
        y /= value
        z /= value
        return this
    }

    /*
     * Divides each component of this [MotmVector3] by the corresponding components
     * of the provided [MotmVector3].
     *
     * @param v [MotmVector3] containing the values to divide by.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun divide(v: MotmVector3): MotmVector3 {
        x /= v.x
        y /= v.y
        z /= v.z
        return this
    }

    /*
     * Divides two input [MotmVector3] objects and sets this one to the result.
     *
     * @param a [MotmVector3] The first vector.
     * @param b [MotmVector3] The second vector.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun divideAndSet(a: MotmVector3, b: MotmVector3): MotmVector3 {
        x = a.x / b.x
        y = a.y / b.y
        z = a.z / b.z
        return this
    }

    /*
     * Scales an input [MotmVector3] by a value and sets this one to the result.
     *
     * @param a [MotmVector3] The [MotmVector3] to scale.
     * @param b [MotmVector3] The scaling factor.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun scaleAndSet(a: MotmVector3, b: Double): MotmVector3 {
        x = a.x * b
        y = a.y * b
        z = a.z * b
        return this
    }

    /*
     * Rotates this [MotmVector3] about the X axis by the angle specified.
     *
     * @param angle double The angle to rotate by in radians.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun rotateX(angle: Double): MotmVector3 {
        val cosRY = cos(angle)
        val sinRY = sin(angle)
        if (temp == null) temp = MotmVector3()
        temp!!.setAll(x, y, z)
        y = temp!!.y * cosRY - temp!!.z * sinRY
        z = temp!!.y * sinRY + temp!!.z * cosRY
        return this
    }

    /*
     * Rotates this [MotmVector3] about the Y axis by the angle specified.
     *
     * @param angle double The angle to rotate by in radians.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun rotateY(angle: Double): MotmVector3 {
        val cosRY = cos(angle)
        val sinRY = sin(angle)
        if (temp == null) temp = MotmVector3()
        temp!!.setAll(x, y, z)
        x = temp!!.x * cosRY + temp!!.z * sinRY
        z = temp!!.x * -sinRY + temp!!.z * cosRY
        return this
    }

    /*
     * Rotates this [MotmVector3] about the Z axis by the angle specified.
     *
     * @param angle double The angle to rotate by in radians.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun rotateZ(angle: Double): MotmVector3 {
        val cosRY = cos(angle)
        val sinRY = sin(angle)
        if (temp == null) temp = MotmVector3()
        temp!!.setAll(x, y, z)
        x = temp!!.x * cosRY - temp!!.y * sinRY
        y = temp!!.x * sinRY + temp!!.y * cosRY
        return this
    }

    /*
     * Normalize this [MotmVector3] to unit length.
     *
     * @return double The initial magnitude.
     */
    fun normalize(): Double {
        val mag = sqrt(x * x + y * y + z * z)
        if (mag != 0.0 && mag != 1.0) {
            val mod = 1 / mag
            x *= mod
            y *= mod
            z *= mod
        }
        return mag
    }

    /*
     * Inverts the direction of this [MotmVector3].
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun inverse(): MotmVector3 {
        x = -x
        y = -y
        z = -z
        return this
    }

    /*
     * Inverts the direction of this [MotmVector3] and creates a new one set with the result.
     *
     * @return [MotmVector3] The resulting [MotmVector3].
     */
    fun invertAndCreate(): MotmVector3 {
        return MotmVector3(-x, -y, -z)
    }

    /*
     * Computes the Euclidean length of this [MotmVector3];
     *
     * @return double The Euclidean length.
     */
    fun length(): Double {
        return length(this)
    }

    /*
     * Computes the squared Euclidean length of this [MotmVector3];
     *
     * @return double The squared Euclidean length.
     */
    fun length2(): Double {
        return x * x + y * y + z * z
    }

    /*
     * Computes the Euclidean length of this [MotmVector3] to the specified [MotmVector3].
     *
     * @param other [MotmVector3] The [MotmVector3] to compute the distance to.
     *
     * @return double The Euclidean distance.
     */
    fun distanceTo(other: MotmVector3): Double {
        val a = x - other.x
        val b = y - other.y
        val c = z - other.z
        return sqrt(a * a + b * b + c * c)
    }

    /*
     * Computes the Euclidean length of this [MotmVector3] to the specified point.
     *
     * @param  x The point x coordinate.
     * @param  y The point y coordinate.
     * @param z The point z coordinate.
     *
     * @return double The Euclidean distance.
     */
    fun distanceTo(x: Double, y: Double, z: Double): Double {
        val a = this.x - x
        val b = this.y - y
        val c = this.z - z
        return sqrt(a * a + b * b + c * c)
    }

    /*
     * Computes the squared Euclidean length of this [MotmVector3] to the specified [MotmVector3].
     *
     * @param other [MotmVector3] The [MotmVector3] to compute the distance to.
     *
     * @return double The squared Euclidean distance.
     */
    fun distanceTo2(other: MotmVector3): Double {
        val a = x - other.x
        val b = y - other.y
        val c = z - other.z
        return a * a + b * b + c * c
    }

    /*
     * Computes the squared Euclidean length of this [MotmVector3] to the specified point.
     *
     * @param  x The point x coordinate.
     * @param  y The point y coordinate.
     * @param  z The point z coordinate.
     *
     * @return double The squared Euclidean distance.
     */
    fun distanceTo2(x: Double, y: Double, z: Double): Double {
        val a = this.x - x
        val b = this.y - y
        val c = this.z - z
        return a * a + b * b + c * c
    }

    /*
     * Sets this [MotmVector3] to the absolute value of itself.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun absoluteValue(): MotmVector3 {
        x = abs(x)
        y = abs(y)
        z = abs(z)
        return this
    }

    /*
     * Projects the specified [MotmVector3] onto this one and sets this [MotmVector3]
     * to the result.
     *
     * @param v [MotmVector3] to be projected.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun project(v: MotmVector3): MotmVector3 {
        val d = dot(v)
        val theDiv = d / length2()
        return multiply(theDiv)
    }

    /*
     * Calculates the angle between this [MotmVector3] and the provided [MotmVector3].
     *
     * @param v [MotmVector3] The [MotmVector3] The [MotmVector3] to calculate the angle to.
     *
     * @return `double` The calculated angle, in degrees.
     */
    fun angle(v: MotmVector3): Double {
        var dot = dot(v)
        dot /= length() * v.length()
        return Math.toDegrees(acos(dot))
    }

    /*
     * Computes the vector dot product between this [MotmVector3] and the specified [MotmVector3].
     *
     * @param v [MotmVector3] to compute the dot product with.
     *
     * @return double The dot product.
     */
    fun dot(v: MotmVector3): Double {
        return x * v.x + y * v.y + z * v.z
    }

    /*
     * Computes the vector dot product between this [MotmVector3] and the specified vector.
     *
     * @param x double The x component of the specified vector.
     * @param y double The y component of the specified vector.
     * @param z double The z component of the specified vector.
     *
     * @return double The dot product.
     */
    fun dot(x: Double, y: Double, z: Double): Double {
        return this.x * x + this.y * y + this.z * z
    }

    /*
     * Computes the cross product between this [MotmVector3] and the specified [MotmVector3],
     * setting this to the result.
     *
     * @param v [MotmVector3] the other [MotmVector3] to cross with.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun cross(v: MotmVector3): MotmVector3 {
        if (temp == null) temp = MotmVector3()
        temp!!.setAll(this)
        x = v.y * temp!!.z - v.z * temp!!.y
        y = v.z * temp!!.x - v.x * temp!!.z
        z = v.x * temp!!.y - v.y * temp!!.x
        return this
    }

    /*
     * Computes the cross product between this [MotmVector3] and the specified vector,
     * setting this to the result.
     *
     * @param x double The x component of the other vector.
     * @param y double The y component of the other vector.
     * @param z double The z component of the other vector.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun cross(x: Double, y: Double, z: Double): MotmVector3 {
        if (temp == null) temp = MotmVector3()
        temp!!.setAll(this)
        this.x = y * temp!!.z - z * temp!!.y
        this.y = z * temp!!.x - x * temp!!.z
        this.z = x * temp!!.y - y * temp!!.x
        return this
    }

    /*
     * Computes the cross product between two [MotmVector3] objects and and sets
     * a this to the result.
     *
     * @param v1 [MotmVector3] The first [MotmVector3] to cross.
     * @param v2 [MotmVector3] The second [MotmVector3] to cross.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun crossAndSet(v1: MotmVector3, v2: MotmVector3): MotmVector3 {
        return setAll(v2.y * v1.z - v2.z * v1.y, v2.z * v1.x - v2.x * v1.z, v2.x * v1.y - v2.y * v1.x)
    }

    /*
     * Creates a {@link Quaternion} which represents the rotation from a this {@link MotmVector3}
     * to the provided {@link MotmVector3}. Adapted from OGRE 3D engine.
     *
     * @param direction {@link MotmVector3} The direction to rotate to.
     *
     * @return {@link Quaternion} The {@link Quaternion} representing the rotation.
     *  http://ogre.sourcearchive.com/documentation/1.4.5/classOgre_1_1Vector3_eeef4472ad0c4d5f34a038a9f2faa819.html#eeef4472ad0c4d5f34a038a9f2faa819
     */
    //    public Quaternion getRotationTo(MotmVector3 direction) {
    //        // Based on Stan Melax's article in Game Programming Gems
    //        Quaternion q = new Quaternion();
    //        // Copy, since cannot modify local
    //        MotmVector3 v0 = this;
    //        MotmVector3 v1 = direction;
    //        v0.normalize();
    //        v1.normalize();
    //
    //        double d = MotmVector3.dot(v0, v1);
    //        // If dot == 1, vectors are the same
    //        if (d >= 1.0f) {
    //            q.identity();
    //        }
    //        if (d < 0.000001 - 1.0) {
    //            // Generate an axis
    //            MotmVector3 axis = MotmVector3.crossAndCreate(MotmVector3.getAxisVector(Axis.X), this);
    //            if (axis.length() == 0) // pick another if colinear
    //                axis = MotmVector3.crossAndCreate(MotmVector3.getAxisVector(Axis.Y), this);
    //            axis.normalize();
    //            q.fromAngleAxis(axis, MathUtil.radiansToDegrees(MathUtil.PI));
    //        } else {
    //            double s = Math.sqrt((1 + d) * 2);
    //            double invs = 1 / s;
    //
    //            MotmVector3 c = MotmVector3.crossAndCreate(v0, v1);
    //
    //            q.x = c.x * invs;
    //            q.y = c.y * invs;
    //            q.z = c.z * invs;
    //            q.w = s * 0.5;
    //            q.normalize();
    //        }
    //        return q;
    //    }

    /*
     * Performs a linear interpolation between this [MotmVector3] and to by the specified amount.
     * The result will be stored in the current object which means that the current
     * x, y, z values will be overridden.
     *
     * @param to     [MotmVector3] Ending point.
     * @param amount double [0-1] interpolation value.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun lerp(to: MotmVector3, amount: Double): MotmVector3 {
        return multiply(1.0 - amount).add(to.x * amount, to.y * amount, to.z * amount)
    }

    /*
     * Performs a linear interpolation between from and to by the specified amount.
     * The result will be stored in the current object which means that the current
     * x, y, z values will be overridden.
     *
     * @param from   [MotmVector3] Starting point.
     * @param to     [MotmVector3] Ending point.
     * @param amount double [0-1] interpolation value.
     *
     * @return A reference to this [MotmVector3] to facilitate chaining.
     */
    fun lerpAndSet(from: MotmVector3, to: MotmVector3, amount: Double): MotmVector3 {
        x = from.x + (to.x - from.x) * amount
        y = from.y + (to.y - from.y) * amount
        z = from.z + (to.z - from.z) * amount
        return this
    }


    //--------------------------------------------------
    // Utility methods
    //--------------------------------------------------

    /*
     * Clones this [MotmVector3].
     *
     * @return [MotmVector3] A copy of this [MotmVector3].
     */
    fun clone(): MotmVector3 {
        return MotmVector3(x, y, z)
    }

    /*
     * Checks if this [MotmVector3] is of unit length with a specified
     * margin of error.
     *
     * @param margin double The desired margin of error for the test.
     *
     * @return boolean True if this [MotmVector3] is of unit length.
     */
    private fun isUnit(margin: Double): Boolean {
        return abs(length2() - 1) < margin * margin
    }

    /*
     * Checks if the length of this [MotmVector3] is smaller than the specified margin.
     *
     * @param margin double The desired margin of error for the test.
     *
     * @return boolean True if this [MotmVector3]'s length is smaller than the margin specified.
     */
    fun isZero(margin: Double): Boolean {
        return length2() < margin * margin
    }

    /*
     * Does a component by component comparison of this [MotmVector3] and the specified [MotmVector3]
     * and returns the result.
     *
     * @param obj [MotmVector3] to compare with this one.
     *
     * @return boolean True if this [MotmVector3]'s components match with the components of the input.
     */
    /*fun equals(obj: MotmVector3): Boolean {
        return obj.x == x && obj.y == y && obj.z == z
    }*/

    /*
     * Does a component by component comparison of this [MotmVector3] and the specified [MotmVector3]
     * with an error parameter and returns the result.
     *
     * @param obj [MotmVector3] to compare with this one.
     * @param error `double` The maximum allowable difference to be considered equal.
     *
     * @return boolean True if this [MotmVector3]'s components match with the components of the input.
     */
    fun equals(obj: MotmVector3, error: Double): Boolean {
        return abs(obj.x - x) <= error && abs(obj.y - y) <= error && abs(obj.z - z) <= error
    }

    /*
     * Fills x, y, z values into first three positions in the
     * supplied array, if it is large enough to accommodate
     * them.
     *
     * @param array The array to be populated
     *
     * @return The passed array with the xyz values inserted
     */
    @JvmOverloads
    fun toArray(array: DoubleArray? = DoubleArray(3)): DoubleArray? {
        if (array != null && array.size >= 3) {
            array[0] = x
            array[1] = y
            array[2] = z
        }

        return array
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("MotmVector3 <x, y, z>: <")
                .append(x)
                .append(", ")
                .append(y)
                .append(", ")
                .append(z)
                .append(">")
        return sb.toString()
    }

    companion object {

        //Unit vectors oriented to each axis
        //DO NOT EVER MODIFY THE VALUES OF THESE MEMBERS
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val X = MotmVector3(1.0, 0.0, 0.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val Y = MotmVector3(0.0, 1.0, 0.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val Z = MotmVector3(0.0, 0.0, 1.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val NEG_X = MotmVector3(-1.0, 0.0, 0.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val NEG_Y = MotmVector3(0.0, -1.0, 0.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val NEG_Z = MotmVector3(0.0, 0.0, -1.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val ZERO = MotmVector3(0.0, 0.0, 0.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val ONE = MotmVector3(1.0, 1.0, 1.0)

        /*
         * Adds two input [MotmVector3] objects and creates a new one to hold the result.
         *
         * @param a [MotmVector3] The first vector.
         * @param b [MotmVector3] The second vector.
         *
         * @return [MotmVector3] The resulting [MotmVector3].
         */
        fun addAndCreate(a: MotmVector3, b: MotmVector3): MotmVector3 {
            return MotmVector3(a.x + b.x, a.y + b.y, a.z + b.z)
        }

        /*
         * Subtracts two input [MotmVector3] objects and creates a new one to hold the result.
         *
         * @param a [MotmVector3] The first vector.
         * @param b [MotmVector3] The second vector
         *
         * @return [MotmVector3] The resulting [MotmVector3].
         */
        fun subtractAndCreate(a: MotmVector3, b: MotmVector3): MotmVector3 {
            return MotmVector3(a.x - b.x, a.y - b.y, a.z - b.z)
        }

        /*
         * Multiplies two input [MotmVector3] objects and creates a new one to hold the result.
         *
         * @param a [MotmVector3] The first vector.
         * @param b [MotmVector3] The second vector
         *
         * @return [MotmVector3] The resulting [MotmVector3].
         */
        fun multiplyAndCreate(a: MotmVector3, b: MotmVector3): MotmVector3 {
            return MotmVector3(a.x * b.x, a.y * b.y, a.z * b.z)
        }

        /*
         * Scales each component of this [MotmVector3] by the specified value and creates a new one to hold the result.
         *
         * @param a     [MotmVector3] The first vector.
         * @param value double The value to scale each component by.
         *
         * @return [MotmVector3] The resulting [MotmVector3].
         */
        fun multiplyAndCreate(a: MotmVector3, value: Double): MotmVector3 {
            return MotmVector3(a.x * value, a.y * value, a.z * value)
        }

        /*
         * Scales an input [MotmVector3] by a value and creates a new one to hold the result.
         *
         * @param a [MotmVector3] The [MotmVector3] to scale.
         * @param b [MotmVector3] The scaling factor.
         *
         * @return [MotmVector3] The resulting [MotmVector3].
         */
        fun scaleAndCreate(a: MotmVector3, b: Double): MotmVector3 {
            return MotmVector3(a.x * b, a.y * b, a.z * b)
        }

        /*
         * Applies Gram-Schmitt Ortho-normalization to the given set of input [MotmVector3] objects.
         *
         * @param vecs Array of [MotmVector3] objects to be ortho-normalized.
         */
        fun orthoNormalize(vecs: Array<MotmVector3>) {
            for (i in vecs.indices) {
                vecs[i].normalize()
                for (j in i + 1 until vecs.size) {
                    vecs[j].subtract(projectAndCreate(vecs[j], vecs[i]))
                }
            }
        }

        /*
         * Efficient Gram-Schmitt Ortho-normalization for the special case of 2 vectors.
         *
         * @param v1 The first [MotmVector3] object to be ortho-normalized.
         * @param v2 The second [MotmVector3]. [MotmVector3] object to be ortho-normalized.
         */
        fun orthoNormalize(v1: MotmVector3, v2: MotmVector3) {
            v1.normalize()
            v2.subtract(projectAndCreate(v2, v1))
            v2.normalize()
        }


        //--------------------------------------------------
        // Vector operation methods
        //--------------------------------------------------

        /*
         * Computes the Euclidean length of the arbitrary vector components passed in.
         *
         * @param x double The x component.
         * @param y double The y component.
         * @param z double The z component.
         *
         * @return double The Euclidean length.
         */
        fun length(x: Double, y: Double, z: Double): Double {
            return sqrt(length2(x, y, z))
        }

        /*
         * Computes the Euclidean length of the arbitrary vector components passed in.
         *
         * @param v [MotmVector3] The [MotmVector3] to calculate the length of.
         *
         * @return double The Euclidean length.
         */
        fun length(v: MotmVector3): Double {
            return length(v.x, v.y, v.z)
        }

        /*
         * Computes the squared Euclidean length of the arbitrary vector components passed in.
         *
         * @param v [MotmVector3] The [MotmVector3] to calculate the length of.
         *
         * @return double The squared Euclidean length.
         */
        fun length2(v: MotmVector3): Double {
            return length2(v.x, v.y, v.z)
        }

        /*
         * Computes the squared Euclidean length of the arbitrary vector components passed in.
         *
         * @param x double The x component.
         * @param y double The y component.
         * @param z double The z component.
         *
         * @return double The squared Euclidean length.
         */
        private fun length2(x: Double, y: Double, z: Double): Double {
            return x * x + y * y + z * z
        }

        /*
         * Computes the Euclidean length between two [MotmVector3] objects.
         *
         * @param v1 [MotmVector3] The first vector.
         * @param v2 [MotmVector3] The second vector.
         *
         * @return double The Euclidean distance.
         */
        fun distanceTo(v1: MotmVector3, v2: MotmVector3): Double {
            val a = v1.x - v2.x
            val b = v1.y - v2.y
            val c = v1.z - v2.z
            return sqrt(a * a + b * b + c * c)
        }

        /*
         * Computes the Euclidean length between two points.
         *
         * @return double The Euclidean distance.
         */
        fun distanceTo(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double {
            val a = x1 - x2
            val b = y1 - y2
            val c = z1 - z2
            return sqrt(a * a + b * b + c * c)
        }

        /*
         * Computes the squared Euclidean length between two [MotmVector3] objects.
         *
         * @param v1 [MotmVector3] The first vector.
         * @param v2 [MotmVector3] The second vector.
         *
         * @return double The squared Euclidean distance.
         */
        fun distanceTo2(v1: MotmVector3, v2: MotmVector3): Double {
            val a = v1.x - v2.x
            val b = v1.y - v2.y
            val c = v1.z - v2.z
            return a * a + b * b + c * c
        }

        /*
         * Computes the squared Euclidean length between two points.
         *
         * @return double The squared Euclidean distance.
         */
        fun distanceTo2(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double {
            val a = x1 - x2
            val b = y1 - y2
            val c = z1 - z2
            return a * a + b * b + c * c
        }

        /*
         * Projects [MotmVector3] v1 onto [MotmVector3] v2 and creates a new
         * [MotmVector3] for the result.
         *
         * @param v1 [MotmVector3] to be projected.
         * @param v2 [MotmVector3] the [MotmVector3] to be projected on.
         *
         * @return [MotmVector3] The result of the projection.
         */
        private fun projectAndCreate(v1: MotmVector3, v2: MotmVector3): MotmVector3 {
            val d = v1.dot(v2)
            val theDiv = d / v2.length2()
            return v2.clone().multiply(theDiv)
        }

        /*
         * Computes the vector dot product between the two specified [MotmVector3] objects.
         *
         * @param v1 The first [MotmVector3].
         * @param v2 The second [MotmVector3].
         *
         * @return double The dot product.
         */
        fun dot(v1: MotmVector3, v2: MotmVector3): Double {
            return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z
        }

        /*
         * Computes the vector dot product between the components of the two supplied vectors.
         *
         * @param x1 double The x component of the first vector.
         * @param y1 double The y component of the first vector.
         * @param z1 double The z component of the first vector.
         * @param x2 double The x component of the second vector.
         * @param y2 double The y component of the second vector.
         * @param z2 double The z component of the second vector.
         *
         * @return double The dot product.
         */
        fun dot(x1: Double, y1: Double, z1: Double,
                x2: Double, y2: Double, z2: Double): Double {
            return x1 * x2 + y1 * y2 + z1 * z2
        }

        /*
         * Computes the cross product between two [MotmVector3] objects and and sets
         * a new [MotmVector3] to the result.
         *
         * @param v1 [MotmVector3] The first [MotmVector3] to cross.
         * @param v2 [MotmVector3] The second [MotmVector3] to cross.
         *
         * @return [MotmVector3] The computed cross product.
         */
        fun crossAndCreate(v1: MotmVector3, v2: MotmVector3): MotmVector3 {
            return MotmVector3(v2.y * v1.z - v2.z * v1.y, v2.z * v1.x - v2.x * v1.z, v2.x * v1.y - v2.y * v1.x)
        }

        /*
         * Performs a linear interpolation between from and to by the specified amount.
         * The result will be stored in a new [MotmVector3] object.
         *
         * @param from   [MotmVector3] Starting point.
         * @param to     [MotmVector3] Ending point.
         * @param amount double [0-1] interpolation value.
         *
         * @return [MotmVector3] The interpolated value.
         */
        fun lerpAndCreate(from: MotmVector3, to: MotmVector3, amount: Double): MotmVector3 {
            val out = MotmVector3()
            out.x = from.x + (to.x - from.x) * amount
            out.y = from.y + (to.y - from.y) * amount
            out.z = from.z + (to.z - from.z) * amount
            return out
        }

        /*
         * Determines and returns the [MotmVector3] pointing along the
         * specified axis.
         * DO NOT MODIFY THE VALUES OF THE RETURNED VECTORS. DOING SO WILL HAVE
         * DRAMATICALLY UNDESIRED CONSEQUENCES.
         *
         * @param axis [Axis] the axis to find.
         *
         * @return [MotmVector3] the [MotmVector3] representing the requested axis.
         */
        fun getAxisVector(axis: Axis): MotmVector3 {
            return when (axis) {
                Axis.X -> X
                Axis.Y -> Y
                Axis.Z -> Z
            }
        }
    }
}
/*
 * Returns an array representation of this MotmVector3.
 *
 * @return An array containing this MotmVector3's xyz values.
 */
