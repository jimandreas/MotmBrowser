package com.bammellab.standalone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bammellab.standalone.R
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class StandaloneFilePermissionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_permission)

        if (isStoragePermissionGranted()) {
            attemptFileRead()
            val pdbIntent = Intent(this, ActivityDisplayPdbFile::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(pdbIntent)
        }
    }

    // see this handy answer:
    //  http://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow
    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Timber.i("Permission is granted")
                true
            } else {
                Timber.i("Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                message(" Please say ALLOW and try again")
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Timber.i("Permission is granted")
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Timber.v("Permission: %s was %s", permissions[0], grantResults[0])
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
