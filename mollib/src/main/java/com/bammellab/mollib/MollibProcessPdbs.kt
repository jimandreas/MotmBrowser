/*
 *  Copyright 2021 Bammellab / James Andreas
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

@file:Suppress("UNUSED_VARIABLE", "FunctionName", "MemberVisibilityCanBePrivate", "unused",
    "unused"
)

package com.bammellab.mollib

import android.graphics.Bitmap
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max
import kotlin.math.min
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
    private var pdbDownload: PdbDownload
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
        pdbDownload = PdbDownload(activity)
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
        renderer.allocateReadBitmapArrays(500, 500)

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
                    var loadSuccess = false
                    try {
                        val cacheDir = activity.externalCacheDir
                        val myFile = File(cacheDir, "PDB/$name.pdb")
                        if (!myFile.exists()) {
                            Timber.e("nope $myFile does not exist, skipping to next")
                        } else {
                            Timber.v("Yay $name exists")
                            val fileStream = FileInputStream(myFile)
                            parsePdbInputStream(fileStream, mol, name)
                            fileStream.close()
                            loadSuccess = true
                        }

                    } catch (e: AccessDeniedException) {
                        Timber.e(e, "$name ACCESS DENIED!!!!!!!!!!!!")
                    } catch (e: FileNotFoundException) {
                        Timber.e(e, "$name not found")
                    } catch (e: IOException) {
                        Timber.e(e, "$name IO Exception")
                    }

                    if (loadSuccess) {
                        startRendering()
                    } else {
                        // Skip to next file if this one couldn't be loaded
                        Timber.w("Skipping $name, moving to next PDB")
                        loadNextPdbFile()
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

    /**
     * Find the bounding box of non-transparent pixels in a bitmap.
     * Returns a Rect with the molecule extents, or null if no molecule pixels found.
     */
    private fun findMoleculeBounds(bitmap: Bitmap): Rect? {
        var minX = bitmap.width
        var minY = bitmap.height
        var maxX = 0
        var maxY = 0
        var foundPixel = false

        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {
                val pixel = bitmap.getPixel(x, y)
                // Non-transparent pixel (alpha > 0) indicates molecule
                if ((pixel ushr 24) > 0) {
                    foundPixel = true
                    if (x < minX) minX = x
                    if (x > maxX) maxX = x
                    if (y < minY) minY = y
                    if (y > maxY) maxY = y
                }
            }
        }

        return if (foundPixel) {
            Rect(minX, minY, maxX, maxY)
        } else {
            null
        }
    }

    /**
     * Calculate the optimal x, y origin for a capture that centers the molecule.
     * The scan bitmap was captured at (scanX, scanY) in GL coordinates.
     * Returns Pair(glX, glY) for the final 500x500 capture.
     */
    private fun calculateCenteredOrigin(
        bounds: Rect,
        scanX: Int,
        scanY: Int,
        scanWidth: Int,
        scanHeight: Int,
        captureSize: Int,
        screenWidth: Int,
        screenHeight: Int
    ): Pair<Int, Int> {
        // Molecule center in the scan bitmap coordinate system
        val moleculeCenterXInBitmap = (bounds.left + bounds.right) / 2
        val moleculeCenterYInBitmap = (bounds.top + bounds.bottom) / 2

        // Convert bitmap Y to GL Y (bitmap Y is flipped from GL Y)
        // In the bitmap, Y=0 is top, but in GL, Y=0 is bottom
        val moleculeCenterYInBitmapFlipped = scanHeight - 1 - moleculeCenterYInBitmap

        // Convert to GL screen coordinates
        val moleculeCenterXInGL = scanX + moleculeCenterXInBitmap
        val moleculeCenterYInGL = scanY + moleculeCenterYInBitmapFlipped

        // Calculate the origin to center the molecule in a captureSize x captureSize capture
        var newX = moleculeCenterXInGL - captureSize / 2
        var newY = moleculeCenterYInGL - captureSize / 2

        // Clamp to screen bounds
        newX = max(0, min(newX, screenWidth - captureSize))
        newY = max(0, min(newY, screenHeight - captureSize))

        return Pair(newX, newY)
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

                val captureSize = 500
                val screenWidth = renderer.getScreenWidth()
                val screenHeight = renderer.getScreenHeight()

                // Default capture position (fallback if molecule bounds can't be found)
                var captureX = 250
                var captureY = 900

                // First, do a scan capture to find the molecule bounds
                // Capture a larger region centered on the screen
                val scanWidth = min(screenWidth, 800)
                val scanHeight = min(screenHeight, 800)
                val scanX = max(0, (screenWidth - scanWidth) / 2)
                val scanY = max(0, (screenHeight - scanHeight) / 2)

                // Allocate buffers for the scan capture
                renderer.allocateReadBitmapArrays(scanWidth, scanHeight)
                val scanBitmap = renderer.readGlBufferToBitmap(scanX, scanY, scanWidth, scanHeight)

                if (scanBitmap != null) {
                    val bounds = findMoleculeBounds(scanBitmap)
                    if (bounds != null) {
                        Timber.d("Molecule bounds in scan: left=${bounds.left}, top=${bounds.top}, " +
                                "right=${bounds.right}, bottom=${bounds.bottom}")

                        val (newX, newY) = calculateCenteredOrigin(
                            bounds, scanX, scanY, scanWidth, scanHeight,
                            captureSize, screenWidth, screenHeight
                        )
                        captureX = newX
                        captureY = newY
                        Timber.d("Adjusted capture origin: x=$captureX, y=$captureY")
                    } else {
                        Timber.w("No molecule pixels found in scan, using default position")
                    }
                    scanBitmap.recycle()
                }

                // Reallocate buffers for the final capture size
                renderer.allocateReadBitmapArrays(captureSize, captureSize)

                // Capture the final centered image
                val myFile = File(internalSDcard, "Thumbs/$pdbName.png")
                val fileOutputStream = FileOutputStream(myFile)
                val bm = renderer.readGlBufferToBitmap(captureX, captureY, captureSize, captureSize)
                if (bm != null) {
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()
                    Timber.e("write OK: $myFile (origin: $captureX, $captureY)")
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