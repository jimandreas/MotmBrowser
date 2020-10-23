package com.bammellab.standalone

import android.app.Application
import android.content.Context
import timber.log.Timber

class StandaloneApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.tag("XYZZY")
        context = this
    }

    companion object {
        lateinit var context: Context
    }
}
