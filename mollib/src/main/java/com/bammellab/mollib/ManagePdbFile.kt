package com.bammellab.mollib

import android.app.Activity
import com.bammellab.mollib.objects.*
import com.bammellab.mollib.protein.Molecule

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

}