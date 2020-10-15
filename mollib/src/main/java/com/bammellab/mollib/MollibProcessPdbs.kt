package com.bammellab.mollib

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
import kotlinx.coroutines.sync.Mutex
import timber.log.Timber
import java.io.*

enum class LoadFromSource {
    FROM_ASSETS,
    FROM_SDCARD,
    FROM_CACHE
}

/**
 * common code to parse and display PDB info
 *   from a PDB folder or from a local asset file.
 */
class MollibProcessPdbs(
        activityIn: AppCompatActivity,
        glSurfaceViewIn: GLSurfaceViewDisplayPdbFile,
        rendererIn: RendererDisplayPdbFile,
        pdbFileNamesIn: List<String>,
        source: LoadFromSource
) : SurfaceCreated {
    private val activity = activityIn
    private val glSurfaceView = glSurfaceViewIn
    private val renderer = rendererIn
    private val managePdbFile = ManagePdbFile(activityIn)
    private val loadPdbFrom = source
    private lateinit var managerViewmode: ManagerViewmode

    private var nextNameIndex = -1

    private val pdbFileNames = pdbFileNamesIn
    private var captureImagesFlag = false

    private var androidFilePath = ""

    init {
        if (loadPdbFrom == FROM_SDCARD) {
            checkFiles()
        }
        rendererIn.setSurfaceCreatedListener(this)
    }

    fun startProcessing(captureImages: Boolean = false) {
        Timber.v("startProcessing: thread ${Thread.currentThread().name}")
        captureImagesFlag = captureImages
    }

    override fun surfaceCreatedCallback() {
        Timber.e("SURFACE CREATED CALLBACK")
        loadNextPdbFile()
    }

    fun loadNextPdbFile() {
        if (++nextNameIndex == pdbFileNames.size) {
            nextNameIndex = 0
        }
        Timber.v("Next file: %s", pdbFileNames[nextNameIndex])
        commonStuff()

//        if (captureImagesFlag) {
//            writeCurrentImage()
//        }


    }

    fun loadPrevPdbFile() {
        if (nextNameIndex-- == 0) {
            nextNameIndex = pdbFileNames.size - 1
        }
        Timber.v("Prev file: %s", pdbFileNames[nextNameIndex])
        commonStuff()
    }


    val mutex = Mutex()

    private fun commonStuff() = runBlocking {
        launch(Dispatchers.IO) {
            //mutex.withLock {
            val name = pdbFileNames[nextNameIndex]
            activity.runOnUiThread { activity.title = name }
            renderer.tossMoleculeToGC()
            val mol = Molecule() // the one place where Molecule is allocated!!
            managerViewmode = ManagerViewmode(
                    activity, mol)

            if (loadPdbFrom == FROM_ASSETS) {
                managePdbFile.parsePdbFileFromAsset(name, mol)
            } else {
                try {
                    val myFile = File(androidFilePath, "$name.pdb")
                    if (!myFile.exists()) {
                        Timber.i("nope %s does not exist", myFile)
                    } else {
                        Timber.i("Yay %s exists", myFile)
                    }
                    val fileStream = FileInputStream(myFile)

                    managePdbFile.parsePdbFile(fileStream, mol, name)
                    fileStream.close()

                } catch (e: FileNotFoundException) {
                    Timber.e("$name not found")
                } catch (e: IOException) {
                    Timber.e("$name IO Exception")
                }
            }

            glSurfaceView.queueEvent {
                managerViewmode.createView()
                renderer.setMolecule(mol)
                renderer.resetCamera()
            }
            //}
        }
    }


    fun writeCurrentImage() {
        if (nextNameIndex < 0 || nextNameIndex > pdbFileNames.size-1) {
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


}