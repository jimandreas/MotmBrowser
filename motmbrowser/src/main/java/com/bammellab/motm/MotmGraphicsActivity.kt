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

@file:Suppress("unused", "unused_variable", "unused_parameter")
package com.bammellab.motm

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.bammellab.mollib.*
import com.bammellab.mollib.LoadFromSource.FROM_CACHE
import com.bammellab.motm.util.PdbCache
import com.bammellab.motm.util.Utility.failDialog
import timber.log.Timber
import java.io.InputStream


/**
 * GraphicsActivity
 * The list of PDB entries for this particular Molecule of the Month
 * is passed in the Intent Extras.   The extras contain both a
 * string array of the PDB designators and an index into the array
 * for the one to be displayed initially.
 */

class MotmGraphicsActivity : AppCompatActivity(), PdbCache.PdbCallback {
    private lateinit var buttonPreviousObj: Button
    private lateinit var buttonNextObj: Button
    private lateinit var buttonSelect: Button
    private lateinit var buttonChangeViewmode: Button
    private lateinit var nextViewProgressCircle: ProgressBar

    private lateinit var glSurfaceView: GLSurfaceViewDisplayPdbFile
    private lateinit var renderer: RendererDisplayPdbFile

    private lateinit var processPdbs: MollibProcessPdbs

    private var pdbList: Array<String>? = null
    private var currentPdbIndex: Int = 0

    private lateinit var pdbCache: PdbCache

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val handler = Handler(
//                Looper.getMainLooper(), this)

        pdbList = intent.getStringArrayExtra(INTENT_TAG_LIST)
        currentPdbIndex = intent.getIntExtra(INTENT_TAG_INDEX, 0)
        if (pdbList == null) {
            Timber.e("An empty list was passed to the MotmGraphicsActivity, giving up")
            return
        }

        // TODO: complain dialog if the list is empty

        setContentView(R.layout.activity_graphics)
        buttonPreviousObj = findViewById(R.id.button_prev_obj)
        buttonNextObj = findViewById(R.id.button_next_obj)
        buttonSelect = findViewById(R.id.button_select)
        buttonChangeViewmode = findViewById(R.id.button_change_viewmode)
        glSurfaceView = findViewById(R.id.gl_surface_view)
        nextViewProgressCircle = findViewById(R.id.next_view_progress_circle)

        pdbCache = PdbCache(this, glSurfaceView.context)

        /*//        gLSurfaceView = (GLSurfaceViewDisplayPdbFile) findViewById(R.id.gl_surface_view);
        //        nextViewProgressCircle = (ProgressBar) findViewById(R.id.next_view_progress_circle);

        // Check if the system supports OpenGL ES 2.0.
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            gLSurfaceView.setEGLContextClientVersion(2)

            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)

            renderer = RendererDisplayPdbFile(this, gLSurfaceView)
            gLSurfaceView.setRenderer(renderer, displayMetrics.density)

            // This freezes the updates, now adjusted in GLSurfaceView
            // gLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        } else {
            Timber.e("No support of OPENGL ES2")
            Toast.makeText(this, "Need OpenGL2 support, sorry", Toast.LENGTH_LONG)
                    .show()
            return
        }*/

        if (!checkForOpengl(this)) {
            failDialog(this,
                    R.string.activity_support_requirement,
                    R.string.activity_support_requirement
            )
            return
        }

        // Request an OpenGL ES 2.0 compatible context.
        glSurfaceView.setEGLContextClientVersion(2)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        renderer = RendererDisplayPdbFile(this, glSurfaceView)
        glSurfaceView.setRenderer(renderer, displayMetrics.density)

        // TODO: fully implement "select".  For now turn it off
        buttonSelect.visibility = View.GONE
        buttonSelect.isClickable = false

        // This freezes the updates, now adjusted in GLSurfaceView
        // glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        processPdbs = MollibProcessPdbs(
                this,
                glSurfaceView,
                renderer,
                pdbList!!.toList(),
                source = FROM_CACHE)

        /*
        * Go to next PDB in the list
        */
        buttonNextObj.setOnClickListener(View.OnClickListener {
            if (pdbList!!.isEmpty() || currentPdbIndex < 0 || currentPdbIndex > pdbList!!.size) {
                Timber.e("Error with pdb list: list size: %d index requested: %d",
                        pdbList!!.size, currentPdbIndex)
                return@OnClickListener
            }

            // mNextViewProgressCircle.visibility = View.VISIBLE

            if (++currentPdbIndex == pdbList!!.size) {
                currentPdbIndex = 0
            }
            title = pdbList!![currentPdbIndex]
            pdbCache.downloadPdb(pdbList!![currentPdbIndex])
        })

        /*
         * Go to previous PDB in the list
         */
        buttonPreviousObj.setOnClickListener(View.OnClickListener {
            if (pdbList!!.isEmpty() || currentPdbIndex < 0 || currentPdbIndex > pdbList!!.size) {
                Timber.e("Error with pdb list: list size: %d index requested: %d",
                        pdbList!!.size, currentPdbIndex)
                return@OnClickListener
            }

            //mNextViewProgressCircle.visibility = View.VISIBLE

            if (--currentPdbIndex < 0) {
                currentPdbIndex = pdbList!!.size - 1
            }
            title = pdbList!![currentPdbIndex]
            pdbCache.downloadPdb(pdbList!![currentPdbIndex])
        })


        buttonSelect.setOnClickListener { toggleSelect() }

        // TODO: update this spinnner
//        buttonChangeViewmode.setOnClickListener {
//            nextViewProgressCircle.visibility = View.VISIBLE
//            gLSurfaceView.queueEvent { mRenderer.nextViewMode() }
//        }

    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        renderer.doCleanUp()
    }

    /**
     * Queue up the PDB parsing in the library
     *
     * @param stream input stream from cache or from http
     */
    override fun loadPdbFromStream(stream: InputStream) {

    }

    private fun toggleShader() {
        glSurfaceView.queueEvent { renderer.toggleShader() }
    }

    private fun toggleHydrogenDisplayMode() {
        glSurfaceView.queueEvent { renderer.toggleHydrogenDisplayMode() }
    }

    private fun toggleWireframe() {
        glSurfaceView.queueEvent { renderer.toggleWireframeFlag() }
    }

    private fun toggleSelect() {
        glSurfaceView.queueEvent { renderer.toggleSelectFlag() }
    }

    fun changeViewIsFinished() {
        runOnUiThread { nextViewProgressCircle.visibility = View.INVISIBLE }
    }

    fun noMemoryForAtomView() {
        runOnUiThread {
            Snackbar.make(findViewById(R.id.activity_graphics_layout),
                    "Not Enough Mem for Atom View", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_wireframe) {
            toggleWireframe()
            return true
        }

        if (id == R.id.action_shading) {
            toggleShader() // toggle for now
            return true
        }

        if (id == R.id.action_hydrogens) {
            toggleHydrogenDisplayMode() // toggle for now
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val INTENT_TAG_LIST = "pdbList"
        const val INTENT_TAG_INDEX = "pdb_list_index"
    }
}