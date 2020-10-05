package com.bammellab.captureimages

import android.app.Application
import timber.log.Timber

class CaptureImagesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
