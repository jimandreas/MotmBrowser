@file:Suppress("unused")

package com.bammellab.captureimages

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.bammellab.mollib.*
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class ActivityImageCap : AppCompatActivity(), UpdateRenderFinished {

    private lateinit var glSurfaceView: GLSurfaceViewDisplayPdbFile
    private lateinit var renderer: RendererDisplayPdbFile
    private lateinit var processPdbs : MotmProcessPdbs

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_pdb_file)

        glSurfaceView = findViewById(R.id.gl_surface_view)

        if (!checkForOpengl(this)) {
            failDialog(this,
                    R.string.activity_support_requirement,
                    R.string.activity_support_opegl)
            return
        }

        // Request an OpenGL ES 2.0 compatible context.
        glSurfaceView.setEGLContextClientVersion(2)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        renderer = RendererDisplayPdbFile(this, glSurfaceView)
        glSurfaceView.setRenderer(renderer, displayMetrics.density)

        // This freezes the updates, now adjusted in GLSurfaceView
        // gLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        processPdbs = MotmProcessPdbs(
                this,
                glSurfaceView,
                renderer,
                MotmPdbNames.pdbNames,
                loadPdbFromAssetsIn = false)

        processPdbs.startProcessing()
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

    fun noMemoryForAtomView() {
        runOnUiThread {
            Snackbar.make(findViewById(R.id.frame_layout),
                    "Not Enough Mem for Atom View", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun updateActivity() {
        Timber.e("Update Activity")
    }
}
