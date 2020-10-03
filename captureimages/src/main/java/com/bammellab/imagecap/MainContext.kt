package com.bammellab.imagecap
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

enum class MainContext {

    INSTANCE;
    var handler: Handler? = null

    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
