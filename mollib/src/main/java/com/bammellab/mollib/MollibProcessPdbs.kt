/*
 *  Copyright 2020 Bammellab / James Andreas
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

@file:Suppress("UNUSED_VARIABLE")

package com.bammellab.mollib

import com.bammellab.mollib.pdbDownload.PdbCallback
import com.bammellab.mollib.pdbDownload.PdbDownload
import android.graphics.Bitmap
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bammellab.mollib.LoadFromSource.*
import com.bammellab.mollib.objects.ManagerViewmode
import com.kotmol.pdbParser.Molecule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.*

enum class LoadFromSource {
    FROM_ASSETS,
    FROM_SDCARD,
    FROM_RCSB_OR_CACHE
}

/**
 * common code to parse and display PDB info
 *   from a PDB folder or from a local asset file.
 */
class MollibProcessPdbs(
        private val activity: AppCompatActivity,
        private val glSurfaceView: GLSurfaceViewDisplayPdbFile,
        private val renderer: RendererDisplayPdbFile,
        private val pdbFileNames: List<String>,
        private val loadPdbFrom: LoadFromSource
) : SurfaceCreated, PdbCallback {
    private val managePdbFile = ManagePdbFile(activity)
    private var managerViewmode: ManagerViewmode? = null
    private var nextNameIndex = -1
    private var captureImagesFlag = false
    private var androidFilePath = ""
    private lateinit var pdbDownload: PdbDownload
    private lateinit var mol : Molecule

    init {
        if (loadPdbFrom == FROM_SDCARD) {
            checkFiles()
        }
        renderer.setSurfaceCreatedListener(this)
    }

    fun startProcessing(captureImages: Boolean = false) {
        Timber.v("startProcessing: thread ${Thread.currentThread().name}")
        captureImagesFlag = captureImages
    }

    /**
     * at the time of surfaceCreatedCallback() the Opengl system is up an running
     * If FROM_SDCARD mode is set, then the goal is to capture images.
     * If FROM_CACHE mode is set, then PDBs are to be downloaded
     */
    override fun surfaceCreatedCallback() {
        Timber.e("SURFACE CREATED CALLBACK")
        when (loadPdbFrom) {
            FROM_SDCARD -> renderer.allocateReadBitmapArrays()
            FROM_RCSB_OR_CACHE -> {
                pdbDownload = PdbDownload(activity)
                pdbDownload.initPdbCallback(this)
            }
            else -> {}
        }
        loadNextPdbFile()
    }

    fun loadNextPdbFile() {
        if (++nextNameIndex == pdbFileNames.size) {
            nextNameIndex = 0
        }
        Timber.v("Next file: %s", pdbFileNames[nextNameIndex])
        commonStuff()
    }

    fun loadPrevPdbFile() {
        if (nextNameIndex-- == 0) {
            nextNameIndex = pdbFileNames.size - 1
        }
        Timber.v("Prev file: %s", pdbFileNames[nextNameIndex])
        commonStuff()
    }

    private fun commonStuff() = runBlocking {
        launch(Dispatchers.IO) {
            //mutex.withLock {
            val name = pdbFileNames[nextNameIndex]
            activity.runOnUiThread { activity.title = name }
            renderer.tossMoleculeToGC()
            mol = Molecule() // the one place where Molecule is allocated!!
            managerViewmode = ManagerViewmode(
                    activity, mol)

            when (loadPdbFrom) {
                FROM_ASSETS -> {managePdbFile.parsePdbFileFromAsset(name, mol)}
                FROM_SDCARD -> {
                    try {
                        val myFile = File(androidFilePath, "$name.pdb")
                        if (!myFile.exists()) {
                            Timber.e("nope $myFile does not exist")
                        } else {
                            Timber.i("Yay $myFile exists")
                        }
                        val fileStream = FileInputStream(myFile)

                        managePdbFile.parsePdbFile(fileStream, mol, name)
                        fileStream.close()

                    }catch (e: AccessDeniedException) {
                        Timber.e(e, "$name ACCESS DENIED!!!!!!!!!!!!")
                    }
                    catch (e: FileNotFoundException) {
                        Timber.e(e, "$name not found")
                    } catch (e: IOException) {
                        Timber.e(e, "$name IO Exception")
                    }

                    glSurfaceView.queueEvent {
                        managerViewmode!!.createView()
                        renderer.setMolecule(mol)
                        renderer.resetCamera()
                    }
                }
                FROM_RCSB_OR_CACHE -> {
                    pdbDownload.downloadPdb(name)
                }
            }


        }
    }

    fun nextViewMode() {
        if (managerViewmode != null) {
            glSurfaceView.queueEvent {
                managerViewmode!!.nextViewMode()
            }
        }
    }


    fun writeCurrentImage() {
        if (nextNameIndex < 0 || nextNameIndex > pdbFileNames.size - 1) {
            return
        }
        glSurfaceView.queueEvent {
            val internalSDcard = getDiskCacheDir("foo")
            try {
                // TODO: figure out how to get write permission to physical SDcard
//                val externalStorageVolumes: Array<out File> =
//                        ContextCompat.getExternalFilesDirs(activity, null)
//                val sdcardRoot = externalStorageVolumes
//                        .last()
//                        .absolutePath
//                        .split("/")
//                        .dropLast(4)
//                        .joinToString("/")
//                val myFile = File(sdcardRoot, "/PDB/$pdbName.png")

                val pdbName = pdbFileNames[nextNameIndex]
                val myFile = File("/sdcard/Pictures/", "$pdbName.png")
                val fileOutputStream = FileOutputStream(myFile)
                val bm = renderer.readGlBufferToBitmap(350, 580, 400, 400)
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
            Timber.e("writeCurrentImage: DONE WRITING")
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

    private fun checkFiles() = runBlocking {
        launch(Dispatchers.IO) { // will get dispatched to ForkJoinPool.commonPool (or equivalent)
            Timber.v("checkFiles: I'm working in thread ${Thread.currentThread().name}")
            val currentTime = System.currentTimeMillis()
            var missingCount = 0
            val path = "/mnt/sdcard/PDB"


            // https://developer.android.com/training/data-storage/app-specific#external

            val externalStorageVolumes: Array<out File> =
                    ContextCompat.getExternalFilesDirs(activity, null)
            val sdcardRoot = externalStorageVolumes
                    .last()
                    .absolutePath
                    .split("/")
                    .dropLast(4)
                    .joinToString("/")

            androidFilePath = "$sdcardRoot/PDB/"

            for (name in pdbFileNames) {
                val file = File(androidFilePath, "$name.pdb")
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

    /**
     * callback from pdbCache when the pdb is in the cache or download is complete
     */
    override fun loadPdbFromStream(stream: InputStream) {
        managePdbFile.parsePdbInputStream(stream, mol, pdbFileNames[nextNameIndex])
        stream.close()
        Timber.e("loadPdbFromStream: Start up the RENDERER")
        glSurfaceView.queueEvent {
            managerViewmode!!.createView()
            renderer.setMolecule(mol)
            renderer.resetCamera()
        }
    }
}