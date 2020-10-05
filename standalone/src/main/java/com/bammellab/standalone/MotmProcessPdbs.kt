package com.bammellab.standalone

import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.bammellab.mollib.GLSurfaceViewDisplayPdbFile
import com.bammellab.mollib.ManagePdbFile
import com.bammellab.mollib.RendererDisplayPdbFile
import com.bammellab.mollib.SurfaceCreated
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class MotmProcessPdbs(
        activityIn: AppCompatActivity,
        glSurfaceViewIn: GLSurfaceViewDisplayPdbFile,
        rendererIn: RendererDisplayPdbFile
) : SurfaceCreated {
    private val activity = activityIn
    private val glSurfaceView = glSurfaceViewIn
    private val renderer = rendererIn
    private val managePdbFile = ManagePdbFile(activityIn, glSurfaceViewIn)

    private var nextNameIndex = -1

    val myPdbFileNames = listOf(
            "1bna",
            "1bbl",
            "4pti"

    )


    init {
        checkFiles()
        rendererIn.setSurfaceCreatedListener(this)
        managePdbFile.setup()
    }

    fun startProcessing() {
        Timber.v("startProcessing: thread ${Thread.currentThread().name}")
    }

    override fun surfaceCreatedCallback() {
        Timber.e("SURFACE CREATED CALLBACK")
        loadNextPdbFile()
    }

    // private fun checkFiles() = runBlocking {
    private fun loadNextPdbFile() = runBlocking {
        launch(Dispatchers.IO) {
            if (++nextNameIndex == myPdbFileNames.size) {
                nextNameIndex = 0
            }
            val name = myPdbFileNames[nextNameIndex]
            Timber.d("Next file: %s", name)

            managePdbFile.parsePdbFile(name)

            launch(Dispatchers.Main) {
                activity.title = name
            }

            // renderer.setPdbFileName(name)

//            // WRITE the image!
//            if (nextNameIndex > 0) {
//                writeCurrentImage()
//            }

            //glSurfaceView.queueEvent { renderer.loadPdbFile() }
            Thread.sleep(1000)
            Timber.e("SETTING DIRTY FLAG")
            glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        }

    }

    private fun loadPrevPdbFile() {

        if (nextNameIndex-- == 0) {
            nextNameIndex = myPdbFileNames.size - 1
        }
        val name = myPdbFileNames[nextNameIndex]

        Timber.d("Previous file: %s", name)

        activity.title = myPdbFileNames[nextNameIndex]
        // renderer.setPdbFileName(name)

        // glSurfaceView.queueEvent { renderer.loadPdbFile() }
    }

    /**
     *
     * @link: https://gist.github.com/granoeste/5574148
     */
    private fun getDiskCacheDir(uniqueName: String): File {

        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir

        val state = Environment.getExternalStorageState()
        val cachePath: String
        val fullPath: String

        cachePath = if (Environment.MEDIA_MOUNTED == state || !Environment.isExternalStorageRemovable()) {

            // File cacheDir = context.getExternalCacheDir();
            val cacheDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (cacheDir != null) {
                activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.path
            } else {
                activity.filesDir.path
            }
        } else {
            activity.cacheDir.path
        }

        fullPath = cachePath + File.separator + uniqueName
        return File(fullPath)
    }

    private fun checkFiles() = runBlocking {
        launch(Dispatchers.IO) { // will get dispatched to ForkJoinPool.commonPool (or equivalent)
            Timber.v("checkFiles: I'm working in thread ${Thread.currentThread().name}")
            val currentTime = System.currentTimeMillis()
            var missingCount = 0
            val path = "/mnt/sdcard/PDB"
            for (name in myPdbFileNames) {
                val file = File(path, "$name.pdb")
                if (!file.exists()) {
                    if (missingCount < 10) {
                        Timber.e("$file is missing from $path")
                    }
                    missingCount++
                }
            }
            if (missingCount > 0) {
                Timber.e("There were $missingCount missing files (%d ms)",
                        System.currentTimeMillis() - currentTime)
            } else {
                Timber.v("There were $missingCount missing files (%d ms)",
                        System.currentTimeMillis() - currentTime)
            }
        }
    }


}
