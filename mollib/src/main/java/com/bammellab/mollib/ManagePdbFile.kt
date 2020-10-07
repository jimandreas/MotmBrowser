package com.bammellab.mollib

import android.app.Activity
import android.content.res.AssetManager
import com.bammellab.mollib.objects.*
import com.bammellab.mollib.protein.Molecule
import timber.log.Timber
import java.io.IOException

class ManagePdbFile(
    private val activity: Activity

) {

    private lateinit var pdbFile: ParserPdbFile
    private val bufferManager = BufferManager.getInstance(activity)

    fun setup(mol: Molecule, managerViewmode: ManagerViewmode ) {
        pdbFile = ParserPdbFile(
                activity, mol, bufferManager, managerViewmode)
    }

    fun parsePdbFile(pdbFileName: String) {
        pdbFile.parse(pdbFileName)
    }

    fun parsePdbFileFromAsset(pdbAssetName: String) {
        val name = "$pdbAssetName.pdb"
        try {
            val inputStream = activity.assets.open(name, AssetManager.ACCESS_BUFFER);
            pdbFile.loadPdbFromStream(pdbAssetName, inputStream)
        } catch (e: IOException) {
            Timber.e("Could not access asset: $name")
            return
        }
    }


}