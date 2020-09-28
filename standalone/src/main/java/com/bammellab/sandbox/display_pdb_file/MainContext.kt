package com.bammellab.sandbox.display_pdb_file
import android.os.Handler
import com.bammellab.mollib.BuildConfig
import timber.log.Timber

/**
 * MainContext - a singleton enum(!) to hold common elements and control structures
 * See also:
 * http://stackoverflow.com/a/15080106/3853712
 *
 * Handler is set up to carry messages from other threads to the UI thread
 */

//@SuppressLint("StaticFieldLeak")
enum class MainContext {

    INSTANCE;

//    var context: Context? = null
    var handler: Handler? = null

    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

//    @IntDef(UI_MESSAGE_GL_READY, UI_MESSAGE_UPDATE_RPM)
//    @Retention(RetentionPolicy.SOURCE)
//    annotation class UImessages
//
//    companion object {
//
//
//        /**
//         * Handler messages
//         * for more information on the IntDef in the Studio annotation library:
//         *
//         * https://noobcoderblog.wordpress.com/2015/04/12/java-enum-and-android-intdefstringdef-annotation/
//         */
//        val UI_MESSAGE_GL_READY = 1
//        val UI_MESSAGE_UPDATE_RPM = 2
//    }
}
