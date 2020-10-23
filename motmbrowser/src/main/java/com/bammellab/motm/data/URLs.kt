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

    const val PDB_MOTM_PREFIX = "https://pdb101.rcsb.org/motm/"
    const val PDB_MOTM_SUFFIX = "#sub-navbar"

    const val RCSB_MOTM_IMAGE_PREFIX = "https://cdn.rcsb.org/pdb101/motm/images/tn/"

    const val RCSB_PDB_INFO_PREFIX = "https://www.rcsb.org/pdb/explore.do?structureId="
    const val RCSB_PDB_INFO_SUFFIX = "#structureID"

    var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.rcsb.org/pdb/rest/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()


}