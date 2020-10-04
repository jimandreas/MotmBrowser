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

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.bammellab.mollib.GLSurfaceViewDisplayPdbFile
import com.bammellab.mollib.RendererDisplayPdbFile
import com.bammellab.mollib.protein.Molecule.Companion.UI_MESSAGE_FINISHED_PARSING
import com.bammellab.mollib.protein.Molecule.Companion.UI_MESSAGE_FINISHED_VIEW_CHANGE
import com.bammellab.mollib.protein.Molecule.Companion.UI_MESSAGE_GL_READY
import com.bammellab.motm.util.PdbCache
import timber.log.Timber
import java.io.InputStream


/**
 * GraphicsActivity
 * The list of PDB entries for this particular Molecule of the Month
 * is passed in the Intent Extras.   The extras contain both a
 * string array of the PDB designators and an index into the array
 * for the one to be displayed initially.
 */

class GraphicsActivity : AppCompatActivity(), PdbCache.PdbCallback {
    private lateinit var buttonPreviousObj: Button
    private lateinit var buttonNextObj: Button
    private lateinit var buttonSelect: Button
    private lateinit var buttonChangeViewmode: Button
    private lateinit var gLSurfaceView: GLSurfaceViewDisplayPdbFile
    private lateinit var nextViewProgressCircle: ProgressBar

    private lateinit var mRenderer: RendererDisplayPdbFile
    private lateinit var pdbList: Array<String>
    private var currentPdbIndex: Int = 0

    private lateinit var pdbCache: PdbCache

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val handler = Handler(
//                Looper.getMainLooper(), this)

        pdbList = intent.getStringArrayExtra(INTENT_TAG_LIST)
        currentPdbIndex = intent.getIntExtra(INTENT_TAG_INDEX, 0)

        setContentView(R.layout.activity_graphics)
        buttonPreviousObj = findViewById(R.id.button_prev_obj)
        buttonNextObj = findViewById(R.id.button_next_obj)
        buttonSelect = findViewById(R.id.button_select)
        buttonChangeViewmode = findViewById(R.id.button_change_viewmode)
        gLSurfaceView = findViewById(R.id.gl_surface_view)
        nextViewProgressCircle = findViewById(R.id.next_view_progress_circle)

        pdbCache = PdbCache(this, gLSurfaceView.context)

        //        gLSurfaceView = (GLSurfaceViewDisplayPdbFile) findViewById(R.id.gl_surface_view);
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

            mRenderer = RendererDisplayPdbFile(this, gLSurfaceView)
            gLSurfaceView.setRenderer(mRenderer, displayMetrics.density)

            // This freezes the updates, now adjusted in GLSurfaceView
            // gLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        } else {
            Timber.e("No support of OPENGL ES2")
            Toast.makeText(this, "Need OpenGL2 support, sorry", Toast.LENGTH_LONG)
                    .show()
            return
        }

        // TODO: fully implement "select".  For now turn it off
        buttonSelect.visibility = View.GONE
        buttonSelect.isClickable = false

        /*
         * Go to next PDB in the list
         */
        buttonNextObj.setOnClickListener(View.OnClickListener {
            if (pdbList.isEmpty() || currentPdbIndex < 0 || currentPdbIndex > pdbList.size) {
                Timber.e("Error with pdb list: list size: %d index requested: %d",
                        pdbList.size, currentPdbIndex)
                return@OnClickListener
            }

            nextViewProgressCircle.visibility = View.VISIBLE

            if (++currentPdbIndex == pdbList.size) {
                currentPdbIndex = 0
            }
            title = pdbList[currentPdbIndex]
            pdbCache.downloadPdb(pdbList[currentPdbIndex])
        })

        /*
         * Go to previous PDB in the list
         */
        buttonPreviousObj.setOnClickListener(View.OnClickListener {
            if (pdbList.isEmpty() || currentPdbIndex < 0 || currentPdbIndex > pdbList.size) {
                Timber.e("Error with pdb list: list size: %d index requested: %d",
                        pdbList.size, currentPdbIndex)
                return@OnClickListener
            }

            nextViewProgressCircle.visibility = View.VISIBLE

            if (--currentPdbIndex < 0) {
                currentPdbIndex = pdbList.size - 1
            }
            title = pdbList[currentPdbIndex]
            pdbCache.downloadPdb(pdbList[currentPdbIndex])
        })

        buttonSelect.setOnClickListener { toggleSelect() }

        // TODO: update this spinnner
        buttonChangeViewmode.setOnClickListener {
            nextViewProgressCircle.visibility = View.VISIBLE
            gLSurfaceView.queueEvent { mRenderer.nextViewMode() }
        }

        /*
         * set the Title as the PDB code
         */
        try {
            title = pdbList[currentPdbIndex]
        } catch (e: Exception) {
            Timber.e(e, "failure on pdb file indexing")
        }

    }

    override fun onResume() {
        super.onResume()
        gLSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        gLSurfaceView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRenderer.doCleanUp()
    }

    /**
     * Queue up the PDB parsing in the library
     *
     * @param stream input stream from cache or from http
     */
    override fun loadPdbFromStream(stream: InputStream) {
        gLSurfaceView.queueEvent { mRenderer.loadPdbFromStream(stream) }
    }

    private fun toggleShader() {
        gLSurfaceView.queueEvent { mRenderer.toggleShader() }
    }

    private fun toggleHydrogenDisplayMode() {
        gLSurfaceView.queueEvent { mRenderer.toggleHydrogenDisplayMode() }
    }

    private fun toggleWireframe() {
        gLSurfaceView.queueEvent { mRenderer.toggleWireframeFlag() }
    }

    private fun toggleSelect() {
        gLSurfaceView.queueEvent { mRenderer.toggleSelectFlag() }
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


    /**
     * receive messages from the renderer
     * Currently:
     * GL_READY - the GL components are created - instance state can
     * now be set
     * UPDATE_RPM - RPM indicator - to be implemented
     *
     * @param msg message sent from renderer
     * @return true - TODO: understand if this means something
     */

//    override fun handleMessage(msg: Message): Boolean {
//
//        // http://stackoverflow.com/a/27659565/3853712
//
//
//        when (msg.what) {
//            UI_MESSAGE_GL_READY -> {
//                pdbCache.downloadPdb(pdbList[currentPdbIndex])
//                runOnUiThread { nextViewProgressCircle.visibility = View.VISIBLE }
//            }
//            UI_MESSAGE_FINISHED_PARSING, UI_MESSAGE_FINISHED_VIEW_CHANGE -> runOnUiThread { nextViewProgressCircle.visibility = View.INVISIBLE }
//        }
//        return true
//    }

    companion object {
        const val INTENT_TAG_LIST = "pdbList"
        const val INTENT_TAG_INDEX = "pdb_list_index"
    }
}