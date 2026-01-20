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

package com.bammellab.motm.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bammellab.motm.R
import com.bammellab.mollib.data.Corpus
import com.bammellab.mollib.data.Corpus.motmTitleGet
import com.bammellab.mollib.data.Corpus.numMonths
import com.bammellab.mollib.data.MotmImageDownload
import com.bammellab.mollib.data.PDBs
import com.bammellab.mollib.data.URLs.PDB_IMAGE_WEB_PREFIX
import com.bammellab.mollib.data.URLs.PDB_MOTM_PNG_WEB_PREFIX
import com.bammellab.mollib.data.URLs.PDB_MOTM_PREFIX
import com.bammellab.mollib.data.URLs.PDB_MOTM_SUFFIX
import com.bammellab.mollib.data.URLs.RCSB_MOTM_IMAGE_PREFIX
import com.bammellab.mollib.data.URLs.RCSB_PDB_INFO_PREFIX
import com.bammellab.mollib.data.URLs.RCSB_PDB_INFO_SUFFIX
import com.bammellab.motm.graphics.MotmGraphicsActivity
import com.bammellab.motm.pdb.PdbFetcherCoroutine
import com.bammellab.motm.util.PrefsUtil
import com.bammellab.motm.util.Util.toastAnError
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import timber.log.Timber

class MotmDetailActivity : AppCompatActivity() {

    private lateinit var motmDescription: TextView
    private lateinit var pdbDate: TextView
    private lateinit var pdbLayout: LinearLayout


    private var motmNumber: Int = 0

    @SuppressLint("SetTextI18n")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdb_detail)
        val motmTitle = findViewById<TextView>(R.id.card_detail_motm_title)
        motmDescription = findViewById(R.id.card_detail_motm_description)
        pdbDate = findViewById(R.id.card_detail_motm_date)
        pdbLayout = findViewById(R.id.pdb_detail_layout)
        val motmDetailCard = findViewById<CardView>(R.id.motm_detail_card)

        //val intent = intent
        val motmNumberString = intent.getStringExtra(MOTM_EXTRA_NAME)
        // be careful about arriving back here from somewhere else without the extra
        if (motmNumberString != null) {
            try {
                motmNumber = Integer.parseInt(motmNumberString)
            } catch (e: NumberFormatException) {
                Timber.e("MotmDetailActivity: Error on name as a number $motmNumberString")
                motmNumber = 1
            } finally {
                if (motmNumber < 0 || motmNumber > numMonths) {
                    Timber.e("motmNumber out of range, %d, should be between 0 and 300", motmNumber)
                    motmNumber = 0
                }
                Timber.i("Display detail on number %d", motmNumber)
            }
        } else {
            motmNumber = 1
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

//        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
//
//        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
//        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

        val motmName = motmTitleGet(this.motmNumber)
//        collapsingToolbar.title = motmName
        motmTitle.text = motmName

        motmDescription.text = Corpus.motmTagLinesGet(this.motmNumber-1)


        /*
         * getting fancier on assembling the Motm date in the detail view
         */
        var descRaw: String
        val sb = StringBuilder()

        sb.append("<i>")
        sb.append(Corpus.motmDateByKey(this.motmNumber))
        sb.append("</i><br>")

        descRaw = sb.toString()

        descRaw = descRaw.replace("\n", "<br>")
        val desc: CharSequence

        // painful fromHtml version handling
        desc = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(descRaw,
                    Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(descRaw)
        }
        pdbDate.text = desc

        loadBackdrop()

        motmDetailCard.setOnClickListener {
            startMotmWebViewActivity()
        }
    }



    private fun loadBackdrop() {
        val imageView = findViewById<ImageView>(R.id.backdrop)
        var pngURL: String

//        val image = Corpus.motmImageListGet(motmNumber)
//        val url = RCSB_MOTM_IMAGE_PREFIX + image

        val motmPngUrl = PDB_MOTM_PNG_WEB_PREFIX
        pngURL = MotmImageDownload.motmTiffImageName(motmNumber)
        pngURL = "$motmPngUrl$pngURL.png?raw=true"
        if (pngURL == "") {
            val image = Corpus.motmImageListGet(motmNumber)
            pngURL = RCSB_MOTM_IMAGE_PREFIX + image
        }
//        Glide.with(this)
//                .load(pngURL)
//                .fitCenter()
//                .into(imageView)

                Picasso.get()
                        .load(pngURL)
                        .centerInside()
                        .resize(400, 400)
        //                .error(R.drawable.ic_no_wifi)
        //                .placeholder(R.drawable.ic_message_24px)
                        .into(imageView)

        /*
         * populate the Motm detail recyclerview with the PDB entries discussed
         * in the monthly feature.
         */
        val pdbs = PDBs.pdbListGivenMotmNumber(motmNumber) ?: return
        val pdbsStringArray = pdbs.toTypedArray()
        for (pdbId in pdbs) {
            val view = LayoutInflater.from(this)
                    .inflate(R.layout.pdb_card, pdbLayout, false) as CardView

            pdbLayout.addView(view)

            val pdbCardTitle = view.findViewById<TextView>(R.id.card_detail_pdb_identifier)
            val pdbCardText = view.findViewById<TextView>(R.id.card_detail_pdb_description)
            val im = view.findViewById<ImageView>(R.id.card_detail_pdb_image)
            val pdbLink = view.findViewById<ImageView>(R.id.card_detail_rcsb_link)

            if (pdbCardTitle != null) {
                pdbCardTitle.text = pdbId
            }

            var imageUrl = PDB_IMAGE_WEB_PREFIX
//            imageUrl = imageUrl + pdbId + "_asym_r_500.jpg" // these images have vanished
            // now the PDB images are partitioned into folders based on the first
            // digit of the PDB name.   Github only allows 1000 images per folder.
            // Hence the partitioning.

            imageUrl += pdbId[0]
            imageUrl += "/"
            imageUrl = "$imageUrl$pdbId.png?raw=true"

            Timber.v("image url is %s", imageUrl)

            Glide.with(this)
                    .load(imageUrl)
                    .fitCenter()
                    .into(im)

            pdbLink.tag = pdbId
            im.tag = pdbId
            view.tag = pdbId

            // load the text field via retrofit
            val fetcher = PdbFetcherCoroutine(pdbId, pdbCardText)
            fetcher.pdbFetcherCoroutine()

            /*
             * --------------
             * Click Handlers
             * --------------
             * respond to a click on the RCSB PDB imageview button
             *    Vector to the PDB info entry on the RCSB website
             * A pref setting determines if the action should be confirmed
             */
            pdbLink.setOnClickListener { v ->
                if (PrefsUtil.prefsTouchToOpenSetting) {
                    startPdbViewActivity(v)
                    return@setOnClickListener
                } else {
                    val snackbar = Snackbar.make(v, "RCSB PDB info", Snackbar.LENGTH_LONG)
                    snackbar.setAction("View website") {
                        startPdbViewActivity(v)
                    }
                    snackbar.show()
                }
            }

            /*
             * respond to a click on the molecule (PDB) - start 3D viewer
             */
            im.setOnClickListener { v -> start3dViewer(v, pdbsStringArray) }
            view.setOnClickListener { v -> start3dViewer(v, pdbsStringArray) }
        }
    }

    private fun startPdbViewActivity(v: View) {
        val pdbOfInterest = v.tag as String
        val intentRCSB = Intent(Intent.ACTION_VIEW)
        intentRCSB.data = Uri.parse(
                RCSB_PDB_INFO_PREFIX
                        + pdbOfInterest + RCSB_PDB_INFO_SUFFIX
        )

        try {
            Timber.i("start activity intent: $intentRCSB, $intentRCSB.data")
            startActivity(intentRCSB)
        } catch (e: Exception) {
            Timber.e(e, "exception on startActivity")
            toastAnError(this, "Error on viewing PDB info")
        }
    }

    private fun startMotmWebViewActivity() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)

            intent.data = Uri.parse(
                    PDB_MOTM_PREFIX
                            + motmNumber
                            + PDB_MOTM_SUFFIX
            )
            startActivity(intent)
        } catch (e: Exception) {
            Timber.e(e, "exception on Motm Web View Activity")
            toastAnError(this, "Error on viewing Molecule Of The Month")
        }
    }


    private fun start3dViewer(v: View, pdbsStringArray: Array<String>) {

        val pdbOfInterest = v.tag as String
        if (pdbOfInterest.isEmpty()) {
            return
        }

        val i = pdbsStringArray.indexOf(pdbOfInterest)
        if (i < 0) { // shouldn't happen
            Timber.e("ERROR: Couldn't find $pdbOfInterest in $pdbsStringArray")
            return
        }

        if (PrefsUtil.prefsTouchToOpenSetting) {
            start3DviewerActivity(pdbsStringArray, i)
        } else {
            val snackbar = Snackbar.make(v, R.string.snackbar_go_to_3d, Snackbar.LENGTH_LONG)
            snackbar.setAction(R.string.snackbar_go_to_3d_start) {
                start3DviewerActivity(pdbsStringArray, i)
            }
            snackbar.show()
        }

    }

    private fun start3DviewerActivity(pdbsStringArray: Array<String>, i: Int) {
        try {
            val intent = Intent(
                    this@MotmDetailActivity, MotmGraphicsActivity::class.java)
            intent.putExtra(
                    MotmGraphicsActivity.PDB_NAME_LIST, pdbsStringArray)
            intent.putExtra(
                    MotmGraphicsActivity.PDB_NAME_LIST_INDEX, i)
            startActivity(intent)
        } catch (e: Exception) {
            Timber.e("Error starting 3D Viewer")
            toastAnError(this, "Error on starting 3D viewer")
        }
    }

    // this brings the UI back to where it left off when the detail was selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Timber.i("Option item selected")
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val MOTM_EXTRA_NAME = "motm"
        const val MOTM_EXTRA_CATEGORY = "category"
    }
}
