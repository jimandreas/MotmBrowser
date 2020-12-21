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

@file:Suppress("UNUSED_VARIABLE", "FunctionName", "MemberVisibilityCanBePrivate")

package com.bammellab.mollib

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import com.bammellab.mollib.LoadFromSource.*
import com.bammellab.mollib.Utility.parsePdbFileFromAsset
import com.bammellab.mollib.Utility.parsePdbInputStream
import com.bammellab.mollib.objects.ManagerViewmode
import com.bammellab.mollib.pdbDownload.PdbCallback
import com.bammellab.mollib.pdbDownload.PdbDownload
import com.kotmol.pdbParser.Molecule
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.*

enum class LoadFromSource {
    FROM_ASSETS,
    FROM_SDCARD_AND_CAPTURE,
    FROM_RCSB_OR_CACHE
}

/**
 * common code to parse and display PDB info
 *   Loads from wired-in assets (standalone app),
 *     or from side-loaded file (captureimages app),
 *     or downloaded from the RCSB website (motmbrowser app).
 *
 *
 */
class MollibProcessPdbs(
        private val activity: AppCompatActivity,
        private val glSurfaceView: GLSurfaceViewDisplayPdbFile,
        private val renderer: RendererDisplayPdbFile,
        private var nextNameIndex: Int = -1,
        private val pdbFileNames: List<String>,
        private val loadPdbFrom: LoadFromSource
) : SurfaceCreated, PdbCallback, UpdateRenderFinished {


    private var managerViewmode: ManagerViewmode? = null
    private var captureImagesFlag = false
    private lateinit var pdbDownload: PdbDownload
    private lateinit var mol: Molecule
    private val scope = MainScope()

    /**
     * getting started:
     *    For capture images app - run a check to see if the
     *    PDB files are side-loaded into the PDB folder.
     */
    init {
        Timber.e("file name list is ${pdbFileNames.size} long")
        renderer.setSurfaceCreatedListener(this)
        if (loadPdbFrom == FROM_SDCARD_AND_CAPTURE) {
            checkFilesLaunch()
        }
        if (nextNameIndex >= 0) {
            nextNameIndex--
        }
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
        Timber.v("startProcessing: thread ${Thread.currentThread().name}")

        when (loadPdbFrom) {
            FROM_SDCARD_AND_CAPTURE ->  {
                renderer.setUpdateListener(this)
                loadSequentialData()
            }
            FROM_RCSB_OR_CACHE -> {
                pdbDownload = PdbDownload(activity)
                pdbDownload.initPdbCallback(this)
                loadNextPdbFile()
            }
            else -> {
                loadNextPdbFile()
            }
        }

    }

    /**
     * iterate through the file list:
     * Parse
     * render
     * capture image
     * write it to a file
     */
    private val jobCaptureImages = Job()
    private fun loadSequentialData() = GlobalScope.launch(Dispatchers.IO + jobCaptureImages) {
        renderer.allocateReadBitmapArrays()

        loadNextPdbFile()
    }

    override fun updateActivity(name: String) {
        GlobalScope.launch(Dispatchers.IO + jobCaptureImages) {
            Timber.e("************")
            Timber.e("WRITE CURRENT IMAGE")
            Timber.e("************")
            writeCurrentImage(name)
            loadNextPdbFile()
        }

    }

    /*override fun updateActivity(name: String) {
        if (!pdbsCaptured.contains(name)) {
            pdbsCaptured.add(name)
            glSurfaceView.requestRender()
            GlobalScope.launch {
                glSurfaceView.requestRender()
                delay(2000L)
                Timber.e("************")
                Timber.e("WRITE IMAGE")
                Timber.e("************")
                processPdbs.writeCurrentImage()
            }
            GlobalScope.launch {
                delay(3000L)
                Timber.e("************")
                Timber.e("LOAD NEXT PDB")
                Timber.e("************")
                processPdbs.loadNextPdbFile()
            }
        }
    }*/

    fun loadNextPdbFile() = scope.launch {
        if (++nextNameIndex == pdbFileNames.size) {
            nextNameIndex = 0
        }
        Timber.v("Next file: %s", pdbFileNames[nextNameIndex])
        commonStuff()
    }

    fun loadPrevPdbFile() = scope.launch {
        if (nextNameIndex-- == 0) {
            nextNameIndex = pdbFileNames.size - 1
        }
        Timber.v("Prev file: %s", pdbFileNames[nextNameIndex])
        commonStuff()
    }

    private suspend fun commonStuff() {
        val data = withContext(Dispatchers.IO) {
            //mutex.withLock {
            val name = pdbFileNames[nextNameIndex]
            activity.runOnUiThread { activity.title = name }
            renderer.tossMoleculeToGC()
            mol = Molecule() // the one place where Molecule is allocated!!
            managerViewmode = ManagerViewmode(activity, mol)

            when (loadPdbFrom) {
                FROM_ASSETS -> {
                    parsePdbFileFromAsset(activity, name, mol)
                    startRendering()
                }
                FROM_SDCARD_AND_CAPTURE -> {
                    try {
                        val cacheDir = activity.externalCacheDir
                        val myFile = File(cacheDir, "PDB/$name.pdb")
                        if (!myFile.exists()) {
                            Timber.e("nope $myFile does not exist")
                        } else {
                            Timber.v("Yay $name exists")
                        }
                        val fileStream = FileInputStream(myFile)

                        parsePdbInputStream(fileStream, mol, name)
                        fileStream.close()

                    } catch (e: AccessDeniedException) {
                        Timber.e(e, "$name ACCESS DENIED!!!!!!!!!!!!")
                    } catch (e: FileNotFoundException) {
                        Timber.e(e, "$name not found")
                    } catch (e: IOException) {
                        Timber.e(e, "$name IO Exception")
                    }

                    startRendering()
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
                glSurfaceView.requestRender()
            }
        }
    }

    fun pickViewMode(doMode: Int) {
        if (managerViewmode != null) {
            glSurfaceView.queueEvent {
                managerViewmode!!.doViewMode(doMode)
                glSurfaceView.requestRender()
            }
        }
    }

    fun writeCurrentImage(pdbName: String) {
        glSurfaceView.queueEvent {
            val internalSDcard = activity.externalCacheDir
            try {
                val testPath = File(internalSDcard, "Thumbs")
                if (!testPath.exists()) {
                    testPath.mkdir()
                    if (!testPath.exists()) {
                        Timber.e("********************************************")
                        Timber.e("ERRROOOOR: cannot make ${testPath.name}")
                        Timber.e("********************************************")
                    }
                }
                val myFile = File(internalSDcard, "Thumbs/$pdbName.png")
                val fileOutputStream = FileOutputStream(myFile)
                val bm = renderer.readGlBufferToBitmap(350, 580, 400, 400)
                if (bm != null) {
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()
                    Timber.e("write OK: $myFile")
                }

            } catch (e: FileNotFoundException) {
                Timber.e("FileNotFound")
            } catch (e: IOException) {
                Timber.e("IOException")
            }
            Timber.v("DONE WRITING name = $pdbName")

        }
    }

    private fun checkFilesLaunch() = scope.launch {
        checkFiles()
    }

    private suspend fun checkFiles() {
        val data = withContext(Dispatchers.IO) { // will get dispatched to ForkJoinPool.commonPool (or equivalent)
            Timber.v("checkFiles: I'm working in thread ${Thread.currentThread().name}")
            val currentTime = System.currentTimeMillis()
            var missingCount = 0

            val cacheDir = activity.externalCacheDir
            for (name in pdbFileNames) {
                val file = File(cacheDir, "PDB/$name.pdb")
                if (!file.exists()) {
                    if (missingCount < 10) {
                        Timber.e("$file is missing from $cacheDir")
                    }
                    missingCount++
                }
            }
            if (missingCount > 0) {
                Timber.e("There were $missingCount missing files from path $cacheDir (%d ms)",
                        System.currentTimeMillis() - currentTime)
            } else {
                Timber.v("There were $missingCount missing files from path $cacheDir (%d ms)",
                        System.currentTimeMillis() - currentTime)
            }
        }
    }

    /**
     * callback from PdbDownload when the pdb is in the cache or download is complete
     */
    override fun loadPdbFromStream(stream: InputStream) {
        parsePdbInputStream(stream, mol, pdbFileNames[nextNameIndex])
        stream.close()
        startRendering()
    }

    private fun startRendering() {
        glSurfaceView.queueEvent {
            managerViewmode!!.createView()
            renderer.setMolecule(mol)
            renderer.resetCamera()
            glSurfaceView.requestRender()
        }
    }
}