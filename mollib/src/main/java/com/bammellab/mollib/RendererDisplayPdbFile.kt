/*
 *  Copyright 2021 Bammellab / James Andreas
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

package com.bammellab.mollib

import android.app.Activity
import android.graphics.Bitmap
import android.opengl.GLES20.*
import android.opengl.GLES30.glReadPixels
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import com.bammellab.mollib.common.math.MotmVector3
import com.bammellab.mollib.objects.BufferManager
import com.bammellab.mollib.objects.CubeHacked
import com.bammellab.mollib.objects.Pointer
import com.bammellab.mollib.objects.XYZ
import com.kotmol.pdbParser.Molecule
import timber.log.Timber
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/*
 *   MVP
 *     M - Model to World
 *     V - World to View
 *     P - View to Projection
 */

interface UpdateRenderFinished {
    fun updateActivity(name: String)
}

interface SurfaceCreated {
    fun surfaceCreatedCallback()
}

class RendererDisplayPdbFile(
        private val activity: Activity,
        private val glSurfaceView: GLSurfaceViewDisplayPdbFile
) : GLSurfaceView.Renderer {

    private var renderCubeFlag = false // set to true for centered cube in view.

    private var updateListener: UpdateRenderFinished? = null
    private var listener: UpdateRenderFinished? = null
    private var surfaceCreated: SurfaceCreated? = null
    private var pdbLoaded = false
    private var listenerIsUpdated = 0

    fun setSurfaceCreatedListener(listener: SurfaceCreated) {
        surfaceCreated = listener
    }

    fun setUpdateListener(listener: UpdateRenderFinished) {
        updateListener = listener
    }

    fun overrideInitialScale(newScale: Float) {
        initialScale = newScale
        scaleCurrentF = newScale
    }

    private val xYZ = XYZ()

    var touchX = 300f
    var touchY = 300f

    private val floatVector1 = FloatArray(4)
    private val floatVector2 = FloatArray(4)

    private var displayHydrosFlag = false

    private var pdbFileName: String = "nofile"

    // update to add touch control - these are set by the SurfaceView class
    // These still work without volatile, but refreshes are not guaranteed to happen.
    @Volatile
    var deltaX = 0f

    @Volatile
    var deltaY = 0f

    @Volatile
    var deltaTranslateX = 0f

    @Volatile
    var deltaTranslateY = 0f

    private var initialScale = INITIAL_SCALE

    @Volatile
    var scaleCurrentF = initialScale

    @Volatile
    var scalePrevious = 0f

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private val modelMatrix = FloatArray(16)

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private val viewMatrix = FloatArray(16)

    /**
     * Store the projection matrix. This is used to project the scene onto a 2D viewport.
     */
    private val projectionMatrix = FloatArray(16)

    /**
     * Allocate storage for the final combined matrix. This will be passed into the shader program.
     */
    private val mVPMatrix = FloatArray(16)

    /**
     * Stores a copy of the model matrix specifically for the light position.
     */
    private val lightModelMatrix = FloatArray(16)

    /**
     * This will be used to pass in the transformation matrix.
     */
    private var mVPMatrixHandle: Int = 0

    /**
     * This will be used to pass in the modelview matrix.
     */
    private var mVMatrixHandle: Int = 0

    /**
     * various handles to shader parameters
     */
    private var positionHandle: Int = 0
    private var colorHandle: Int = 0
    private var normalHandle: Int = 0
    private var lightPosHandle: Int = 0

    /**
     * Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
     * we multiply this by our transformation matrices.
     */
    private val lightPosInModelSpace = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)

    /**
     * Used to hold the current position of the light in world space (after transformation via model matrix).
     */
    private val lightPosInWorldSpace = FloatArray(4)

    /**
     * Used to hold the transformed position of the light in eye space (after transformation via modelview matrix)
     */
    private val lightPosInEyeSpace = FloatArray(4)

    private var shaderProgramMode : Int = SHADER_PER_PIXEL

    /**
     * This is a handle to our per-vertex cube shading program.
     */
    private var perVertexProgramHandle = -1

    /**
     * This is a handle to our per-pixel cube shading program.
     */
    private var perPixelProgramHandle: Int = 0

    private var wireFrameRenderingFlag = false
    private var selectModeFlag = false

    /**
     * This is a handle to our light point program.
     */
    private var pointProgramHandle: Int = 0

    private val temporaryMatrix = FloatArray(16)

    /**
     * Store the accumulated rotation.
     */
    private val accumulatedRotation = FloatArray(16)
    private val accumulatedTranslation = FloatArray(16)
    private val accumulatedScaling = FloatArray(16)

    /**
     * Store the current rotation.
     */
    private val incrementalRotation = FloatArray(16)

    private var mCube: CubeHacked? = null
    private var mPointer: Pointer? = null
    private var molecule: Molecule? = null
    private var reportedTimeFlag = false

    fun setMolecule(moleculeIn: Molecule) {
        molecule = moleculeIn
        reportedTimeFlag = false
        Timber.v("mol is set")
        listenerIsUpdated = 0
    }

    fun tossMoleculeToGC() {
        molecule = null
        listenerIsUpdated = 0
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        // Set the background clear color to black.
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        // Use culling to remove back faces.
        glEnable(GL_CULL_FACE)

        // Enable depth testing
        glEnable(GL_DEPTH_TEST)

        // Position the eye in front of the origin.
        val eyeX = 0.0f
        val eyeY = 0.0f
        val eyeZ = -0.5f

        // We are looking toward the distance
        val lookX = 0.0f
        val lookY = 0.0f
        val lookZ = -5.0f

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        val upX = 0.0f
        val upY = 1.0f
        val upZ = 0.0f

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)

        var vertexShader = xYZ.vertexShaderLesson2
        var fragmentShader = xYZ.fragmentShaderLesson2
        var vertexShaderHandle = compileShader(GL_VERTEX_SHADER, vertexShader)
        var fragmentShaderHandle = compileShader(GL_FRAGMENT_SHADER, fragmentShader)

        perVertexProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                arrayOf("a_Position", "a_Color", "a_Normal"))

        /* add in a pixel shader from lesson 3 - switchable */
        vertexShader = xYZ.vertexShaderLesson3
        fragmentShader = xYZ.fragmentShaderLesson3
        vertexShaderHandle = compileShader(GL_VERTEX_SHADER, vertexShader)
        fragmentShaderHandle = compileShader(GL_FRAGMENT_SHADER, fragmentShader)
        perPixelProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                arrayOf("a_Position", "a_Color", "a_Normal"))

        // Define a simple shader program for our point (the orbiting light source)
        val pointVertexShader = ("uniform mat4 u_MVPMatrix;      \n"
                + "attribute vec4 a_Position;     \n"
                + "void main()                    \n"
                + "{                              \n"
                + "   gl_Position = u_MVPMatrix   \n"
                + "               * a_Position;   \n"
                + "   gl_PointSize = 5.0;         \n"
                + "}                              \n")

        val pointFragmentShader = ("precision mediump float;       \n"
                + "void main()                    \n"
                + "{                              \n"
                + "   gl_FragColor = vec4(1.0,    \n"
                + "   1.0, 1.0, 1.0);             \n"
                + "}                              \n")

        val pointVertexShaderHandle = compileShader(GL_VERTEX_SHADER, pointVertexShader)
        val pointFragmentShaderHandle = compileShader(GL_FRAGMENT_SHADER, pointFragmentShader)
        pointProgramHandle = createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle,
                arrayOf("a_Position"))

        /*
         * CubeHacked is the current "selected" graphic for an atom
         */
        mCube = CubeHacked()
        mPointer = Pointer()

        // Initialize the modifier matrices
        Matrix.setIdentityM(accumulatedRotation, 0)
        Matrix.setIdentityM(accumulatedTranslation, 0)
        Matrix.setIdentityM(accumulatedScaling, 0)

        // was the old "tunnel" graphic showing the selection area
        //   now deprecated
        // mSegmentBackboneDirect = new SegmentBackboneDirect(mMol);

        if (surfaceCreated != null) {
            surfaceCreated!!.surfaceCreatedCallback()
        }
    }

    override fun onSurfaceChanged(glUnused: GL10?, widthIn: Int, heightIn: Int) {
        // Timber.e("Scale is $scaleCurrentF $this")
        // Set the OpenGL viewport to the same size as the surface.
        glViewport(0, 0, widthIn, heightIn)
        width = widthIn
        height = heightIn

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        val ratio = widthIn.toFloat() / heightIn
        val left = -ratio * scaleCurrentF
        val right = ratio * scaleCurrentF
        val bottom = -1.0f * scaleCurrentF
        val top = 1.0f * scaleCurrentF
        val near = 1.0f
        val far = 20.0f
        // final float far = 5.0f;  nothing visible

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far)

        val glError = glGetError()
        if (glError != GL_NO_ERROR) {
            Timber.e("GLERROR: $glError")
        }
    }

    override fun onDrawFrame(glUnused: GL10) {

        val startTime = SystemClock.uptimeMillis().toFloat()
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        if (molecule == null) {
            return
        }

        if (!selectModeFlag) {
            if (scaleCurrentF != scalePrevious) {
                onSurfaceChanged(null, width, height)  // adjusts view
                scalePrevious = scaleCurrentF
            }

            // move the view as necessary if the user has shifted it manually
            Matrix.translateM(viewMatrix, 0, deltaTranslateX, deltaTranslateY, 0.0f)
            deltaTranslateX = 0.0f
            deltaTranslateY = 0.0f

           
            // Set our per-vertex lighting program.
            val selectedProgramHandle = when (shaderProgramMode) {
                SHADER_PER_VERTEX -> perVertexProgramHandle
                SHADER_POINT_NO_NORMALS ->  pointProgramHandle
                else ->  perPixelProgramHandle
            }

            glUseProgram(selectedProgramHandle)
            // Set program handles for drawing.
            mVPMatrixHandle = glGetUniformLocation(selectedProgramHandle, "u_MVPMatrix")
            mVMatrixHandle = glGetUniformLocation(selectedProgramHandle, "u_MVMatrix")
            lightPosHandle = glGetUniformLocation(selectedProgramHandle, "u_LightPos")
            positionHandle = glGetAttribLocation(selectedProgramHandle, "a_Position")
            colorHandle = glGetAttribLocation(selectedProgramHandle, "a_Color")
            normalHandle = glGetAttribLocation(selectedProgramHandle, "a_Normal")

            // Calculate position of the light. Push into the distance.
            Matrix.setIdentityM(lightModelMatrix, 0)
            Matrix.translateM(lightModelMatrix, 0, 0.0f, 0.0f, -1.0f)

            Matrix.multiplyMV(lightPosInWorldSpace, 0, lightModelMatrix, 0, lightPosInModelSpace, 0)
            Matrix.multiplyMV(lightPosInEyeSpace, 0, viewMatrix, 0, lightPosInWorldSpace, 0)

            // 0.5 is pretty small but not small enough
            var scaleF = 0.05f
            if (molecule != null) {
                scaleF = (0.4 / molecule!!.maxPostCenteringVectorMagnitude).toFloat()
            }


            /*
             * render the molecule triangles
             */
            Matrix.setIdentityM(modelMatrix, 0)
            Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -2.5f)
            Matrix.scaleM(modelMatrix, 0, scaleF, scaleF, scaleF)
            doMatrixSetup()
            BufferManager.render(positionHandle, colorHandle, normalHandle, wireFrameRenderingFlag)
            // DEBUG:  box in scene center
            // mBoundingBox.render(positionHandle, colorHandle, normalHandle, wireFrameRenderingFlag);

            // DEBUG cube
            if (renderCubeFlag) {
                debugCube()
            }

            // END of DEBUG cube

            val glError = glGetError()
            if (glError != GL_NO_ERROR) {
                Timber.e("GLERROR: $glError")
            }

        } else {
            select()
        }

        val endTime = SystemClock.uptimeMillis().toFloat()
        val elapsedTime = (endTime - startTime) / 1000
        val prettyPrint = String.format("%6.2f", elapsedTime)
//        Timber.v("T $prettyPrint")

        if (!reportedTimeFlag) {
            reportedTimeFlag = true
//            val endTime = SystemClock.uptimeMillis().toFloat()
//            val elapsedTime = (endTime - startTime) / 1000
//            val prettyPrint = String.format("%6.2f", elapsedTime)
//            val activityManager = activity.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
//            val memInfo = ActivityManager.MemoryInfo()
//            activityManager.getMemoryInfo(memInfo)
//            Timber.i("*** RENDERER mema %d seconds %s",
//                    memInfo.availMem / 1024 / 1024,
//                    prettyPrint)
        }

        if (updateListener != null) {
            if (molecule != null && molecule!!.molName.isNotEmpty()) {
                if (listenerIsUpdated < 5) {
                    listenerIsUpdated += 1
                    Timber.d("DRAW number $listenerIsUpdated")
                    if (listenerIsUpdated == 5) {  // hey five times works!!
                        listenerIsUpdated += 1
                        try {
                            val name = molecule!!.molName
                            Timber.e("onDrawFrame: Update Activity")
                            activity.runOnUiThread { updateListener!!.updateActivity(name) }
                        } catch (e: Exception) {
                            Timber.e("NULL pointer in renderer")
                        }
                    } else {
                        glSurfaceView.requestRender() // Hack for double buffering
                    }
                }
            }
        }
    }


    private fun doMatrixSetup() {
        /*
         * Set a matrix that contains the additional *incremental* rotation
         * as indicated by the user touching the screen
         */

        if (!selectModeFlag) {
            Matrix.setIdentityM(incrementalRotation, 0)
            Matrix.rotateM(incrementalRotation, 0, deltaX, 0.0f, 1.0f, 0.0f)
            Matrix.rotateM(incrementalRotation, 0, deltaY, 1.0f, 0.0f, 0.0f)
            deltaX = 0.0f
            deltaY = 0.0f

            // Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
            Matrix.multiplyMM(temporaryMatrix, 0, incrementalRotation, 0, accumulatedRotation, 0)
            System.arraycopy(temporaryMatrix, 0, accumulatedRotation, 0, 16)
        }
        // Rotate the object taking the overall rotation into account.
        Matrix.multiplyMM(temporaryMatrix, 0, modelMatrix, 0, accumulatedRotation, 0)
        System.arraycopy(temporaryMatrix, 0, modelMatrix, 0, 16)

        // This multiplies the view matrix by the model matrix, and stores
        // the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mVPMatrix, 0, viewMatrix, 0, modelMatrix, 0)

        // Pass in the modelview matrix.
        glUniformMatrix4fv(mVMatrixHandle, 1, false, mVPMatrix, 0)

        val mvmB4 = mVPMatrix

        // This multiplies the modelview matrix by the projection matrix,
        // and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(temporaryMatrix, 0, projectionMatrix, 0, mVPMatrix, 0)
        System.arraycopy(temporaryMatrix, 0, mVPMatrix, 0, 16)

        // Pass in the combined matrix.
        glUniformMatrix4fv(mVPMatrixHandle, 1, false, mVPMatrix, 0)

        // Pass in the light position in eye space.
        glUniform3f(lightPosHandle, lightPosInEyeSpace[0], lightPosInEyeSpace[1], lightPosInEyeSpace[2])

        val mm = modelMatrix
        val vm = viewMatrix
        val mvm = mVPMatrix
        val light = lightPosInEyeSpace

        val glError = glGetError()
        if (glError != GL_NO_ERROR) {
            Timber.e("OnDrawFrame, glerror =  $glError")
        }
    }

    fun resetCamera() {
        /*
         * reset view manipulation
         */
        scaleCurrentF = initialScale // zoom

        // Position the eye in front of the origin.
        val eyeX = 0.0f
        val eyeY = 0.0f
        val eyeZ = -0.5f

        // We are looking toward the distance
        val lookX = 0.0f
        val lookY = 0.0f
        val lookZ = -5.0f

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        val upX = 0.0f
        val upY = 1.0f
        val upZ = 0.0f

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)

    }

    /*
     * this is a special setup -
     *    that feeds a uniform matrix to the shader as the Model + View matrix.
     *    This is then combined with the Projection matrix for the MVP matrix.
     *    The result is a matrix that doesn't change with the model and view -
     *    used for pointer rendering and manipulation.
     */
    private fun doMatrixSetupViewOnly() {

        // Pass in the modelview matrix.
        // GLES20.glUniformMatrix4fv(mVMatrixHandle, 1, false, mVPMatrix, 0);
        Matrix.setIdentityM(temporaryMatrix, 0)
        glUniformMatrix4fv(mVMatrixHandle, 1, false, temporaryMatrix, 0)

        // This multiplies the modelview matrix by the projection matrix,
        // and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(temporaryMatrix, 0, projectionMatrix, 0, modelMatrix, 0)
        System.arraycopy(temporaryMatrix, 0, mVPMatrix, 0, 16)

        // Pass in the combined matrix.
        // GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false, mVPMatrix, 0);
        glUniformMatrix4fv(mVPMatrixHandle, 1, false, projectionMatrix, 0)

        // Pass in the light position in eye space.
        glUniform3f(lightPosHandle, lightPosInEyeSpace[0], lightPosInEyeSpace[1], lightPosInEyeSpace[2])

        val glError = glGetError()
        if (glError != GL_NO_ERROR) {
            Timber.e("OnDrawFrame, glerror =  $glError")
        }
    }

    /**
     * Draws a point representing the position of the light.
     */
    private fun drawLight() {
        val pointMVPMatrixHandle = glGetUniformLocation(pointProgramHandle, "u_MVPMatrix")
        val pointPositionHandle = glGetAttribLocation(pointProgramHandle, "a_Position")

        // Pass in the position.
        glVertexAttrib3f(pointPositionHandle, lightPosInModelSpace[0], lightPosInModelSpace[1], lightPosInModelSpace[2])

        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        glDisableVertexAttribArray(pointPositionHandle)

        // Pass in the transformation matrix.
        Matrix.multiplyMM(mVPMatrix, 0, viewMatrix, 0, lightModelMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, mVPMatrix, 0)
        glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mVPMatrix, 0)

        // Draw the point.
        glDrawArrays(GL_POINTS, 0, 1)
    }

    /**
     * Helper function to compile a shader.
     *
     * @param shaderType   The shader type.
     * @param shaderSource The shader source code.
     * @return An OpenGL handle to the shader.
     */
    private fun compileShader(shaderType: Int, shaderSource: String): Int {
        var shaderHandle = glCreateShader(shaderType)

        if (shaderHandle != 0) {
            // Pass in the shader source.
            glShaderSource(shaderHandle, shaderSource)

            // Compile the shader.
            glCompileShader(shaderHandle)

            // Get the compilation status.
            val compileStatus = IntArray(1)
            glGetShaderiv(shaderHandle, GL_COMPILE_STATUS, compileStatus, 0)

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                Timber.e("Error compiling shader: ${glGetShaderInfoLog(shaderHandle)}")
                glDeleteShader(shaderHandle)
                shaderHandle = 0
            }
        }

        if (shaderHandle == 0) {
            throw RuntimeException("Error creating shader.")
        }

        return shaderHandle
    }

    /**
     * Helper function to compile and link a program.
     *
     * @param vertexShaderHandle   An OpenGL handle to an already-compiled vertex shader.
     * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
     * @param attributes           Attributes that need to be bound to the program.
     * @return An OpenGL handle to the program.
     */
    private fun createAndLinkProgram(vertexShaderHandle: Int, fragmentShaderHandle: Int, attributes: Array<String>?): Int {
        var programHandle = glCreateProgram()

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            glAttachShader(programHandle, vertexShaderHandle)

            // Bind the fragment shader to the program.
            glAttachShader(programHandle, fragmentShaderHandle)

            // Bind attributes
            if (attributes != null) {
                val size = attributes.size
                for (i in 0 until size) {
                    glBindAttribLocation(programHandle, i, attributes[i])
                }
            }

            // Link the two shaders together into a program.
            glLinkProgram(programHandle)

            // Get the link status.
            val linkStatus = IntArray(1)
            glGetProgramiv(programHandle, GL_LINK_STATUS, linkStatus, 0)

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                Timber.e("Error compiling shader: ${glGetShaderInfoLog(programHandle)}")
                glDeleteProgram(programHandle)
                programHandle = 0
            }
        }

        if (programHandle == 0) {
            throw RuntimeException("Error creating program.")
        }

        return programHandle
    }

    // rotate through shader modes
    fun toggleShader() {
        shaderProgramMode = when (shaderProgramMode) {
            SHADER_PER_PIXEL -> SHADER_PER_VERTEX
            SHADER_PER_VERTEX -> SHADER_POINT_NO_NORMALS
            else -> SHADER_PER_PIXEL
        }
    }

    fun overrideShader(toWhat: Int) {
        shaderProgramMode = toWhat
    }

    fun toggleHydrogenDisplayMode() {
        displayHydrosFlag = !displayHydrosFlag
    }

    fun toggleWireframeFlag() {
        wireFrameRenderingFlag = !wireFrameRenderingFlag
    }

    fun toggleSelectFlag() {

        if (selectModeFlag) {
            selectModeFlag = false
            // reset any rotation during selection movement
            deltaX = 0.0f
            deltaY = 0.0f
            scaleCurrentF = saveScale
            glSurfaceView.selectMode = false
        } else {
            selectModeFlag = true
            saveScale = scaleCurrentF
            glSurfaceView.selectMode = true
        }
    }


    /**
     * Implement the interactive selection of an atom
     */
    private fun select() {

        /*
         * first render the triangles of the molecule
         */
//        var scaleF = (1.5f / molecule!!.dcOffset).toFloat()
//        Matrix.setIdentityM(modelMatrix, 0)
//        Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -2.5f)
//        Matrix.scaleM(modelMatrix, 0, scaleF, scaleF, scaleF)
//        doMatrixSetup()
//        BufferManager.render(positionHandle, colorHandle, normalHandle, wireFrameRenderingFlag)
//
//        // int[] mViewport = new int[]{0, 0, mWidth, mHeight};
//        mViewport[2] = mWidth
//        mViewport[3] = mHeight
//        val x = touchX.toInt()
//        // offset works out to 100 pixels on an Galaxy S2 (800 vertical),
//        //   or 160 on a Nexus 4 (1280 vertical)
//        val y = touchY.toInt() - (max(mHeight.toFloat(), mWidth.toFloat()) * 0.125f).toInt()
//
//        Matrix.setIdentityM(temporaryMatrix, 0)
//
//        /*        int result;
//        result = GLU.gluUnProject(x, mHeight - y, 0.0f, temporaryMatrix, 0,
//                projectionMatrix, 0, mViewport, 0, mFloatVector1, 0);
//
//        result = GLU.gluUnProject(x, mHeight - y, 1.0f, temporaryMatrix, 0,
//                projectionMatrix, 0, mViewport, 0, mFloatVector2, 0);*/
//
//        screenVector1.setAll(
//                (floatVector1[0] / floatVector1[3]).toFloat(),
//                (floatVector1[1] / floatVector1[3]).toFloat(),
//                (floatVector1[2] / floatVector1[3]).toFloat()
//        )
//
//        screenVector2.setAll(
//                (floatVector2[0] / floatVector2[3]).toFloat(),
//                (floatVector2[1] / floatVector2[3]).toFloat(),
//                (floatVector2[2] / floatVector2[3]).toFloat()
//        )
//
//        // now draw the targeted location in screen coords
//        doMatrixSetupViewOnly()
//
//        //        String pretty_print_a = String.format("%6.2f", screenVector1.x);
//        //        String pretty_print_b = String.format("%6.2f", screenVector1.y);
//        //        String pretty_print_c = String.format("%6.2f", screenVector1.z);
//
//        // Timber.i("pointer: " + pretty_print_a + pretty_print_b + pretty_print_c);
//
//        mPointer!!.setup(
//                screenVector1.x.toFloat(),
//                screenVector1.y.toFloat(),
//                -1.01f, scaleCurrentF)
//
//        mPointer!!.render(positionHandle, colorHandle, normalHandle, false /* wireFrameRenderingFlag */)
//
//        /*
//         * move the origin point of the interesecting ray out
//         *   a bit in Z space - so that the origin is always
//         *   towards the camera
//         */
//        tempVector1.setAll(screenVector2)
//        tempVector1.subtract(screenVector1)
//        tempVector1.normalize()
//        tempVector1.multiply(2.0)
//        screenVector1.subtract(tempVector1)
//
//        var selectedAtomIndex: Int
//
//        val gotHit = false
//
//        var atom1: PdbAtom?
//        selectedAtomsList.clear()
//        for (i in 0 until molecule!!.atomNumberList.size) {
//            atom1 = molecule!!.atomNumberToAtomInfoHash[molecule!!.atomNumberList[i]]
//            if (atom1 == null) {
//                Timber.e("drawSpheres: error - got null for %d", molecule!!.atomNumberList[i])
//                continue
//            }
//            // skip HOH (water) molecules
//            if (atom1.atomType == PdbAtom.AtomType.IS_HETATM && atom1.residueName == "HOH") {
//                continue
//            }
//
//            //            if (atom1.pickedAtom) {
//            //                atom1.pickedAtom = false;
//            //                managerViewmode.repeatViewMode();
//            //            }
//
//            floatVector2[0] = atom1.atomPosition.x.toFloat()
//            floatVector2[1] = atom1.atomPosition.y.toFloat()
//            floatVector2[2] = atom1.atomPosition.z.toFloat()
//            floatVector2[3] = 1.0f
//
//            // same as for rendering!!
//            // Works!!
//            scaleF = (1.5f / molecule!!.dcOffset).toFloat()
//            Matrix.setIdentityM(modelMatrix, 0)
//            Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -2.5f)
//            Matrix.scaleM(modelMatrix, 0, scaleF, scaleF, scaleF)
//            doMatrixSetup()
//
//            Matrix.multiplyMM(temporaryMatrix, 0, viewMatrix, 0, modelMatrix, 0)
//            Matrix.multiplyMV(floatVector1, 0, temporaryMatrix, 0, floatVector2, 0)
//
//            if (floatVector1[3] == 0f) floatVector1[3] = 1.0f
//
//            testAtomVector.setAll(
//                    (floatVector1[0] / floatVector1[3]).toFloat(),
//                    (floatVector1[1] / floatVector1[3]).toFloat(),
//                    (floatVector1[2] / floatVector1[3]).toFloat()
//            )
//
//            /*  old way of doing the intersection:  */
//            //                got_hit = Intersector.intersectRaySphere(
//            //                        screenVector1, screenVector2,
//            //                        testAtomVector, 0.10f,
//            //                        screen_vector /* if a hit */);
//
//            /* end old way of doing the intersection */
//
//            // following along: http://paulbourke.net/geometry/circlesphere/raysphere.c
//
//            //   -- USE only static allocation in main loop
//            // MotmVector3 p1_minus_sc = new MotmVector3();
//            // MotmVector3 dp = new MotmVector3();
//
//            val p1_minus_sc = tempVector1
//            val dp = tempVector2
//
//            dp.setAll(screenVector2)
//            dp.subtract(screenVector1)
//            dp.normalize()
//
//            val a = dp.dot(dp)
//
//            p1_minus_sc.setAll(screenVector1)
//            p1_minus_sc.subtract(testAtomVector)
//
//            val b = 2 * dp.dot(p1_minus_sc)
//
//            var c = testAtomVector.dot(testAtomVector)
//            c += screenVector1.dot(screenVector1)
//            c -= 2 * testAtomVector.dot(screenVector1)
//            // c = c - (0.10 * 0.10);
//            // c = c - (0.05 * 0.05);
//            // tempVector1 the "radius" of the target sphere
//            //  based on the size of the molecule
//            val radius = 0.05 * 15.0 / molecule!!.dcOffset
//            c -= radius * radius
//
//            val bb4ac = b * b - 4.0 * a * c
//
//            /*
//             * if >= 0 then we have a hit
//             */
//            if (bb4ac >= 0) {
//
//                val selectedAtom = AtomInfo()
//                selectedAtom.atomTransformedPosition.setAll(testAtomVector)
//                selectedAtom.indexNumber = i
//                selectedAtomsList.add(selectedAtom)
//            }
//        }
//
//        if (selectedAtomsList.isEmpty()) {
//            return
//        }
//        var maxZ = -999999.0
//        selectedAtomIndex = 999999
//        var ai: AtomInfo
//        for (i in selectedAtomsList.indices) {
//            ai = selectedAtomsList[i]
//            val zvalue = ai.atomTransformedPosition.z
//            if (zvalue > maxZ) {
//                selectedAtomIndex = ai.indexNumber
//                maxZ = zvalue
//            }
//        }
//
//        //        if (atom1.atom_number != lastReportedAtom) {
//        //            Log.i("SEL", "atom " + atom1.atom_number);
//        //            lastReportedAtom = atom1.atom_number;
//        //        }
//
//
//        // pop in a cube - try to surround the picked atom
//        scaleF = 1.5f / molecule!!.dcOffset
//        // cube is 8X a molecule
//        // scaleF = scaleF / 8.0f;
//        Matrix.setIdentityM(modelMatrix, 0)
//        Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -2.5f)
//        Matrix.scaleM(modelMatrix, 0, scaleF, scaleF, scaleF)
//
//
//        atom1 = molecule!!.atoms[molecule!!.numList[selectedAtomIndex]]
//        doMatrixSetup()
//        mCube!!.setup(
//                atom1!!.atomPosition.x.toFloat(),
//                atom1.atomPosition.y.toFloat(),
//                atom1.atomPosition.z.toFloat())
//        mCube!!.render(positionHandle, colorHandle, normalHandle, false /* wireFrameRenderingFlag */)
//
//        if (atom1.atomNumber != lastReportedAtom) {
//            Timber.i("atom %s", atom1.atomNumber)
//            lastReportedAtom = atom1.atomNumber
//        }


    }

    /**
     *  cube at 0,0.0f
     */
    private fun debugCube() {
        val debugCubeScale = 0.05f
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -2.5f)
        Matrix.scaleM(modelMatrix, 0, debugCubeScale, debugCubeScale, debugCubeScale)

        doMatrixSetup()
        mCube!!.setup(
                0.toFloat(),
                0.toFloat(),
                0.toFloat())
        mCube!!.render(positionHandle, colorHandle, normalHandle, false /* wireFrameRenderingFlag */)
    }

    fun doCleanUp() {
        BufferManager.resetBuffersForNextUsage()
        molecule = null
        Timber.i("CLEANUP")
    }

    /**
     *  read the GL frame buffer and convert the bits
     *  to an Android compatible bitmap.
     *  Convert RGB(#000000) to transparent
     *  @link https://stackoverflow.com/a/5013141/3853712
     */
    private lateinit var b: IntArray
    private lateinit var bt: IntArray
    private lateinit var ib: IntBuffer

    fun allocateReadBitmapArrays(w: Int, h: Int) {
        b = IntArray(w * h)
        bt = IntArray(w * h)
        ib = IntBuffer.wrap(b)
    }

    fun getScreenWidth(): Int = width
    fun getScreenHeight(): Int = height

    fun readGlBufferToBitmap(x: Int, y: Int, w: Int, h: Int): Bitmap {


//        b = IntArray(width * height)
//        bt = IntArray(width * height)
//        ib = IntBuffer.wrap(b)
//        ib.position(0)
//        glReadBuffer(GL_BACK);
//        glReadPixels(0,0,width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib)
//        var non_zero = 0
//        for (i in 0 until width * height) {
//            if (b[i] != 0xff000000.toInt()) {
//                non_zero += 1
//            }
//
//        }
//        Timber.d("$non_zero")

        glFlush()
        ib.position(0)

        //glReadBuffer(GL_BACK)
        glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib)
        val glError = glGetError()
        if (glError != GL_NO_ERROR) {
            Timber.e("readGlBufferToBitmap, glerror =  ${glError.toString(16)}")
        }
        var i = 0
        var k = 0
        while (i < h) {
            // remember, that OpenGL bitmap is incompatible with Android bitmap
            // and so, some correction is needed.
            for (j in 0 until w) {
                var pix = b[(i /*+ y*/) * w + j]
                // convert black pixels to transparent
                if (pix == 0xff000000.toInt()) {
                    pix = 0
                }
                val pb = pix shr 16 and 0xff
                val pr = pix shl 16 and 0x00ff0000
                val pix1 = pix and 0xff00ff00.toInt() or pr or pb
                bt[(h - k - 1) * w + j] = pix1
            }
            i++
            k++
        }
        return Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888)
    }

    companion object {

        const val SHADER_PER_VERTEX = 0
        const val SHADER_PER_PIXEL = 1
        const val SHADER_POINT_NO_NORMALS = 2
        
        
        private val screenVector1 = MotmVector3()
        private val screenVector2 = MotmVector3()
        private val tempVector1 = MotmVector3()
        private val tempVector2 = MotmVector3()
        private val testAtomVector = MotmVector3()

//        private val selectedAtomsList = ArrayList<AtomInfo>()

        //private const val INITIAL_SCALE = 0.5f // small molecule for photos
        private const val INITIAL_SCALE = 0.2f
        private var saveScale = 0f

        private var height: Int = 0
        private var width: Int = 0
        private var lastReportedAtom = -1
        private val mViewport = intArrayOf(0, 0, 0, 0)
    }

    //    public void updatePdbFinishedParsing() {
    //        /*
    //         * let the UI know that the parsing is completed (to flush the spinner)
    //         */
    //        Message message;
    //        message = Message.obtain(handler, UI_MESSAGE_FINISHED_PARSING);
    //        handler.dispatchMessage(message);
    //    }
}