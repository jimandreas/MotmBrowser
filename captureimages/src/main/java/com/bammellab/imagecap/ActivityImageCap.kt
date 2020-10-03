//@file:Suppress("unused", "FunctionName", "IllegalIdentifier")
@file:Suppress("unused")
package com.bammellab.imagecap

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.os.*
import android.os.Environment.DIRECTORY_PICTURES
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.bammellab.mollib.GLSurfaceViewDisplayPdbFile
import com.bammellab.mollib.RendererDisplayPdbFile
import com.bammellab.mollib.UpdateRenderFinished
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


//@Suppress("UsePropertyAccessSyntax", "UNUSED_VARIABLE", "unused", "UNUSED_PARAMETER", "DEPRECATION")
//@SuppressWarnings("UnusedParameters", "unused") // Parameters inspected reflectively.

class ActivityImageCap : AppCompatActivity(), UpdateRenderFinished {
    /** Hold a reference to our GLSurfaceView  */
    private var mGLSurfaceView: GLSurfaceViewDisplayPdbFile? = null
    private var mRenderer: RendererDisplayPdbFile? = null
    private var mNextViewProgress: ProgressBar? = null

    private var mNextNameIndex = -1
    // wire in the names and display names
    private val pdbFileNames2 = arrayOf(

            "1bna",
            "1bna",
            "1bna",
            "1bna",
            "1bna"
    )

    private val pdbFileNames = MotmPdbNames.pdbNames

    private fun loadNextPdbFile() {
        if (++mNextNameIndex == pdbFileNames.size) {
            mNextNameIndex = 0
        }
        val name = pdbFileNames[mNextNameIndex]
        Timber.d("Next file: %s", name)
        //        setTitle(pdb_file_display_name[mNextNameIndex]);
        title = pdbFileNames[mNextNameIndex]
        mRenderer!!.setPdbFileName(name)

        // WRITE the image!!!
        if (mNextNameIndex > 0) {
            writeCurrentImage()
        }

        mGLSurfaceView!!.queueEvent { mRenderer!!.loadPdbFile() }
    }

    private fun loadPrevPdbFile() {

        if (mNextNameIndex-- == 0) {
            mNextNameIndex = pdbFileNames.size - 1
        }
        val name = pdbFileNames[mNextNameIndex]

        Timber.d("Previous file: %s", name)
        //        setTitle(pdb_file_display_name[mNextNameIndex]);
        title = pdbFileNames[mNextNameIndex]
        mRenderer!!.setPdbFileName(name)
        // mRenderer!!.listener = this

        mGLSurfaceView!!.queueEvent { mRenderer!!.loadPdbFile() }
    }

    private fun writeCurrentImage() {

        mGLSurfaceView!!.queueEvent {
            val file = getDiskCacheDir("foo")
            try {
                val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val pdbName = pdbFileNames[mNextNameIndex]
                val myFile = File(folder, "$pdbName.png")
                val fileOutputStream = FileOutputStream(myFile)
                val bm = mRenderer!!.readGlBufferToBitmap(200, 500, 700, 700)
                if (bm != null) {
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()
                }

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getDiskCacheDir(uniqueName: String): File {

        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir

        val state = Environment.getExternalStorageState()
        val cachePath: String
        val fullPath: String

        cachePath = if (Environment.MEDIA_MOUNTED == state || !Environment.isExternalStorageRemovable()) {

            // File cacheDir = context.getExternalCacheDir();
            val cacheDir = getExternalFilesDir(DIRECTORY_PICTURES)
            if (cacheDir != null) {
                getExternalFilesDir(DIRECTORY_PICTURES)!!.path
            } else {
                filesDir.path
            }
        } else {
            cacheDir.path
        }

        fullPath = cachePath + File.separator + uniqueName
        return File(fullPath)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_pdb_file)

        mGLSurfaceView = findViewById(R.id.gl_surface_view)


        // Check if the system supports OpenGL ES 2.0.
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            mGLSurfaceView!!.setEGLContextClientVersion(2)

            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)

            mRenderer = RendererDisplayPdbFile(this, mGLSurfaceView!!)
            mGLSurfaceView!!.setRenderer(mRenderer!!, displayMetrics.density)
            // This freezes the updates, now adjusted in GLSurfaceView
            // mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        } else {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return
        }

        Timber.v("onCreate - loadNext")
        loadNextPdbFile()


    }


    override fun onResume() {
        // The activity must call the GL surface view's onResume() on activity
        // onResume().
        super.onResume()
        mGLSurfaceView!!.onResume()
    }

    override fun onPause() {
        // The activity must call the GL surface view's onPause() on activity
        // onPause().
        super.onPause()
        mGLSurfaceView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRenderer!!.doCleanUp()
    }

    private fun toggleShader() {
        mGLSurfaceView!!.queueEvent { mRenderer!!.toggleShader() }
    }

    private fun toggleHydrogenDisplayMode() {
        mGLSurfaceView!!.queueEvent { mRenderer!!.toggleHydrogenDisplayMode() }
    }

    private fun toggleWireframe() {
        mGLSurfaceView!!.queueEvent { mRenderer!!.toggleWireframeFlag() }
    }

    private fun toggleSelect() {
        mGLSurfaceView!!.queueEvent { mRenderer!!.toggleSelectFlag() }
    }

    fun changeViewIsFinished() {
        runOnUiThread { mNextViewProgress!!.visibility = View.INVISIBLE }
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

    override fun updateActivity() {
        loadNextPdbFile()
    }
}