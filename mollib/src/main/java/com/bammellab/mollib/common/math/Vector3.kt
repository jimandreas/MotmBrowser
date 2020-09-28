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
 * @see [libGDX->Vector3](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Vector3.java)
 *
 *
 * This class is not thread safe and must be confined to a single thread or by
 * some external locking mechanism if necessary. All static methods are thread safe.
 */
class Vector3 {
    //The vector components
    var x: Double = 0.toDouble()
    var y: Double = 0.toDouble()
    var z: Double = 0.toDouble()

    //Scratch vector. We use lazy loading here.
    private var mTemp: Vector3? = null

    /*
     * Checks if this [Vector3] is of unit length with a default
     * margin of error of 1e-8.
     *
     * @return boolean True if this [Vector3] is of unit length.
     */
    val isUnit: Boolean
        get() = isUnit(1e-8)

    /*
     * Checks if this [Vector3] is a zero vector.
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
     * Constructs a new [Vector3] at (0, 0, 0).
     */
    constructor() {
        //They are technically zero, but we wont rely on the uninitialized state here.
        x = 0.0
        y = 0.0
        z = 0.0
    }

    /*
     * Constructs a new [Vector3] at <from></from>, from, from>,>.
     *
     * @param from double which all components will be initialized to.
     */
    constructor(from: Double) {
        x = from
        y = from
        z = from
    }

    /*
     * Constructs a new [Vector3] with components matching the input [Vector3].
     *
     * @param from [Vector3] to initialize the components with.
     */
    constructor(from: Vector3) {
        x = from.x
        y = from.y
        z = from.z
    }

    /*
     * Constructs a new {@link Vector3} with components initialized from the input {@link String} array.
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
     * Constructs a new {@link Vector3} with components initialized from the input double array.
     *
     * @param values A double array of values to be parsed for each component.
     *
     * @throws {@link IllegalArgumentException} if there are fewer than 3 values in the array.
     */
    @Throws(IllegalArgumentException::class)
    constructor(values: DoubleArray) {
        if (values.size < 3) throw IllegalArgumentException("Vector3 must be initialized with an array length of at least 3.")
        x = values[0]
        y = values[1]
        z = values[2]
    }

    /*
     * Constructs a new [Vector3] object with components initialized to the specified values.
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
     * Sets all components of this [Vector3] to the specified values.
     *
     * @param x double The x component.
     * @param y double The y component.
     * @param z double The z component.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun setAll(x: Double, y: Double, z: Double): Vector3 {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    /*
     * Sets all components of this [Vector3] to the values provided
     * by the input [Vector3].
     *
     * @param other [Vector3] The vector to copy.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun setAll(other: Vector3): Vector3 {
        x = other.x
        y = other.y
        z = other.z
        return this
    }

    /*
     * Sets all components of this [Vector3] to the values provided representing
     * the input [Axis].
     *
     * @param axis [Axis] The cardinal axis to set the values to.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun setAll(axis: Axis): Vector3 {
        return setAll(getAxisVector(axis))
    }

    /*
     * Adds the provided [Vector3] to this one.
     *
     * @param v [Vector3] to be added to this one.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun add(v: Vector3): Vector3 {
        x += v.x
        y += v.y
        z += v.z
        return this
    }

    /*
     * Adds the given values to the respective components of this [Vector3].
     *
     * @param x The value to add to the x component.
     * @param y The value to add to the y component.
     * @param z The value to add to the z component.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun add(x: Double, y: Double, z: Double): Vector3 {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    /*
     * Adds the given value to each component of this [Vector3].
     *
     * @param value double value to add.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun add(value: Double): Vector3 {
        x += value
        y += value
        z += value
        return this
    }

    /*
     * Adds two input [Vector3] objects and sets this one to the result.
     *
     * @param a [Vector3] The first vector.
     * @param b [Vector3] The second vector.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun addAndSet(a: Vector3, b: Vector3): Vector3 {
        x = a.x + b.x
        y = a.y + b.y
        z = a.z + b.z
        return this
    }

    /*
     * Subtracts the provided [Vector3] from this one.
     *
     * @param v [Vector3] to be subtracted from this one.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun subtract(v: Vector3): Vector3 {
        x -= v.x
        y -= v.y
        z -= v.z
        return this
    }

    /*
     * Subtracts the given values from the respective components of this [Vector3].
     *
     * @param x The value to subtract to the x component.
     * @param y The value to subtract to the y component.
     * @param z The value to subtract to the z component.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun subtract(x: Double, y: Double, z: Double): Vector3 {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    /*
     * Subtracts the given value from each component of this [Vector3].
     *
     * @param value double value to subtract.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun subtract(value: Double): Vector3 {
        x -= value
        y -= value
        z -= value
        return this
    }

    /*
     * Subtracts two input [Vector3] objects and sets this one to the result.
     *
     * @param a [Vector3] The first vector.
     * @param b [Vector3] The second vector.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun subtractAndSet(a: Vector3, b: Vector3): Vector3 {
        x = a.x - b.x
        y = a.y - b.y
        z = a.z - b.z
        return this
    }

    /*
     * Scales each component of this [Vector3] by the specified value.
     *
     * @param value double The value to scale each component by.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun multiply(value: Double): Vector3 {
        x *= value
        y *= value
        z *= value
        return this
    }

    /*
     * Scales each component of this [Vector3] by the corresponding components
     * of the provided [Vector3].
     *
     * @param v [Vector3] containing the values to scale by.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun multiply(v: Vector3): Vector3 {
        x *= v.x
        y *= v.y
        z *= v.z
        return this
    }


    /*
     * Multiplies two input [Vector3] objects and sets this one to the result.
     *
     * @param a [Vector3] The first vector.
     * @param b [Vector3] The second vector.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun multiplyAndSet(a: Vector3, b: Vector3): Vector3 {
        x = a.x * b.x
        y = a.y * b.y
        z = a.z * b.z
        return this
    }

    /*
     * Divide each component of this [Vector3] by the specified value.
     *
     * @param value double The value to divide each component by.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun divide(value: Double): Vector3 {
        x /= value
        y /= value
        z /= value
        return this
    }

    /*
     * Divides each component of this [Vector3] by the corresponding components
     * of the provided [Vector3].
     *
     * @param v [Vector3] containing the values to divide by.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun divide(v: Vector3): Vector3 {
        x /= v.x
        y /= v.y
        z /= v.z
        return this
    }

    /*
     * Divides two input [Vector3] objects and sets this one to the result.
     *
     * @param a [Vector3] The first vector.
     * @param b [Vector3] The second vector.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun divideAndSet(a: Vector3, b: Vector3): Vector3 {
        x = a.x / b.x
        y = a.y / b.y
        z = a.z / b.z
        return this
    }

    /*
     * Scales an input [Vector3] by a value and sets this one to the result.
     *
     * @param a [Vector3] The [Vector3] to scale.
     * @param b [Vector3] The scaling factor.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun scaleAndSet(a: Vector3, b: Double): Vector3 {
        x = a.x * b
        y = a.y * b
        z = a.z * b
        return this
    }

    /*
     * Rotates this [Vector3] about the X axis by the angle specified.
     *
     * @param angle double The angle to rotate by in radians.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun rotateX(angle: Double): Vector3 {
        val cosRY = cos(angle)
        val sinRY = sin(angle)
        if (mTemp == null) mTemp = Vector3()
        mTemp!!.setAll(x, y, z)
        y = mTemp!!.y * cosRY - mTemp!!.z * sinRY
        z = mTemp!!.y * sinRY + mTemp!!.z * cosRY
        return this
    }

    /*
     * Rotates this [Vector3] about the Y axis by the angle specified.
     *
     * @param angle double The angle to rotate by in radians.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun rotateY(angle: Double): Vector3 {
        val cosRY = cos(angle)
        val sinRY = sin(angle)
        if (mTemp == null) mTemp = Vector3()
        mTemp!!.setAll(x, y, z)
        x = mTemp!!.x * cosRY + mTemp!!.z * sinRY
        z = mTemp!!.x * -sinRY + mTemp!!.z * cosRY
        return this
    }

    /*
     * Rotates this [Vector3] about the Z axis by the angle specified.
     *
     * @param angle double The angle to rotate by in radians.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun rotateZ(angle: Double): Vector3 {
        val cosRY = cos(angle)
        val sinRY = sin(angle)
        if (mTemp == null) mTemp = Vector3()
        mTemp!!.setAll(x, y, z)
        x = mTemp!!.x * cosRY - mTemp!!.y * sinRY
        y = mTemp!!.x * sinRY + mTemp!!.y * cosRY
        return this
    }

    /*
     * Normalize this [Vector3] to unit length.
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
     * Inverts the direction of this [Vector3].
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun inverse(): Vector3 {
        x = -x
        y = -y
        z = -z
        return this
    }

    /*
     * Inverts the direction of this [Vector3] and creates a new one set with the result.
     *
     * @return [Vector3] The resulting [Vector3].
     */
    fun invertAndCreate(): Vector3 {
        return Vector3(-x, -y, -z)
    }

    /*
     * Computes the Euclidean length of this [Vector3];
     *
     * @return double The Euclidean length.
     */
    fun length(): Double {
        return length(this)
    }

    /*
     * Computes the squared Euclidean length of this [Vector3];
     *
     * @return double The squared Euclidean length.
     */
    fun length2(): Double {
        return x * x + y * y + z * z
    }

    /*
     * Computes the Euclidean length of this [Vector3] to the specified [Vector3].
     *
     * @param other [Vector3] The [Vector3] to compute the distance to.
     *
     * @return double The Euclidean distance.
     */
    fun distanceTo(other: Vector3): Double {
        val a = x - other.x
        val b = y - other.y
        val c = z - other.z
        return sqrt(a * a + b * b + c * c)
    }

    /*
     * Computes the Euclidean length of this [Vector3] to the specified point.
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
     * Computes the squared Euclidean length of this [Vector3] to the specified [Vector3].
     *
     * @param other [Vector3] The [Vector3] to compute the distance to.
     *
     * @return double The squared Euclidean distance.
     */
    fun distanceTo2(other: Vector3): Double {
        val a = x - other.x
        val b = y - other.y
        val c = z - other.z
        return a * a + b * b + c * c
    }

    /*
     * Computes the squared Euclidean length of this [Vector3] to the specified point.
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
     * Sets this [Vector3] to the absolute value of itself.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun absoluteValue(): Vector3 {
        x = abs(x)
        y = abs(y)
        z = abs(z)
        return this
    }

    /*
     * Projects the specified [Vector3] onto this one and sets this [Vector3]
     * to the result.
     *
     * @param v [Vector3] to be projected.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun project(v: Vector3): Vector3 {
        val d = dot(v)
        val theDiv = d / length2()
        return multiply(theDiv)
    }

    /*
     * Calculates the angle between this [Vector3] and the provided [Vector3].
     *
     * @param v [Vector3] The [Vector3] The [Vector3] to calculate the angle to.
     *
     * @return `double` The calculated angle, in degrees.
     */
    fun angle(v: Vector3): Double {
        var dot = dot(v)
        dot /= length() * v.length()
        return Math.toDegrees(acos(dot))
    }

    /*
     * Computes the vector dot product between this [Vector3] and the specified [Vector3].
     *
     * @param v [Vector3] to compute the dot product with.
     *
     * @return double The dot product.
     */
    fun dot(v: Vector3): Double {
        return x * v.x + y * v.y + z * v.z
    }

    /*
     * Computes the vector dot product between this [Vector3] and the specified vector.
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
     * Computes the cross product between this [Vector3] and the specified [Vector3],
     * setting this to the result.
     *
     * @param v [Vector3] the other [Vector3] to cross with.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun cross(v: Vector3): Vector3 {
        if (mTemp == null) mTemp = Vector3()
        mTemp!!.setAll(this)
        x = v.y * mTemp!!.z - v.z * mTemp!!.y
        y = v.z * mTemp!!.x - v.x * mTemp!!.z
        z = v.x * mTemp!!.y - v.y * mTemp!!.x
        return this
    }

    /*
     * Computes the cross product between this [Vector3] and the specified vector,
     * setting this to the result.
     *
     * @param x double The x component of the other vector.
     * @param y double The y component of the other vector.
     * @param z double The z component of the other vector.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun cross(x: Double, y: Double, z: Double): Vector3 {
        if (mTemp == null) mTemp = Vector3()
        mTemp!!.setAll(this)
        this.x = y * mTemp!!.z - z * mTemp!!.y
        this.y = z * mTemp!!.x - x * mTemp!!.z
        this.z = x * mTemp!!.y - y * mTemp!!.x
        return this
    }

    /*
     * Computes the cross product between two [Vector3] objects and and sets
     * a this to the result.
     *
     * @param v1 [Vector3] The first [Vector3] to cross.
     * @param v2 [Vector3] The second [Vector3] to cross.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun crossAndSet(v1: Vector3, v2: Vector3): Vector3 {
        return setAll(v2.y * v1.z - v2.z * v1.y, v2.z * v1.x - v2.x * v1.z, v2.x * v1.y - v2.y * v1.x)
    }

    /*
     * Creates a {@link Quaternion} which represents the rotation from a this {@link Vector3}
     * to the provided {@link Vector3}. Adapted from OGRE 3D engine.
     *
     * @param direction {@link Vector3} The direction to rotate to.
     *
     * @return {@link Quaternion} The {@link Quaternion} representing the rotation.
     *  http://ogre.sourcearchive.com/documentation/1.4.5/classOgre_1_1Vector3_eeef4472ad0c4d5f34a038a9f2faa819.html#eeef4472ad0c4d5f34a038a9f2faa819
     */
    //    public Quaternion getRotationTo(Vector3 direction) {
    //        // Based on Stan Melax's article in Game Programming Gems
    //        Quaternion q = new Quaternion();
    //        // Copy, since cannot modify local
    //        Vector3 v0 = this;
    //        Vector3 v1 = direction;
    //        v0.normalize();
    //        v1.normalize();
    //
    //        double d = Vector3.dot(v0, v1);
    //        // If dot == 1, vectors are the same
    //        if (d >= 1.0f) {
    //            q.identity();
    //        }
    //        if (d < 0.000001 - 1.0) {
    //            // Generate an axis
    //            Vector3 axis = Vector3.crossAndCreate(Vector3.getAxisVector(Axis.X), this);
    //            if (axis.length() == 0) // pick another if colinear
    //                axis = Vector3.crossAndCreate(Vector3.getAxisVector(Axis.Y), this);
    //            axis.normalize();
    //            q.fromAngleAxis(axis, MathUtil.radiansToDegrees(MathUtil.PI));
    //        } else {
    //            double s = Math.sqrt((1 + d) * 2);
    //            double invs = 1 / s;
    //
    //            Vector3 c = Vector3.crossAndCreate(v0, v1);
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
     * Performs a linear interpolation between this [Vector3] and to by the specified amount.
     * The result will be stored in the current object which means that the current
     * x, y, z values will be overridden.
     *
     * @param to     [Vector3] Ending point.
     * @param amount double [0-1] interpolation value.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun lerp(to: Vector3, amount: Double): Vector3 {
        return multiply(1.0 - amount).add(to.x * amount, to.y * amount, to.z * amount)
    }

    /*
     * Performs a linear interpolation between from and to by the specified amount.
     * The result will be stored in the current object which means that the current
     * x, y, z values will be overridden.
     *
     * @param from   [Vector3] Starting point.
     * @param to     [Vector3] Ending point.
     * @param amount double [0-1] interpolation value.
     *
     * @return A reference to this [Vector3] to facilitate chaining.
     */
    fun lerpAndSet(from: Vector3, to: Vector3, amount: Double): Vector3 {
        x = from.x + (to.x - from.x) * amount
        y = from.y + (to.y - from.y) * amount
        z = from.z + (to.z - from.z) * amount
        return this
    }


    //--------------------------------------------------
    // Utility methods
    //--------------------------------------------------

    /*
     * Clones this [Vector3].
     *
     * @return [Vector3] A copy of this [Vector3].
     */
    fun clone(): Vector3 {
        return Vector3(x, y, z)
    }

    /*
     * Checks if this [Vector3] is of unit length with a specified
     * margin of error.
     *
     * @param margin double The desired margin of error for the test.
     *
     * @return boolean True if this [Vector3] is of unit length.
     */
    private fun isUnit(margin: Double): Boolean {
        return abs(length2() - 1) < margin * margin
    }

    /*
     * Checks if the length of this [Vector3] is smaller than the specified margin.
     *
     * @param margin double The desired margin of error for the test.
     *
     * @return boolean True if this [Vector3]'s length is smaller than the margin specified.
     */
    fun isZero(margin: Double): Boolean {
        return length2() < margin * margin
    }

    /*
     * Does a component by component comparison of this [Vector3] and the specified [Vector3]
     * and returns the result.
     *
     * @param obj [Vector3] to compare with this one.
     *
     * @return boolean True if this [Vector3]'s components match with the components of the input.
     */
    /*fun equals(obj: Vector3): Boolean {
        return obj.x == x && obj.y == y && obj.z == z
    }*/

    /*
     * Does a component by component comparison of this [Vector3] and the specified [Vector3]
     * with an error parameter and returns the result.
     *
     * @param obj [Vector3] to compare with this one.
     * @param error `double` The maximum allowable difference to be considered equal.
     *
     * @return boolean True if this [Vector3]'s components match with the components of the input.
     */
    fun equals(obj: Vector3, error: Double): Boolean {
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
        sb.append("Vector3 <x, y, z>: <")
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
        val X = Vector3(1.0, 0.0, 0.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val Y = Vector3(0.0, 1.0, 0.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val Z = Vector3(0.0, 0.0, 1.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val NEG_X = Vector3(-1.0, 0.0, 0.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val NEG_Y = Vector3(0.0, -1.0, 0.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val NEG_Z = Vector3(0.0, 0.0, -1.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val ZERO = Vector3(0.0, 0.0, 0.0)
        /*
         * DO NOT EVER MODIFY THE VALUES OF THIS VECTOR
         */
        val ONE = Vector3(1.0, 1.0, 1.0)

        /*
         * Adds two input [Vector3] objects and creates a new one to hold the result.
         *
         * @param a [Vector3] The first vector.
         * @param b [Vector3] The second vector.
         *
         * @return [Vector3] The resulting [Vector3].
         */
        fun addAndCreate(a: Vector3, b: Vector3): Vector3 {
            return Vector3(a.x + b.x, a.y + b.y, a.z + b.z)
        }

        /*
         * Subtracts two input [Vector3] objects and creates a new one to hold the result.
         *
         * @param a [Vector3] The first vector.
         * @param b [Vector3] The second vector
         *
         * @return [Vector3] The resulting [Vector3].
         */
        fun subtractAndCreate(a: Vector3, b: Vector3): Vector3 {
            return Vector3(a.x - b.x, a.y - b.y, a.z - b.z)
        }

        /*
         * Multiplies two input [Vector3] objects and creates a new one to hold the result.
         *
         * @param a [Vector3] The first vector.
         * @param b [Vector3] The second vector
         *
         * @return [Vector3] The resulting [Vector3].
         */
        fun multiplyAndCreate(a: Vector3, b: Vector3): Vector3 {
            return Vector3(a.x * b.x, a.y * b.y, a.z * b.z)
        }

        /*
         * Scales each component of this [Vector3] by the specified value and creates a new one to hold the result.
         *
         * @param a     [Vector3] The first vector.
         * @param value double The value to scale each component by.
         *
         * @return [Vector3] The resulting [Vector3].
         */
        fun multiplyAndCreate(a: Vector3, value: Double): Vector3 {
            return Vector3(a.x * value, a.y * value, a.z * value)
        }

        /*
         * Scales an input [Vector3] by a value and creates a new one to hold the result.
         *
         * @param a [Vector3] The [Vector3] to scale.
         * @param b [Vector3] The scaling factor.
         *
         * @return [Vector3] The resulting [Vector3].
         */
        fun scaleAndCreate(a: Vector3, b: Double): Vector3 {
            return Vector3(a.x * b, a.y * b, a.z * b)
        }

        /*
         * Applies Gram-Schmitt Ortho-normalization to the given set of input [Vector3] objects.
         *
         * @param vecs Array of [Vector3] objects to be ortho-normalized.
         */
        fun orthoNormalize(vecs: Array<Vector3>) {
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
         * @param v1 The first [Vector3] object to be ortho-normalized.
         * @param v2 The second [Vector3]. [Vector3] object to be ortho-normalized.
         */
        fun orthoNormalize(v1: Vector3, v2: Vector3) {
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
         * @param v [Vector3] The [Vector3] to calculate the length of.
         *
         * @return double The Euclidean length.
         */
        fun length(v: Vector3): Double {
            return length(v.x, v.y, v.z)
        }

        /*
         * Computes the squared Euclidean length of the arbitrary vector components passed in.
         *
         * @param v [Vector3] The [Vector3] to calculate the length of.
         *
         * @return double The squared Euclidean length.
         */
        fun length2(v: Vector3): Double {
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
         * Computes the Euclidean length between two [Vector3] objects.
         *
         * @param v1 [Vector3] The first vector.
         * @param v2 [Vector3] The second vector.
         *
         * @return double The Euclidean distance.
         */
        fun distanceTo(v1: Vector3, v2: Vector3): Double {
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
         * Computes the squared Euclidean length between two [Vector3] objects.
         *
         * @param v1 [Vector3] The first vector.
         * @param v2 [Vector3] The second vector.
         *
         * @return double The squared Euclidean distance.
         */
        fun distanceTo2(v1: Vector3, v2: Vector3): Double {
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
         * Projects [Vector3] v1 onto [Vector3] v2 and creates a new
         * [Vector3] for the result.
         *
         * @param v1 [Vector3] to be projected.
         * @param v2 [Vector3] the [Vector3] to be projected on.
         *
         * @return [Vector3] The result of the projection.
         */
        private fun projectAndCreate(v1: Vector3, v2: Vector3): Vector3 {
            val d = v1.dot(v2)
            val theDiv = d / v2.length2()
            return v2.clone().multiply(theDiv)
        }

        /*
         * Computes the vector dot product between the two specified [Vector3] objects.
         *
         * @param v1 The first [Vector3].
         * @param v2 The second [Vector3].
         *
         * @return double The dot product.
         */
        fun dot(v1: Vector3, v2: Vector3): Double {
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
         * Computes the cross product between two [Vector3] objects and and sets
         * a new [Vector3] to the result.
         *
         * @param v1 [Vector3] The first [Vector3] to cross.
         * @param v2 [Vector3] The second [Vector3] to cross.
         *
         * @return [Vector3] The computed cross product.
         */
        fun crossAndCreate(v1: Vector3, v2: Vector3): Vector3 {
            return Vector3(v2.y * v1.z - v2.z * v1.y, v2.z * v1.x - v2.x * v1.z, v2.x * v1.y - v2.y * v1.x)
        }

        /*
         * Performs a linear interpolation between from and to by the specified amount.
         * The result will be stored in a new [Vector3] object.
         *
         * @param from   [Vector3] Starting point.
         * @param to     [Vector3] Ending point.
         * @param amount double [0-1] interpolation value.
         *
         * @return [Vector3] The interpolated value.
         */
        fun lerpAndCreate(from: Vector3, to: Vector3, amount: Double): Vector3 {
            val out = Vector3()
            out.x = from.x + (to.x - from.x) * amount
            out.y = from.y + (to.y - from.y) * amount
            out.z = from.z + (to.z - from.z) * amount
            return out
        }

        /*
         * Determines and returns the [Vector3] pointing along the
         * specified axis.
         * DO NOT MODIFY THE VALUES OF THE RETURNED VECTORS. DOING SO WILL HAVE
         * DRAMATICALLY UNDESIRED CONSEQUENCES.
         *
         * @param axis [Axis] the axis to find.
         *
         * @return [Vector3] the [Vector3] representing the requested axis.
         */
        fun getAxisVector(axis: Axis): Vector3 {
            return when (axis) {
                Axis.X -> X
                Axis.Y -> Y
                Axis.Z -> Z
            }
        }
    }
}
/*
 * Returns an array representation of this Vector3.
 *
 * @return An array containing this Vector3's xyz values.
 */
