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

@file:Suppress(
        "unused",
        "unused_variable",
        "unused_parameter",
        "unused_property",
        "deprecation",
        "ConstantConditionIf",
        "LocalVariableName",
        "PropertyName")

package com.bammellab.mollib

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES30.glReadPixels
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.view.View
import com.bammellab.mollib.common.math.Vector3
import com.bammellab.mollib.objects.*
import com.bammellab.mollib.protein.AtomInfo
import com.bammellab.mollib.protein.Molecule
import com.bammellab.mollib.protein.PdbAtom
import timber.log.Timber
import java.io.InputStream
import java.nio.IntBuffer
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.max


/*
 *   Alt-Enter to disable annoying Lint warnings...
 *
 *   MVP
 *     M - Model to World
 *     V - World to View
 *     P - View to Projection
 */

interface UpdateRenderFinished {
    fun updateActivity()
}

/**
 * This class implements our custom renderer. Note that the GL10 parameter passed in is unused for OpenGL ES 2.0
 * renderers -- the static class GLES20 is used instead.
 */
class RendererDisplayPdbFile
/*
 * Let's get started.
 */
(private val mActivity: Activity,
 private val mGlSurfaceView: GLSurfaceViewDisplayPdbFile
 ) : GLSurfaceView.Renderer {

    var listener: UpdateRenderFinished? = null


    private val mXYZ = XYZ()

    var mTouchX = 300f
    var mTouchY = 300f

    private val mFloatVector1 = FloatArray(4)
    private val mFloatVector2 = FloatArray(4)


    private var mPdbFileName: String = "nofile"

    // update to add touch control - these are set by the SurfaceView class
    // These still work without volatile, but refreshes are not guaranteed to happen.
    @Volatile
    var mDeltaX: Float = 0.toFloat()

    @Volatile
    var mDeltaY: Float = 0.toFloat()

    @Volatile
    var mDeltaTranslateX: Float = 0.toFloat()

    @Volatile
    var mDeltaTranslateY: Float = 0.toFloat()

    // public volatile float mScaleCurrentF = 1.0f;
    // use scale to zoom in initially
    @Volatile
    var mScaleCurrentF = INITIAL_SCALE

    @Volatile
    var mScalePrevious = 0f

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private val mModelMatrix = FloatArray(16)

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private val mViewMatrix = FloatArray(16)

    /**
     * Store the projection matrix. This is used to project the scene onto a 2D viewport.
     */
    private val mProjectionMatrix = FloatArray(16)

    /**
     * Allocate storage for the final combined matrix. This will be passed into the shader program.
     */
    private val mMVPMatrix = FloatArray(16)

    /**
     * Stores a copy of the model matrix specifically for the light position.
     */
    private val mLightModelMatrix = FloatArray(16)

    /**
     * This will be used to pass in the transformation matrix.
     */
    private var mMVPMatrixHandle: Int = 0

    /**
     * This will be used to pass in the modelview matrix.
     */
    private var mMVMatrixHandle: Int = 0

    /**
     * This will be used to pass in the light position.
     */
    private var mLightPosHandle: Int = 0

    /**
     * This will be used to pass in model position information.
     */
    private var mPositionHandle: Int = 0

    /**
     * This will be used to pass in model color information.
     */
    private var mColorHandle: Int = 0

    /**
     * This will be used to pass in model normal information.
     */
    private var mNormalHandle: Int = 0

    /**
     * Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
     * we multiply this by our transformation matrices.
     */
    private val mLightPosInModelSpace = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)

    /**
     * Used to hold the current position of the light in world space (after transformation via model matrix).
     */
    private val mLightPosInWorldSpace = FloatArray(4)

    /**
     * Used to hold the transformed position of the light in eye space (after transformation via modelview matrix)
     */
    private val mLightPosInEyeSpace = FloatArray(4)

    private var mUseVertexShaderProgram = false

    /**
     * This is a handle to our per-vertex cube shading program.
     */
    private var mPerVertexProgramHandle = -1

    /**
     * This is a handle to our per-pixel cube shading program.
     */
    private var mPerPixelProgramHandle: Int = 0

    private var mWireFrameRenderingFlag = false
    private var mSelectModeFlag = false

    /**
     * This is a handle to our light point program.
     */
    private var mPointProgramHandle: Int = 0

    /**
     * A temporary matrix.
     */
    private val mTemporaryMatrix = FloatArray(16)

    /**
     * Store the accumulated rotation.
     */
    private val mAccumulatedRotation = FloatArray(16)
    private val mAccumulatedTranslation = FloatArray(16)
    private val mAccumulatedScaling = FloatArray(16)

    /**
     * Store the current rotation.
     */
    private val mIncrementalRotation = FloatArray(16)

    private var mCube: CubeHacked? = null
    private var mPointer: Pointer? = null
    private val mMol: Molecule
    private val mPdbFile: ParserPdbFile
    private val mManagerViewmode: ManagerViewmode?

    private val mBufferManager = BufferManager.getInstance(mActivity)

    init {

        mBufferManager.resetBuffersForNextUsage()

        mMol = Molecule()
        mManagerViewmode = ManagerViewmode(
                mActivity, mMol, mBufferManager)
        mPdbFile = ParserPdbFile(
                mActivity, mMol, mBufferManager, mManagerViewmode)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        // Set the background clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

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
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)

        var vertexShader = mXYZ.vertexShaderLesson2
        var fragmentShader = mXYZ.fragmentShaderLesson2
        var vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader)
        var fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader)

        mPerVertexProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                arrayOf("a_Position", "a_Color", "a_Normal"))

        /* add in a pixel shader from lesson 3 - switchable */
        vertexShader = mXYZ.vertexShaderLesson3
        fragmentShader = mXYZ.fragmentShaderLesson3
        vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader)
        fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader)
        mPerPixelProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
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

        val pointVertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, pointVertexShader)
        val pointFragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, pointFragmentShader)
        mPointProgramHandle = createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle,
                arrayOf("a_Position"))

        /*
         * CubeHacked is the current "selected" graphic for an atom
         */
        mCube = CubeHacked()
        mPointer = Pointer()


        // Initialize the modifier matrices
        Matrix.setIdentityM(mAccumulatedRotation, 0)
        Matrix.setIdentityM(mAccumulatedTranslation, 0)
        Matrix.setIdentityM(mAccumulatedScaling, 0)

        /*
         * let the UI thread know that all GL objects are created
         */
//        val message = Message.obtain(mHandler, Molecule.UI_MESSAGE_GL_READY)
//        mHandler.dispatchMessage(message)

        // was the old "tunnel" graphic showing the selection area
        //   now deprecated
        // mSegmentBackboneDirect = new SegmentBackboneDirect(mMol);
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height)
        mWidth = width
        mHeight = height

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        val ratio = width.toFloat() / height
        val left = -ratio * mScaleCurrentF
        val right = ratio * mScaleCurrentF
        val bottom = -1.0f * mScaleCurrentF
        val top = 1.0f * mScaleCurrentF
        val near = 1.0f
        val far = 20.0f
        // final float far = 5.0f;  nothing visible

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far)

        val glError = GLES20.glGetError()
        if (glError != GLES20.GL_NO_ERROR) {
            Timber.e("GLERROR: $glError")
        }
    }

    override fun onDrawFrame(glUnused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        if (!mSelectModeFlag) {
            if (mScaleCurrentF != mScalePrevious) {
                onSurfaceChanged(null, mWidth, mHeight)  // adjusts view
                mScalePrevious = mScaleCurrentF
            }

            // move the view as necessary if the user has shifted it manually
            Matrix.translateM(mViewMatrix, 0, mDeltaTranslateX, mDeltaTranslateY, 0.0f)
            mDeltaTranslateX = 0.0f
            mDeltaTranslateY = 0.0f

            // Set our per-vertex lighting program.
            val mSelectedProgramHandle = if (mUseVertexShaderProgram) {
                mPerVertexProgramHandle
            } else {
                mPerPixelProgramHandle
            }

            GLES20.glUseProgram(mSelectedProgramHandle)
            // Set program handles for drawing.
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mSelectedProgramHandle, "u_MVPMatrix")
            mMVMatrixHandle = GLES20.glGetUniformLocation(mSelectedProgramHandle, "u_MVMatrix")
            mLightPosHandle = GLES20.glGetUniformLocation(mSelectedProgramHandle, "u_LightPos")
            mPositionHandle = GLES20.glGetAttribLocation(mSelectedProgramHandle, "a_Position")
            mColorHandle = GLES20.glGetAttribLocation(mSelectedProgramHandle, "a_Color")
            mNormalHandle = GLES20.glGetAttribLocation(mSelectedProgramHandle, "a_Normal")

            // Calculate position of the light. Push into the distance.
            Matrix.setIdentityM(mLightModelMatrix, 0)
            Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -1.0f)

            Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0)
            Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0)

            val scaleF = 1.5f / mMol.dcOffset

            /*
             * render the molecule triangles
             */
            Matrix.setIdentityM(mModelMatrix, 0)
            Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -2.5f)
            Matrix.scaleM(mModelMatrix, 0, scaleF, scaleF, scaleF)
            doMatrixSetup()
            mBufferManager.render(mPositionHandle, mColorHandle, mNormalHandle, mWireFrameRenderingFlag)
            // DEBUG:  box in scene center
            // mBoundingBox.render(mPositionHandle, mColorHandle, mNormalHandle, mWireFrameRenderingFlag);

        } else {
            select()
        }

        if (!mMol.mReportedTimeFlag) {
            mMol.mReportedTimeFlag = true
            val endTime = SystemClock.uptimeMillis().toFloat()
            val elapsedTime = (endTime - mMol.mStartOfParseTime) / 1000
            @SuppressLint("DefaultLocale") val prettyPrint = String.format("%6.2f", elapsedTime)

            val activityManager = mActivity.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
            val mInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(mInfo)
            Timber.i("*** RENDERER mema " + mInfo.availMem / 1024 / 1024
                    + " seconds: " + prettyPrint)
        }

//        if (listener != null) {
//            mActivity.runOnUiThread { listener!!.updateActivity()}
//        }
    }


    private fun doMatrixSetup() {
        /*
         * Set a matrix that contains the additional *incremental* rotation
         * as indicated by the user touching the screen
         */

        if (!mSelectModeFlag) {
            Matrix.setIdentityM(mIncrementalRotation, 0)
            Matrix.rotateM(mIncrementalRotation, 0, mDeltaX, 0.0f, 1.0f, 0.0f)
            Matrix.rotateM(mIncrementalRotation, 0, mDeltaY, 1.0f, 0.0f, 0.0f)
            mDeltaX = 0.0f
            mDeltaY = 0.0f

            // Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
            Matrix.multiplyMM(mTemporaryMatrix, 0, mIncrementalRotation, 0, mAccumulatedRotation, 0)
            System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16)
        }
        // Rotate the object taking the overall rotation into account.
        Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mAccumulatedRotation, 0)
        System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16)

        // This multiplies the view matrix by the model matrix, and stores
        // the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0)

        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0)

        // This multiplies the modelview matrix by the projection matrix,
        // and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0)
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16)

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0)

        // Pass in the light position in eye space.
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2])

        val glError = GLES20.glGetError()
        if (glError != GLES20.GL_NO_ERROR) {
            Timber.e("OnDrawFrame, glerror =  $glError")
        }
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
        // GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);
        Matrix.setIdentityM(mTemporaryMatrix, 0)
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mTemporaryMatrix, 0)

        // This multiplies the modelview matrix by the projection matrix,
        // and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mModelMatrix, 0)
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16)

        // Pass in the combined matrix.
        // GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mProjectionMatrix, 0)

        // Pass in the light position in eye space.
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2])

        val glError = GLES20.glGetError()
        if (glError != GLES20.GL_NO_ERROR) {
            Timber.e("OnDrawFrame, glerror =  $glError")
        }
    }

    /**
     * Draws a point representing the position of the light.
     */
    private fun drawLight() {
        val pointMVPMatrixHandle = GLES20.glGetUniformLocation(mPointProgramHandle, "u_MVPMatrix")
        val pointPositionHandle = GLES20.glGetAttribLocation(mPointProgramHandle, "a_Position")

        // Pass in the position.
        GLES20.glVertexAttrib3f(pointPositionHandle, mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2])

        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(pointPositionHandle)

        // Pass in the transformation matrix.
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mLightModelMatrix, 0)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0)
        GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0)

        // Draw the point.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)
    }

    /**
     * Helper function to compile a shader.
     *
     * @param shaderType   The shader type.
     * @param shaderSource The shader source code.
     * @return An OpenGL handle to the shader.
     */
    private fun compileShader(shaderType: Int, shaderSource: String): Int {
        var shaderHandle = GLES20.glCreateShader(shaderType)

        if (shaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderSource)

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle)

            // Get the compilation status.
            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                Timber.e("Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle))
                GLES20.glDeleteShader(shaderHandle)
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
        var programHandle = GLES20.glCreateProgram()

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle)

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle)

            // Bind attributes
            if (attributes != null) {
                val size = attributes.size
                for (i in 0 until size) {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i])
                }
            }

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle)

            // Get the link status.
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0)

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                Timber.e("Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle))
                GLES20.glDeleteProgram(programHandle)
                programHandle = 0
            }
        }

        if (programHandle == 0) {
            throw RuntimeException("Error creating program.")
        }

        return programHandle
    }

    // TODO: send message to Handler
    fun toggleShader() {
        mUseVertexShaderProgram = !mUseVertexShaderProgram
    }

    fun toggleHydrogenDisplayMode() {
        mMol.displayHydrosFlag = !mMol.displayHydrosFlag
    }

    fun toggleWireframeFlag() {
        mWireFrameRenderingFlag = !mWireFrameRenderingFlag
    }

    fun toggleSelectFlag() {

        if (mSelectModeFlag) {
            mSelectModeFlag = false
            // reset any rotation during selection movement
            mDeltaX = 0.0f
            mDeltaY = 0.0f
            mScaleCurrentF = mSaveScale
            mGlSurfaceView.mSelectMode = false
        } else {
            mSelectModeFlag = true
            mSaveScale = mScaleCurrentF
            mGlSurfaceView.mSelectMode = true
        }
    }

    fun loadPdbFile() {
        // TODO: fix this hack on detecting when OPENGL is up and running
        if (mPerVertexProgramHandle != -1) {
            mPdbFile.parse(mPdbFileName)
            /*
             * reset view manipulation
             */
            mScaleCurrentF = INITIAL_SCALE // zoom

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
            Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)
        }
    }

    // TODO: send message to Handler
    fun nextViewMode() {
        // TODO: fix this hack on detecting when OPENGL is up and running
        mManagerViewmode?.nextViewMode()
        // mActivity.changeViewIsFinished();
    }


    /**
     * Implement the interactive selection of an atom
     */
    private fun select() {

        /*
         * first render the triangles of the molecule
         */
        var scaleF = 1.5f / mMol.dcOffset
        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -2.5f)
        Matrix.scaleM(mModelMatrix, 0, scaleF, scaleF, scaleF)
        doMatrixSetup()
        mBufferManager.render(mPositionHandle, mColorHandle, mNormalHandle, mWireFrameRenderingFlag)

        // int[] mViewport = new int[]{0, 0, mWidth, mHeight};
        mViewport[2] = mWidth
        mViewport[3] = mHeight
        val x = mTouchX.toInt()
        // offset works out to 100 pixels on an Galaxy S2 (800 vertical),
        //   or 160 on a Nexus 4 (1280 vertical)
        val y = mTouchY.toInt() - (max(mHeight.toFloat(), mWidth.toFloat()) * 0.125f).toInt()

        Matrix.setIdentityM(mTemporaryMatrix, 0)

        /*        int result;
        result = GLU.gluUnProject(x, mHeight - y, 0.0f, mTemporaryMatrix, 0,
                mProjectionMatrix, 0, mViewport, 0, mFloatVector1, 0);

        result = GLU.gluUnProject(x, mHeight - y, 1.0f, mTemporaryMatrix, 0,
                mProjectionMatrix, 0, mViewport, 0, mFloatVector2, 0);*/

        mScreenVector1.setAll(
                (mFloatVector1[0] / mFloatVector1[3]).toDouble(),
                (mFloatVector1[1] / mFloatVector1[3]).toDouble(),
                (mFloatVector1[2] / mFloatVector1[3]).toDouble()
        )

        mScreenVector2.setAll(
                (mFloatVector2[0] / mFloatVector2[3]).toDouble(),
                (mFloatVector2[1] / mFloatVector2[3]).toDouble(),
                (mFloatVector2[2] / mFloatVector2[3]).toDouble()
        )

        // now draw the targeted location in screen coords
        doMatrixSetupViewOnly()

        //        String pretty_print_a = String.format("%6.2f", mScreenVector1.x);
        //        String pretty_print_b = String.format("%6.2f", mScreenVector1.y);
        //        String pretty_print_c = String.format("%6.2f", mScreenVector1.z);

        // Timber.i("pointer: " + pretty_print_a + pretty_print_b + pretty_print_c);

        mPointer!!.setup(
                mScreenVector1.x.toFloat(),
                mScreenVector1.y.toFloat(),
                -1.01f, mScaleCurrentF)

        mPointer!!.render(mPositionHandle, mColorHandle, mNormalHandle, false /* mWireFrameRenderingFlag */)

        /*
         * move the origin point of the interesecting ray out
         *   a bit in Z space - so that the origin is always
         *   towards the camera
         */
        mTempVector1.setAll(mScreenVector2)
        mTempVector1.subtract(mScreenVector1)
        mTempVector1.normalize()
        mTempVector1.multiply(2.0)
        mScreenVector1.subtract(mTempVector1)

        var selectedAtomIndex: Int

        val gotHit = false

        var atom1: PdbAtom?
        mSelectedAtomsList.clear()
        for (i in 0 until mMol.atoms.size) {
            atom1 = mMol.atoms[mMol.numList[i]]
            if (atom1 == null) {
                Timber.e("drawSpheres: error - got null for " + mMol.numList[i])
                continue
            }
            // skip HOH (water) molecules
            if (atom1.atomType == PdbAtom.IS_HETATM && atom1.residueName == "HOH") {
                continue
            }

            //            if (atom1.pickedAtom) {
            //                atom1.pickedAtom = false;
            //                mManagerViewmode.repeatViewMode();
            //            }

            mFloatVector2[0] = atom1.atomPosition.x.toFloat()
            mFloatVector2[1] = atom1.atomPosition.y.toFloat()
            mFloatVector2[2] = atom1.atomPosition.z.toFloat()
            mFloatVector2[3] = 1.0f

            // same as for rendering!!
            // Works!!
            scaleF = 1.5f / mMol.dcOffset
            Matrix.setIdentityM(mModelMatrix, 0)
            Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -2.5f)
            Matrix.scaleM(mModelMatrix, 0, scaleF, scaleF, scaleF)
            doMatrixSetup()

            Matrix.multiplyMM(mTemporaryMatrix, 0, mViewMatrix, 0, mModelMatrix, 0)
            Matrix.multiplyMV(mFloatVector1, 0, mTemporaryMatrix, 0, mFloatVector2, 0)

            if (mFloatVector1[3] == 0f) mFloatVector1[3] = 1.0f

            mTestAtomVector.setAll(
                    (mFloatVector1[0] / mFloatVector1[3]).toDouble(),
                    (mFloatVector1[1] / mFloatVector1[3]).toDouble(),
                    (mFloatVector1[2] / mFloatVector1[3]).toDouble()
            )

            /*  old way of doing the intersection:  */
            //                got_hit = Intersector.intersectRaySphere(
            //                        mScreenVector1, mScreenVector2,
            //                        mTestAtomVector, 0.10f,
            //                        screen_vector /* if a hit */);

            /* end old way of doing the intersection */

            // following along: http://paulbourke.net/geometry/circlesphere/raysphere.c

            //   -- USE only static allocation in main loop
            // Vector3 p1_minus_sc = new Vector3();
            // Vector3 dp = new Vector3();

            val p1_minus_sc = mTempVector1
            val dp = mTempVector2

            dp.setAll(mScreenVector2)
            dp.subtract(mScreenVector1)
            dp.normalize()

            val a = dp.dot(dp)

            p1_minus_sc.setAll(mScreenVector1)
            p1_minus_sc.subtract(mTestAtomVector)

            val b = 2 * dp.dot(p1_minus_sc)

            var c = mTestAtomVector.dot(mTestAtomVector)
            c += mScreenVector1.dot(mScreenVector1)
            c -= 2 * mTestAtomVector.dot(mScreenVector1)
            // c = c - (0.10 * 0.10);
            // c = c - (0.05 * 0.05);
            // mTempVector1 the "radius" of the target sphere
            //  based on the size of the molecule
            val radius = 0.05 * 15.0 / mMol.dcOffset
            c -= radius * radius

            val bb4ac = b * b - 4.0 * a * c

            /*
             * if >= 0 then we have a hit
             */
            if (bb4ac >= 0) {

                val selectedAtom = AtomInfo()
                selectedAtom.atomTransformedPosition.setAll(mTestAtomVector)
                selectedAtom.indexNumber = i
                mSelectedAtomsList.add(selectedAtom)
            }
        }

        if (mSelectedAtomsList.isEmpty()) {
            return
        }
        var maxZ = -999999.0
        selectedAtomIndex = 999999
        var ai: AtomInfo
        for (i in mSelectedAtomsList.indices) {
            ai = mSelectedAtomsList[i]
            val zvalue = ai.atomTransformedPosition.z
            if (zvalue > maxZ) {
                selectedAtomIndex = ai.indexNumber
                maxZ = zvalue
            }
        }

        //        if (atom1.atom_number != mLastReportedAtom) {
        //            Log.i("SEL", "atom " + atom1.atom_number);
        //            mLastReportedAtom = atom1.atom_number;
        //        }


        // pop in a cube - try to surround the picked atom
        scaleF = 1.5f / mMol.dcOffset
        // cube is 8X a molecule
        // scaleF = scaleF / 8.0f;
        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -2.5f)
        Matrix.scaleM(mModelMatrix, 0, scaleF, scaleF, scaleF)


        atom1 = mMol.atoms[mMol.numList[selectedAtomIndex]]
        doMatrixSetup()
        mCube!!.setup(
                atom1!!.atomPosition.x.toFloat(),
                atom1.atomPosition.y.toFloat(),
                atom1.atomPosition.z.toFloat())
        mCube!!.render(mPositionHandle, mColorHandle, mNormalHandle, false /* mWireFrameRenderingFlag */)

        if (atom1.atomNumber != mLastReportedAtom) {
            Timber.i("atom " + atom1.atomNumber)
            mLastReportedAtom = atom1.atomNumber
        }


    }

    fun doCleanUp() {
        mBufferManager.resetBuffersForNextUsage()
        mMol.clearLists()
        Timber.i("CLEANUP")
    }

    fun setPdbFileName(name: String) {
        mPdbFileName = name
    }

    fun loadPdbFromStream(inputStream: InputStream) {
        mPdbFile.loadPdbFromStream(inputStream)
    }

    /**
     *  read the GL frame buffer and convert the bits
     *  to an Android compatible bitmap.
     *  Convert RGB(#000000) to transparent
     *  @link https://stackoverflow.com/a/5013141/3853712
     */
    fun readGlBufferToBitmap(x: Int, y: Int, w: Int, h: Int): Bitmap? {
        val b = IntArray(w * (y + h))
        val bt = IntArray(w * h)
        val ib: IntBuffer = IntBuffer.wrap(b)
        ib.position(0)
        glReadPixels(x, 0, w, y + h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib)
        val glError = GLES20.glGetError()
        if (glError != GLES20.GL_NO_ERROR) {
            Timber.e("OnDrawFrame, glerror =  $glError")
        }
        var i = 0
        var k = 0
        while (i < h) {
            //remember, that OpenGL bitmap is incompatible with Android bitmap
            //and so, some correction need.
            for (j in 0 until w) {
                var pix = b[(i + y) * w + j]
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
        private val mScreenVector1 = Vector3()
        private val mScreenVector2 = Vector3()
        private val mTempVector1 = Vector3()
        private val mTempVector2 = Vector3()
        private val mTestAtomVector = Vector3()

        private val mSelectedAtomsList = ArrayList<AtomInfo>()

        private const val INITIAL_SCALE = 0.5f
        private var mSaveScale = 0f

        private var mHeight: Int = 0
        private var mWidth: Int = 0
        private var mLastReportedAtom = -1
        private val mViewport = intArrayOf(0, 0, 0, 0)
    }

    //    public void updatePdbFinishedParsing() {
    //        /*
    //         * let the UI know that the parsing is completed (to flush the spinner)
    //         */
    //        Message message;
    //        message = Message.obtain(mHandler, UI_MESSAGE_FINISHED_PARSING);
    //        mHandler.dispatchMessage(message);
    //    }
}