/*
 *  Copyright 2021 Bammellab / James Andreas
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

package com.bammellab.captureimages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bammellab.mollib.GLSurfaceViewDisplayPdbFile
import com.bammellab.mollib.LoadFromSource.FROM_SDCARD_AND_CAPTURE
import com.bammellab.mollib.MollibProcessPdbs
import com.bammellab.mollib.RendererDisplayPdbFile
import com.bammellab.mollib.Utility.checkForOpengl
import com.bammellab.mollib.Utility.failDialog
import timber.log.Timber

class ActivityImageCap : AppCompatActivity() {

    private lateinit var glSurfaceView: GLSurfaceViewDisplayPdbFile
    private lateinit var renderer: RendererDisplayPdbFile
    private lateinit var processPdbs: MollibProcessPdbs

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_pdb_file)

        glSurfaceView = findViewById(R.id.gl_surface_view)

        if (!checkForOpengl(this)) {
            failDialog(this,
                    R.string.activity_support_requirement,
                    R.string.activity_support_opegl)
            return
        }

        // Request an OpenGL ES 2.0 compatible context.
        glSurfaceView.setEGLContextClientVersion(2)

        val config = resources.configuration
        renderer = RendererDisplayPdbFile(this, glSurfaceView)
        glSurfaceView.setRenderer(renderer, config.densityDpi)
        //renderer.setUpdateListener(this)
//        renderer.overrideInitialScale(.6f) // thumbnail size
        renderer.overrideInitialScale(.8f) // thumbnail size

        // This freezes the updates, now adjusted in GLSurfaceView
        // gLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        processPdbs = MollibProcessPdbs(
                this,
                glSurfaceView,
                renderer,
                nextNameIndex = 0,
//                pdbFileNames = MotmPdbNames.pdbNames,
                pdbFileNames = MotmPdbNames.pdbNames2026,
                loadPdbFrom = FROM_SDCARD_AND_CAPTURE)

        //processPdbs.startProcessing(captureImages = true)
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        renderer.doCleanUp()
    }


}
