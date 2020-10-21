//@file:Suppress("unused", "FunctionName", "IllegalIdentifier")
@file:Suppress("unused")

package com.bammellab.standalone

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.bammellab.mollib.*
import com.bammellab.mollib.LoadFromSource.FROM_ASSETS
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


//@Suppress("UsePropertyAccessSyntax", "UNUSED_VARIABLE", "unused", "UNUSED_PARAMETER", "DEPRECATION")
//@SuppressWarnings("UnusedParameters", "unused") // Parameters inspected reflectively.

class ActivityDisplayPdbFile : AppCompatActivity(), UpdateRenderFinished {
    /** Hold a reference to our GLSurfaceView  */
    private lateinit var glSurfaceView: GLSurfaceViewDisplayPdbFile
    private lateinit var renderer: RendererDisplayPdbFile
    private lateinit var processPdbs: MollibProcessPdbs

    private var nextViewProgress: ProgressBar? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_pdb_file)

        glSurfaceView = findViewById(R.id.gl_surface_view)
        nextViewProgress = findViewById(R.id.next_view_progress_circle)

        if (!checkForOpengl(this)) {
            failDialog(this,
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

        // This freezes the updates, now adjusted in GLSurfaceView
        // glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        processPdbs = MollibProcessPdbs(
                this,
                glSurfaceView,
                renderer,
                pdbFileNames2,
                source = FROM_ASSETS)

        processPdbs.startProcessing()


        findViewById<View>(R.id.button_next_obj).setOnClickListener { processPdbs.loadNextPdbFile() }

        findViewById<View>(R.id.button_prev_obj).setOnClickListener { processPdbs.loadPrevPdbFile() }

        findViewById<View>(R.id.button_select).setOnClickListener { toggleSelect() }

        findViewById<View>(R.id.button_change_viewmode).setOnClickListener {
            processPdbs.nextViewMode()
        }
    }

    private var nextNameIndex = -1

    // wire in the names and display names
    private val pdbFileNames2 = listOf(

            "1aew_firstloop",
            "1f6g",

            "1bna",
            "1aew",

            "1AGU_simple",
            "1amb",
            // "1bn0", // boring
            "1bn1",
            "1ei0",
            "1gfl",
            "1mat",
            "1mfk_rna",
            "1adc",
            "1x1r"
    )

    override fun onResume() {
        // The activity must call the GL surface view's onResume() on activity
        // onResume().
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        // The activity must call the GL surface view's onPause() on activity
        // onPause().
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

    fun changeViewIsFinished() {
        runOnUiThread { nextViewProgress!!.visibility = View.INVISIBLE }
    }


    fun noMemoryForAtomView() {
        runOnUiThread {
            Snackbar.make(findViewById(R.id.frame_layout),
                    "Not Enough Mem for Atom View", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    // no button anymore for shader
    fun updateShaderStatus() {}


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_wireframe) {
            //            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            //            startActivity(intent);
            // Timber.w("wireframe");
            toggleWireframe()
            return true
        }
        //        if (id == R.id.action_select ) {
        //            toggleSelect(); // toggle for now
        //            return true;
        //        }

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
//        private val LOG_TAG = "activity"
    }

    override fun updateActivity(name: String) {
        Timber.v("updateActivity")
    }

}
