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


@file:Suppress("UnnecessaryVariable", "unused")

package com.bammellab.mollib

import android.app.Activity
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.GET_ACTIVITIES
import android.content.pm.PackageManager.NameNotFoundException
import android.content.res.AssetManager
import android.content.res.Resources
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import com.kotmol.pdbParser.Molecule
import com.kotmol.pdbParser.ParserPdbFile
import timber.log.Timber
import java.io.IOException
import java.io.InputStream

object Utility {

    fun parsePdbInputStream(stream: InputStream, mol: Molecule, pdbName: String) {
        val retainedMessages = mutableListOf<String>()
        ParserPdbFile
                .Builder(mol)
                .setMoleculeName(pdbName)
                .setMessageStrings(retainedMessages)
                .loadPdbFromStream(stream)
                .doBondProcessing(true)
                .parse()
        stream.close()
    }


    fun parsePdbFileFromAsset(activity: Activity, pdbAssetName: String, mol: Molecule) {
        val name = "$pdbAssetName.pdb"
        try {
            val inputStream = activity.assets.open(name, AssetManager.ACCESS_BUFFER)
            val retainedMessages = mutableListOf<String>()
            ParserPdbFile
                    .Builder(mol)
                    .setMoleculeName(pdbAssetName)
                    .setMessageStrings(retainedMessages)
                    .loadPdbFromStream(inputStream)
                    .doBondProcessing(true)
                    .parse()
            inputStream.close()
        } catch (e: IOException) {
            Timber.e(e, "Could not access asset: $name")
            return
        }
    }

    fun failDialog(
            activityIn: Activity,
            titleString: Int,
            messageString: Int
    ) {
        AlertDialog.Builder(activityIn)
                .setTitle(activityIn.getString(titleString))
                .setMessage(activityIn.getString(messageString))
                .setPositiveButton(activityIn.getString(R.string.affirmative_respose))
                { _, _ ->
                    activityIn.finish()
                }.show()
    }

    fun checkForOpengl(activityIn: Activity): Boolean {
        // Check if the system supports OpenGL ES 2.0.
        val activityManager = activityIn.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000

        return supportsEs2
    }

/*
 *   Youtube credit for this particular code work - with backstory on Recycler View and ListView:
 *    Using RecyclerView Part 3
 *   https://www.youtube.com/watch?v=jkK-Uxx6dLQ
 *   part of the Advanced Android App Development training at Audacity
 *       See also this repo:
 *
 * https://github.com/udacity/Advanced_Android_Development/tree/6.18_Bonus_RecyclerView_Code/app/src/main/java/com/example/android/sunshine/app
 *
 * And the training (HIGHLY RECOMMENDED):
 * https://www.udacity.com/course/advanced-android-app-development--ud855
 *
 * in particular for this module, ref:
 *
 *   wisdom.credit_to(Dan_Galpin);
 *
 INSTRUCTOR
 Dan Galpin is a Developer Advocate for Android,
 where his focus has been on Android performance tuning,
 developer training, and games. He has spent over 10 years
 working in the mobile space, developing at almost every
 layer of the phone stack. There are videos that demonstrate
 that he has performed in musical theater productions, but he would deny it.
 */



    private fun isAppInstalled(uri: String, context: Context): Boolean {
        val pm = context.packageManager
        val installed: Boolean
        installed = try {
            pm.getPackageInfo(uri, GET_ACTIVITIES)
            true
        } catch (e: NameNotFoundException) {
            false
        }

        return installed
    }

    fun viewUrl(url: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }

    /*
     *  see stackoverflow for wisdom on this technique:
     *  http://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
     */
    fun watchYoutubeVideo(id: String, context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
            if (isAppInstalled("com.google.android.youtube", context)) {
                intent.setClassName("com.google.android.youtube", "com.google.android.youtube.WatchActivity")
            }
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=$id"))
            context.startActivity(intent)
        }
    }

}