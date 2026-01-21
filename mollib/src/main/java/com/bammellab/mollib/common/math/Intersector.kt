/*
 * Copyright 2013 Dennis Ippel
 * Copyright 2013 Jim Andreas kotlin conversion
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
@file:Suppress(
        "unused",
        "unused_variable",
        "unused_parameter",
        "deprecation",
        "ConstantConditionIf",
        "LocalVariableName",
        "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "AssignedValueIsNeverRead", "VariableNeverRead",
    "JoinDeclarationAndAssignment"
)

package com.bammellab.mollib.common.math

import android.annotation.SuppressLint

import timber.log.Timber
import kotlin.math.sqrt

/** Class offering various static methods for intersection testing between different geometric objects.
 *
 * Originally written by Badlogic Games. Ported for Rajawali by Andrew Jo.
 *
 * @author badlogicgames@gmail.com
 * @author jan.stria
 * @author andrewjo@gmail.com
 *
 * (removed triangle and plane - only need intersectRaySphere() - jra)
 */
object Intersector {
    private val v0 = MotmVector3()
    private val v1 = MotmVector3()
    private val v2 = MotmVector3()


    @Suppress("UNUSED_VALUE")
            /**
             * Intersects a ray defined by the start and end point and a sphere, returning the intersection point in intersection.
             * @param rayStartIn Startpoint of the ray
             * @param rayEndIn Endpoint of the ray
             * @param sphereCenterIn The center of the sphere
             * @param sphereRadius The radius of the sphere
             * @param hitPointIn The intersection point (optional)
             * @return True if there is an intersection, false otherwise.
             */
    fun intersectRaySphere(
            rayStartIn: MotmVector3, rayEndIn: MotmVector3,
            sphereCenterIn: MotmVector3, sphereRadius: Float,
            hitPointIn: MotmVector3): Boolean {
        var rayStart = rayStartIn
        var rayEnd = rayEndIn
        var sphereCenter = sphereCenterIn
        var hitPoint = hitPointIn

        rayStart = MotmVector3(rayStart)
        rayEnd = MotmVector3(rayEnd)
        val dir = MotmVector3.subtractAndCreate(rayEnd, rayStart)
        dir.normalize()

        sphereCenter = MotmVector3(sphereCenter)
        val radius2 = sphereRadius * sphereRadius

        /*
		 * Refer to http://paulbourke.net/geometry/circlesphere/ for mathematics
		 * behind ray-sphere intersection.
		 */
        val a = MotmVector3.dot(dir, dir)
        val b = 2.0f * MotmVector3.dot(dir, MotmVector3.subtractAndCreate(rayStart, sphereCenter))
        val c = (MotmVector3.dot(sphereCenter, sphereCenter) + MotmVector3.dot(rayStart, rayStart)
                - 2.0f * MotmVector3.dot(sphereCenter, rayStart) - radius2)

        // Test for intersection.
        val result = b * b - 4.0 * a * c

        @SuppressLint("DefaultLocale") val prettyPrintA = String.format("%6.2f", a)
        @SuppressLint("DefaultLocale") val prettyPrintB = String.format("%6.2f", b)
        @SuppressLint("DefaultLocale") val prettyPrintC = String.format("%6.2f", c)

        Timber.i("int a b c$prettyPrintA$prettyPrintB$prettyPrintC")

        if (result < 0) return false


        // Starting with this section, the code was referenced from libGDX.
        val distSqrt = sqrt(result).toFloat()
        val q: Float

        q = if (b < 0f)
            (-b - distSqrt) / 2.0f
        else
            (-b + distSqrt) / 2.0f


        var t0 = q / 1f
        var t1 = c / q

        // If t0 is larger than t1, swap them around.
        if (t0 > t1) {
            val temp = t0
            t0 = t1
            t1 = temp
        }

        @SuppressLint("DefaultLocale") val prettyPrintX = String.format("%6.2f", sphereCenter.x)
        @SuppressLint("DefaultLocale") val prettyPrintY = String.format("%6.2f", sphereCenter.y)
        @SuppressLint("DefaultLocale") val prettyPrintZ = String.format("%6.2f", sphereCenter.z)
        Timber.i("Intersector: $prettyPrintX  $prettyPrintY $prettyPrintZ result $result t1 $t1")

        // If t1 is less than zero, the object is in the ray's negative direction
        // and consequently ray misses the sphere.
        if (t1 < 0) return false

        // If t0 is less than zero, intersection point is at t1.
        return if (t0 < 0) {
            hitPoint = rayStart.add(MotmVector3.scaleAndCreate(dir, t1))
            true
        } else {
            hitPoint = rayStart.add(MotmVector3.scaleAndCreate(dir, t0))
            true
        }
    }
}










