package com.bammellab.mollib

import android.app.Activity
import android.content.res.AssetManager
import com.bammellab.mollib.objects.*
import com.bammellab.mollib.protein.Molecule
import timber.log.Timber
import java.io.IOException

class ManagePdbFile(
    private val activity: Activity,
    glSurfaceViewIn: GLSurfaceViewDisplayPdbFile
) {
    private lateinit var mol: Molecule
    private lateinit var pdbFile: ParserPdbFile
    private lateinit var managerViewmode: ManagerViewmode
    private val glSurfaceView = glSurfaceViewIn

    private val bufferManager = BufferManager.getInstance(activity)
    fun setup() {
        bufferManager.resetBuffersForNextUsage()

        mol = Molecule()
        managerViewmode = ManagerViewmode(
                activity, mol, bufferManager)
        pdbFile = ParserPdbFile(
                activity, mol, bufferManager, managerViewmode)
    }

    fun parsePdbFile(pdbFileName: String) {
        pdbFile.parse(pdbFileName)

        glSurfaceView.queueEvent {
            managerViewmode.createView()
        }
    }

    fun parsePdbFileFromAsset(pdbAssetName: String) {
        val name = "$pdbAssetName.pdb"
        try {
            val inputStream = activity.assets.open(name, AssetManager.ACCESS_BUFFER);
            pdbFile.loadPdbFromStream(inputStream)
        } catch (e: IOException) {
            Timber.e("Could not access asset: $name")
            return
        }
        glSurfaceView.queueEvent {
            managerViewmode.createView()
        }
    }


}