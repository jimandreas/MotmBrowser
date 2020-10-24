/*
 *  Copyright 2020 James Andreas
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

package com.bammellab.mollib

import android.app.Activity
import android.content.res.AssetManager
import com.kotmol.pdbParser.Molecule
import com.kotmol.pdbParser.ParserPdbFile


import timber.log.Timber
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class ManagePdbFile(
        private val activity: Activity
) {
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

    fun parsePdbInputStream(stream: InputStream, mol: Molecule, pdbName: String) {
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