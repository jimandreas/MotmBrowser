package com.bammellab.mollib

import android.app.Activity
import android.content.res.AssetManager
import com.kotmol.pdbParser.Molecule
import com.kotmol.pdbParser.ParserPdbFile


import timber.log.Timber
import java.io.FileInputStream
import java.io.IOException

class ManagePdbFile(
        private val activity: Activity

) {

    private lateinit var pdbFile: ParserPdbFile

    fun parsePdbFile(stream: FileInputStream, mol: Molecule, pdbName: String) {
        val retainedMessages = mutableListOf<String>()
        ParserPdbFile
                .Builder(mol)
                .setMoleculeName(pdbName)
                .setMessageStrings(retainedMessages)
                .loadPdbFromStream(stream)
                .doBondProcessing(true)
                .parse()
    }

    fun parsePdbFileFromAsset(pdbAssetName: String, mol: Molecule) {
        val name = "$pdbAssetName.pdb"
        try {
            val inputStream = activity.assets.open(name, AssetManager.ACCESS_BUFFER);
            val retainedMessages = mutableListOf<String>()
            ParserPdbFile
                    .Builder(mol)
                    .setMoleculeName(pdbAssetName)
                    .setMessageStrings(retainedMessages)
                    .loadPdbFromStream(inputStream)
                    .doBondProcessing(true)
                    .parse()
        } catch (e: IOException) {
            Timber.e("Could not access asset: $name")
            return
        }
    }


}