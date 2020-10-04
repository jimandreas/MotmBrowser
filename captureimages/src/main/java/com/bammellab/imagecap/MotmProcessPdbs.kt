package com.bammellab.imagecap

import android.graphics.Bitmap
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.bammellab.mollib.GLSurfaceViewDisplayPdbFile
import com.bammellab.mollib.RendererDisplayPdbFile
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
) {
    private val activity = activityIn
    private val glSurfaceView = glSurfaceViewIn
    private val renderer = rendererIn

    private var nextNameIndex = -1

    private val pdbFileNames = MotmPdbNames.pdbNames

    init {
        checkFiles()
    }

    private fun checkFiles() = runBlocking {
        launch(Dispatchers.IO) { // will get dispatched to ForkJoinPool.commonPool (or equivalent)
            Timber.v("checkFiles: I'm working in thread ${Thread.currentThread().name}")
            val currentTime = System.currentTimeMillis()
            var missingCount = 0
            val path = "/mnt/sdcard/PDB"
            for (name in pdbFileNames) {
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

    fun startProcessing() {
        Timber.v("startProcessing: thread ${Thread.currentThread().name}")
    }

    private fun loadNextPdbFile() {
        if (++nextNameIndex == pdbFileNames.size) {
            nextNameIndex = 0
        }
        val name = pdbFileNames[nextNameIndex]
        Timber.d("Next file: %s", name)

        activity.title = pdbFileNames[nextNameIndex]
        renderer.setPdbFileName(name)

        // WRITE the image!
        if (nextNameIndex > 0) {
            writeCurrentImage()
        }

        glSurfaceView.queueEvent { renderer.loadPdbFile() }
    }

    private fun loadPrevPdbFile() {

        if (nextNameIndex-- == 0) {
            nextNameIndex = pdbFileNames.size - 1
        }
        val name = pdbFileNames[nextNameIndex]

        Timber.d("Previous file: %s", name)

        activity.title = pdbFileNames[nextNameIndex]
        renderer.setPdbFileName(name)

        glSurfaceView.queueEvent { renderer.loadPdbFile() }
    }

    private fun writeCurrentImage() {

        glSurfaceView.queueEvent {
            val file = getDiskCacheDir("foo")
            try {
                val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val pdbName = pdbFileNames[nextNameIndex]
                val myFile = File(folder, "$pdbName.png")
                val fileOutputStream = FileOutputStream(myFile)
                val bm = renderer.readGlBufferToBitmap(200, 500, 700, 700)
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

}