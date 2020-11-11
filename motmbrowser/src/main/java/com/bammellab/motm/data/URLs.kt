/*
 *  Copyright 2020 James Andreas
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

@file:Suppress("deprecation")

package com.bammellab.motm.data

import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

/**
 * The paths to:
 *
 * Motm Images (created by the captureimages app in this repo, and uploaded to KotmolMotmImages
 *
 * RCSB Molecule Of The Month resources
 */
object URLs {

    const val PDB_IMAGE_WEB_PREFIX = "https://github.com/kotmol/KotmolMotmImages/blob/master/docs/img/"
    const val PDB_MOTM_PNG_WEB_PREFIX = "https://github.com/kotmol/KotmolMotmImages/blob/master/docs/motm_png/"
    const val PDB_MOTM_THUMB_WEB_PREFIX = "https://github.com/kotmol/KotmolMotmImages/blob/master/docs/motm_thumbnail/"

    const val PDB_MOTM_PREFIX = "https://pdb101.rcsb.org/motm/"
    const val PDB_MOTM_SUFFIX = "#sub-navbar"

    const val RCSB_MOTM_IMAGE_PREFIX = "https://cdn.rcsb.org/pdb101/motm/images/tn/"

    const val RCSB_PDB_INFO_PREFIX = "https://www.rcsb.org/pdb/explore.do?structureId="
    const val RCSB_PDB_INFO_SUFFIX = "#structureID"

    const val RCSB_PDB_DOWNLOAD = "https://files.rcsb.org/download/"

    var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.rcsb.org/pdb/rest/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()


}