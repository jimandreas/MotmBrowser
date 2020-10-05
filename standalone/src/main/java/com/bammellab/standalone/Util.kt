@file:Suppress("UnnecessaryVariable")

package com.bammellab.standalone

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService

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
            } .show()

}

fun checkForOpengl(activityIn: Activity): Boolean {
    // Check if the system supports OpenGL ES 2.0.
    val activityManager = activityIn.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val configurationInfo = activityManager.deviceConfigurationInfo
    val supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000

    return supportsEs2
}
