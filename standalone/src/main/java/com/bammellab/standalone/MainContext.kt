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

@file:Suppress("unused")

package com.bammellab.standalone

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
