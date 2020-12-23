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

package com.bammellab.screensaver

import android.net.Uri
import com.bammellab.mollib.data.MotmImageDownload.buildMotmImageList
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.MuzeiArtProvider

class MotmImageProvider : MuzeiArtProvider() {
    /**
     * set up the list of screensaver images
     * 1) call into the mollib to assemble the data list
     * 2) hand this data list to Muzei with all the appropriate builder attributes
     */
    override fun onLoadRequested(initial: Boolean) {

        val artworkList = mutableListOf<Artwork>()

        val theList = buildMotmImageList()
        for (motm in theList) {
            val artwork = Artwork.Builder()
                    .token(motm.motmGraphicName)
                    .title(motm.motmTitle)
                    .byline(motm.motmTagLine)
                    .persistentUri(
                            Uri.parse(
                        "https://github.com/kotmol/KotmolMotmImages/raw/master/docs/motm_png/${motm.motmGraphicName}.png"))
                    .webUri(Uri.parse("https://pdb101.rcsb.org/motm/${motm.motmNumber}"))
                    .attribution("Molecular Landscapes by David S. Goodsell CC-BY-4.0 license")
                    // .metadata("this is metadata")  // not used by Muzei
                    .build()
            artworkList.add(artwork)
        }
        setArtwork(artworkList)
    }
}