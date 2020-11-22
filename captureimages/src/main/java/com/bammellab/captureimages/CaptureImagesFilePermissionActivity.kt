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

@file:Suppress("UNUSED_VARIABLE", "SameParameterValue")

package com.bammellab.captureimages

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class CaptureImagesFilePermissionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_permission)

        if (isStoragePermissionGranted()) {
            attemptFileRead()
            val pdbIntent = Intent(this, ActivityImageCap::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(pdbIntent)
        }
    }

    // see this handy answer:
    //  http://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow
    private fun isStoragePermissionGranted(): Boolean {

        val readOnly = isExternalStorageReadOnly()

        val available = isExternalStorageAvailable()

        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Timber.i("Permission is granted")
                true
            } else {
                Timber.e("Permission is revoked!!!!!!!!!!!!!!!!!!!")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                message(" Please say ALLOW and try again")
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Timber.i("Permission is granted")
            true
        }
    }


    private fun isExternalStorageReadOnly(): Boolean {
        val extStorageState: String = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState
    }

    private fun isExternalStorageAvailable(): Boolean {
        val extStorageState: String = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == extStorageState
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Timber.v("Permission: %s was %s (GRANTED = 0, DENIED = -1)", permissions[0], grantResults[0])
            //resume tasks needing this permission
        }
    }

    private fun message(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun attemptFileRead() {

        val inputStream: InputStream
        //        val reader: BufferedReader? = null
//        val line: String? = null
        val fileInputStream: FileInputStream

        try {
//            val file = File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES), "test123")
//            val folder = mActivity.getExternalFilesDir("PDB")
//            val folder2 = mActivity.filesDir

            val folder3 = File("/storage/emulated/0/PDB/")
            //            File myFile = new File(folder3,"1a0h.pdb.gz");
            val myFile = File(folder3, "1a0h.pdb")
            fileInputStream = FileInputStream(myFile)

            Timber.i("success with %s", "1a0h.pdb")

            /*
             * do a test read then close the stream
             */
            val byteCount = fileInputStream.read()
            Timber.v("1a0h: read %d bytes", byteCount)
            fileInputStream.close()

            // inputStream = assetManager.open(pdbFileName, AssetManager.ACCESS_BUFFER);

        } catch (e: IOException) {
            Timber.e("IO error in file 1a0h.pdb")
        }
    }
}
