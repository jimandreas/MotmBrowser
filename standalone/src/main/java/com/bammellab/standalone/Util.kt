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

package com.bammellab.standalone

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AlertDialog

fun failDialog(
        activityIn: Activity,
        titleString: Int
) {
    AlertDialog.Builder(activityIn)
            .setTitle(activityIn.getString(titleString))
            //.setMessage(activityIn.getString(messageString))
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
