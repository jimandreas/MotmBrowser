/*
 * Copyright (C) 2016-2018 James Andreas
 *
 * From code from various sources (iosched, Sunshine advanced, Cheesesquare):
 *
 *  Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.bammellab.motm

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.bammellab.motm.MotmApplication.Companion.RCSB_PDB_INFO_SUFFIX
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.bammellab.motm.data.Corpus
import com.bammellab.motm.data.Corpus.motmTitleGet
import com.bammellab.motm.data.MotmImageDownload
import com.bammellab.motm.pdb.PdbFetcherCoroutine
import timber.log.Timber

class MotmDetailActivity : AppCompatActivity()
/* implements View.OnTouchListener */ {

    private lateinit var motmTitle: TextView
    private lateinit var pdbText: TextView
    private lateinit var moreButton: FloatingActionButton
    private lateinit var pdbLayout: LinearLayout

    private var motmNumber: Int = 0

    @SuppressLint("SetTextI18n")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdb_detail)
        motmTitle = findViewById(R.id.pdb_title)
        pdbText = findViewById(R.id.pdb_text)
        moreButton = findViewById(R.id.more_button)
        pdbLayout = findViewById(R.id.pdb_detail_layout)

        //val intent = intent
        val motmNumberString = intent.getStringExtra(EXTRA_NAME)
        // be careful about arriving back here from somewhere else without the extra
        if (motmNumberString != null) {
            try {
                motmNumber = Integer.parseInt(motmNumberString)
            } finally {
                if (motmNumber < 0 || motmNumber > 300) {
                    Timber.e("motmNumber out of range, %d, should be between 0 and 300", motmNumber)
                    motmNumber = 0
                }
                Timber.i("Display detail on number %d", motmNumber)
            }
        } else {
            motmNumber = 1
        }

        /*
         * use special instructions to allow assignment to the IntDef type,
         *   see: http://stackoverflow.com/a/27659565/3853712
         */
        //val motmCategory = intent.getIntExtra(EXTRA_CATEGORY, 0)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)

        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

        val motmName = motmTitleGet(this.motmNumber)
        collapsingToolbar.title = motmName

        loadBackdrop()

//        motmTitle.text = Corpus.motmDescByKey[this.motmNumber]
        motmTitle.text = Corpus.motmTagLines[Corpus.motmTagLines.size - this.motmNumber]

        /*
         * getting fancier on assembling the Motm description in the detail view
         */
        var descRaw: String
        //        String desc_raw = "<i>"
        //                        + Corpus.motmDateByKey.get(this.motmNumber)
        //                        + "</i><br>"
        //                        + "Category: asdf<br>Subcategory:xyz";


        val sb = StringBuilder()

        sb.append("<i>")
//        sb.append(Corpus.motmDateByKey[this.motmNumber])
        sb.append(Corpus.motmDateByKey(this.motmNumber))
        sb.append("</i><br>")

        /*if (motmCategory != MotmCategoryFragment.Companion.getFRAG_ALL_MOTM()) {
            MotmCategories categories = MotmApplication.INSTANCE.getMotmCategories();
            String categoryString;
            if (categories.categoryList.containsKey(motmCategory)) {
                sb.append("Category: <b>");
                categoryString = categories.categoryList.get(motmCategory);
                sb.append(categoryString);
                sb.append("</b>");
            }
        }*/

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
        pdbText.text = desc


        /*
         * FAB button (floating action button = FAB) to get more information
         *    Vector to the Motm website for this particular Motm entry
         */
        moreButton.setOnClickListener { view ->
            val snackbar = Snackbar.make(view, "MotM website", Snackbar.LENGTH_LONG)

            snackbar.setAction("View website") {
                val intent = Intent(Intent.ACTION_VIEW)

                intent.data = Uri.parse(
                        MotmApplication.PDB_MOTM_PREFIX
                                + motmNumber
                                + MotmApplication.PDB_MOTM_SUFFIX
                )
                startActivity(intent)
            }
            snackbar.show()
        }
    }


    private fun loadBackdrop() {
        val imageView = findViewById<ImageView>(R.id.backdrop)
        var pngURL: String = ""

//        val image = Corpus.motmImageListGet(motmNumber)
//        val url = MotmApplication.RCSB_MOTM_IMAGE_PREFIX + image

        var motmPngUrl = MotmApplication.PDB_MOTM_PNG_WEB_PREFIX
        pngURL = MotmImageDownload.getFirstTiffImageURL(motmNumber)
        pngURL = "$motmPngUrl$pngURL.png?raw=true"
        if (pngURL == "") {
            val image = Corpus.motmImageListGet(motmNumber)
            pngURL = MotmApplication.RCSB_MOTM_IMAGE_PREFIX + image
        }
        Glide.with(this)
                .load(pngURL)
                .fitCenter()
                .into(imageView)

        //        Picasso.with(this)
        //                .load(url)
        //                .centerInside()
        //                .resize(100, 100)
        ////                .error(R.drawable.ic_no_wifi)
        ////                .placeholder(R.drawable.ic_loading)
        //                .into(imageView);


        /*         * experiment with adding cards to the layouts
         */

        val app = application as MotmApplication
        val pdbList = app.pdbs
        val pdbs = pdbList.pdbsByMonth.get(motmNumber)
        val pdbsStringArray = pdbs.toTypedArray()
        for (pdbId in pdbs) {
            Timber.i("pdbId is %s", pdbId)

            val descRaw = "<big><bold>$pdbId</bold></big>"
            val desc: CharSequence
            desc = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Html.fromHtml(descRaw,
                        Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(descRaw)
            }
            pdbText.text = desc

            val view = LayoutInflater.from(this)
                    .inflate(R.layout.pdb_card, pdbLayout, false) as CardView

            pdbLayout.addView(view)
            val pdbCardTitle = view.findViewById<TextView>(R.id.pdb_title)
            val pdbCardText = view.findViewById<TextView>(R.id.pdb_text)
            val im = view.findViewById<ImageView>(R.id.pdb_image)
            val pdbLink = view.findViewById<ImageView>(R.id.pdb_link)

            if (pdbCardTitle != null) {
                pdbCardTitle.text = pdbId
            }

            //            if (pdb_card_text != null) {
            //                pdb_card_text.setText(desc);
            //            }


            var imageUrl = MotmApplication.PDB_IMAGE_WEB_PREFIX
//            imageUrl = imageUrl + pdbId + "_asym_r_500.jpg" // these images have vanished
            imageUrl = "$imageUrl$pdbId.png?raw=true"

            Timber.v("image url is %s", imageUrl)

            Glide.with(this)
                    .load(imageUrl)
                    .fitCenter()
                    .into(im)

            pdbLink.tag = pdbId
            im.tag = pdbId
            //            pdb_link.setOnTouchListener(this);
            //            pdb_card_text.setOnTouchListener(this);
            //            pdb_card_title.setOnTouchListener(this);
            //            im.setOnTouchListener(this);


            /*
             * respond to a click on the RCSB PDB imageview button
             *    Vector to the PDB info entry on the RCSB website
             */
            pdbLink.setOnClickListener { v ->
                val snackbar = Snackbar.make(v, "RCSB PDB info", Snackbar.LENGTH_LONG)

                val pdbOfInterest = v.tag as String

                snackbar.setAction("View website") {
                    val intent = Intent(Intent.ACTION_VIEW)

                    intent.data = Uri.parse(
                            MotmApplication.RCSB_PDB_INFO_PREFIX
                                    + pdbOfInterest + RCSB_PDB_INFO_SUFFIX
                    )
                    startActivity(intent)
                }
                snackbar.show()
            }

            /*
             * respond to a click on the molecule (PDB)
             *    experiment with master / detail
             */
            im.setOnClickListener(View.OnClickListener { v ->
                val snackbar = Snackbar.make(v, R.string.snackbar_go_to_3d, Snackbar.LENGTH_LONG)

                val pdbOfInterest = v.tag as String
                if (pdbOfInterest.isEmpty()) {
                    return@OnClickListener
                }
                @Suppress("RedundantSamConstructor")
                snackbar.setAction(R.string.snackbar_go_to_3d_start, View.OnClickListener {
                    /*
                     * get the index of our chosen PDB to pass along to the viewer
                     */
                    var i = 0
                    while (i < pdbsStringArray.size) {
                        if (pdbsStringArray[i] == pdbOfInterest) {
                            break
                        }
                        i++
                    }
                    if (i < pdbsStringArray.size) {
                        val intent = Intent(
                                this@MotmDetailActivity, MotmGraphicsActivity::class.java)
                        intent.putExtra(
                                MotmGraphicsActivity.INTENT_TAG_LIST, pdbsStringArray)
                        intent.putExtra(
                                MotmGraphicsActivity.INTENT_TAG_INDEX, i)
                        startActivity(intent)
                    }
                })
                snackbar.show()
            })
            /*
             * use Retrofit to load the PDB info from RCSB.org
             */
            val retro = app.retrofit

            // load the text field via retrofit
            val fetcher = PdbFetcherCoroutine(pdbId, pdbCardText, retro)
            fetcher.pdbFetcherCoroutine()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sample_actions, menu)
        return true
    }

    // this brings the UI back to where it left off when the detail was selected
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Timber.i("Option item selected")
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_NAME = "motm"
        const val EXTRA_CATEGORY = "category"
    }
}
