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

@file:Suppress("LiftReturnOrAssignment", "UnnecessaryVariable", "DEPRECATION")

package com.bammellab.motm.search

import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bammellab.motm.R
import com.bammellab.motm.data.Corpus
import com.bammellab.motm.data.Corpus.motmTitleGet
import com.bammellab.motm.data.PdbInfo
import com.bammellab.motm.data.URLs.PDB_IMAGE_WEB_PREFIX
import com.bammellab.motm.data.URLs.PDB_MOTM_THUMB_WEB_PREFIX
import com.bammellab.motm.detail.MotmDetailActivity
import com.bammellab.motm.graphics.MotmGraphicsActivity
import com.bammellab.motm.util.PrefsUtil
import com.bumptech.glide.Glide

import timber.log.Timber

class SearchAdapter(
    private val context: Context
) : RecyclerView.Adapter<SearchAdapter.ViewHolderMotm>() {

    private var pdbInfoList: List<PdbInfo.PdbEntryInfo> = emptyList()
    private var motmInfoList: List<Corpus.MotmEntryInfo> = emptyList()
    private var viewModel: SearchViewModel? = null
    private var arrowDown =
        AppCompatResources.getDrawable(context, R.drawable.ic_arrow_drop_down_grey_24px)
    private var arrowUp =
        AppCompatResources.getDrawable(context, R.drawable.ic_arrow_drop_up_grey_24px)
    private var searchTerm = ""

    fun doSearchOn(s: String) {
        searchTerm = s

        pdbInfoList = PdbInfo.searchPdbInfo(searchTerm)

        motmInfoList = Corpus.searchMotmInfo(searchTerm)

        notifyDataSetChanged()
    }

    fun setViewModel(vm: SearchViewModel) {
        viewModel = vm
    }

    fun setPdbInfoList(theList: List<PdbInfo.PdbEntryInfo>) {
        pdbInfoList = theList
    }

    fun setMotmInfoList(theList: List<Corpus.MotmEntryInfo>) {
        motmInfoList = theList
    }

    /**
     * the view is dependent on the 4 states - both lists collapsed,
     * one or the other collapsed, or both lists expanded.
     * First work out the extents of the lists
     * and then figure out what view this is at this position.
     */
    override fun getItemViewType(position: Int): Int {

        val motmBase = 0
        val motmLast = if (ifMotmListCollapsed()) 1 else motmInfoList.size
        val pdbBase = if (ifMotmListCollapsed()) {
            1
        } else {
            motmInfoList.size + 1
        }
        val pdbLast = if (ifMotmListCollapsed()) {
            pdbInfoList.size + 1
        } else {
            motmInfoList.size + pdbInfoList.size + 1
        }

        val retVal: Int = when (position) {
            0 -> {
                VIEW_TYPE_HEADER_MOTM
            }
            pdbBase -> {
                VIEW_TYPE_HEADER_PDB
            }
            in (motmBase + 1)..motmLast -> {
                VIEW_TYPE_MOTM
            }
            in (pdbBase + 1)..pdbLast -> {
                VIEW_TYPE_PDB
            }
            else -> {
                VIEW_TYPE_ERROR
            }
        }
        return retVal
    }

    /**
     * list is:
     * - a MotM header (with expansion instructions) and expandable matching Motm info
     * - a PDB header (with expansion instructions) and expandable matching PDBS
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMotm {
        if (parent is RecyclerView) {
            var layoutId = -1
            when (viewType) {
                VIEW_TYPE_HEADER_MOTM, VIEW_TYPE_HEADER_PDB -> {
                    layoutId = R.layout.fragment_search_list_header
                }
                VIEW_TYPE_MOTM, VIEW_TYPE_PDB -> {
                    layoutId = R.layout.fragment_search_list_item
                }
            }
            val view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false)
            return ViewHolderMotm(view, viewType)
        } else {
            throw RuntimeException("Not bound to RecyclerView")
        }
    }

    override fun onBindViewHolder(holder: ViewHolderMotm, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_HEADER_MOTM -> {
                holder.searchHeaderTitleText.text =
                    context.resources.getString(
                        R.string.search_motm_search_results
                    )
                val numMatchesString =
                    context.resources.getQuantityString(
                        R.plurals.search_results_with_placeholders,
                        motmInfoList.size,
                        motmInfoList.size
                    )
                holder.searchHeaderMatchesText.text = numMatchesString
                if (motmInfoList.isEmpty()) {
                    holder.searchExpandCollapseHint.visibility = View.INVISIBLE
                    holder.searchHeaderExpandArrow.visibility = View.INVISIBLE
                } else {
                    holder.searchExpandCollapseHint.visibility = View.VISIBLE
                    holder.searchHeaderExpandArrow.visibility = View.VISIBLE
                }
            }
            VIEW_TYPE_HEADER_PDB -> {
                holder.searchHeaderTitleText.text =
                    context.resources.getString(
                        R.string.search_pdb_search_results
                    )
                val numMatchesString =
                    context.resources.getQuantityString(
                        R.plurals.search_results_with_placeholders,
                        pdbInfoList.size,
                        pdbInfoList.size
                    )
                holder.searchHeaderMatchesText.text = numMatchesString

                if (pdbInfoList.isEmpty()) {
                    holder.searchExpandCollapseHint.visibility = View.INVISIBLE
                    holder.searchHeaderExpandArrow.visibility = View.INVISIBLE
                } else {
                    holder.searchExpandCollapseHint.visibility = View.VISIBLE
                    holder.searchHeaderExpandArrow.visibility = View.VISIBLE
                }
            }
            VIEW_TYPE_MOTM -> {

                val motmIndex = motmInfoList[position - 1].theIndexNumber
                holder.motmName = motmTitleGet(motmIndex)
                val spannedString = Html.fromHtml(
                    "<strong><big>"
                            + motmTitleGet(motmIndex)
                            + "</big></strong><br><i>"
                            + Corpus.motmDateByKey(motmIndex)
                            + "</i><br>"
                            + Corpus.motmTagLines[motmIndex]
                )
                holder.recyclerListTopTextline.text = spannedString

                val imageString = Corpus.motmImageListGet(motmIndex)
                val url = "$PDB_MOTM_THUMB_WEB_PREFIX$imageString?raw=true"
                Glide.with(holder.recyclerListLeftGraphic.context)
                    .load(url)
                    .fitCenter()
                    .into(holder.recyclerListLeftGraphic)

                holder.imageView.visibility = View.VISIBLE
            }
            /*
             * The PDB list is after the MOTM list.
             * Adjust the index into the pdb matches according to
             * whether the MOTM list is collapsed or expanded,
             * less the two header cards.
             */
            VIEW_TYPE_PDB -> {

                val adjustedIndex =
                    if (ifMotmListCollapsed()) {
                        position - 2
                    } else {
                        position - motmInfoList.size - 2
                    }
                val pdbName = pdbInfoList[adjustedIndex].pdbName

                val spannedString = Html.fromHtml(
                    "<strong><big>"
                            + pdbName
                            + "</big></strong><br>"
                            + pdbInfoList[adjustedIndex].pdbInfo
                )
                holder.recyclerListTopTextline.text = spannedString

                var imageUrl = PDB_IMAGE_WEB_PREFIX
//            imageUrl = imageUrl + pdbId + "_asym_r_500.jpg" // these images have vanished
                // now the PDB images are partitioned into folders based on the first
                // digit of the PDB name.   Github only allows 1000 images per folder.
                // Hence the partitioning.
                imageUrl += pdbName[0]
                imageUrl += "/"
                imageUrl = "$imageUrl$pdbName.png?raw=true"

                Glide.with(holder.recyclerListLeftGraphic.context)
                    .load(imageUrl)
                    .fitCenter()
                    .into(holder.recyclerListLeftGraphic)

            }
        }

        /*
         * handle clicks on molecules.  clicks on headers are ignored
         */
        holder.v.setOnClickListener(View.OnClickListener { v ->
            when (holder.itemViewType) {
                VIEW_TYPE_HEADER_MOTM -> {
                    if (toggleMotmMatchesListState()) {
                        holder.searchHeaderExpandArrow.background = arrowDown
                    } else {
                        holder.searchHeaderExpandArrow.background = arrowUp
                    }
                    if (callback != null) {
                        callback!!.clearKeyboard()
                    }
                    notifyDataSetChanged()
                }
                VIEW_TYPE_HEADER_PDB -> {
                    if (togglePdbMatchesListState()) {
                        holder.searchHeaderExpandArrow.background = arrowDown
                    } else {
                        holder.searchHeaderExpandArrow.background = arrowUp
                    }
                    if (callback != null) {
                        callback!!.clearKeyboard()
                    }
                    notifyDataSetChanged()
                }
                VIEW_TYPE_MOTM -> {
                    addSearchString(searchTerm)
                    val motmIndex = motmInfoList[position - 1].theIndexNumber
                    Timber.v("Molecule clicked, Motm number $motmIndex")
                    val intent = Intent(context, MotmDetailActivity::class.java)
                    intent.putExtra(MotmDetailActivity.MOTM_EXTRA_NAME, motmIndex.toString())
                    context.startActivity(intent)
                }
                VIEW_TYPE_PDB -> {
                    Timber.v("PDB clicked")
                    addSearchString(searchTerm)

                    val adjustedIndex =
                            if (ifMotmListCollapsed()) {
                                position - 2
                            } else {
                                position - motmInfoList.size - 2
                            }
                    val pdbName = pdbInfoList[adjustedIndex].pdbName

                    val pdbList = listOf(pdbName).toTypedArray()
                    val intent = Intent(
                            context, MotmGraphicsActivity::class.java)
                    intent.putExtra(
                            MotmGraphicsActivity.PDB_NAME_LIST, pdbList)
                    intent.putExtra(
                            MotmGraphicsActivity.PDB_NAME_LIST_INDEX, 0)
                    context.startActivity(intent)
                }
            }
        })

    }

    private fun addSearchString(str: String) {
        val oldSet = PrefsUtil.getStringSet(
            PrefsUtil.PREVIOUS_SEARCHES_KEY,
            emptySet()
        )
        val newSet: MutableSet<String>
        if (oldSet == null) {
            newSet = mutableSetOf(str) // seed string
        } else {
            newSet = oldSet.toMutableSet()
            newSet.add(str)
        }
        PrefsUtil.setStringSet(
            PrefsUtil.PREVIOUS_SEARCHES_KEY,
            newSet
        )
    }

    override fun getItemCount(): Int {
        var totalItems = 2
        if (!ifMotmListCollapsed()) {
            totalItems += motmInfoList.size
        }
        if (!ifPdbListCollapsed()) {
            totalItems += pdbInfoList.size
        }
        return totalItems
    }

    /**
     * Basically a mapping from the type of thing in the recyler list
     * to set of view variables that can be manipulated.
     */
    inner class ViewHolderMotm(val v: View, viewType: Int) : RecyclerView.ViewHolder(v),
        View.OnClickListener {

        lateinit var searchHeaderTitleText: TextView
        lateinit var searchHeaderMatchesText: TextView
        lateinit var searchHeaderExpandArrow: ImageView
        lateinit var searchExpandCollapseHint: TextView
        lateinit var imageView: ImageView
        lateinit var recyclerListTopTextline: TextView
        lateinit var recyclerListLeftGraphic: ImageView
        var motmName: String = ""


        init {
            when (viewType) {
                VIEW_TYPE_HEADER_MOTM, VIEW_TYPE_HEADER_PDB -> {
                    searchHeaderTitleText = v.findViewById(R.id.search_header_title_text)
                    searchHeaderMatchesText = v.findViewById(R.id.search_matches_text)
                    searchHeaderExpandArrow =
                        v.findViewById(R.id.search_header_expand_collapse_icon)
                    searchExpandCollapseHint = v.findViewById(R.id.search_expand_collapse_hint_text)
                }
                VIEW_TYPE_MOTM, VIEW_TYPE_PDB -> {
                    imageView = v.findViewById(R.id.recycler_list_left_graphic)
                    recyclerListTopTextline = v.findViewById(R.id.recycler_list_top_textline)
                    //recyclerListBodyTextline = v.findViewById(R.id.recycler_list_body_textline)
                    recyclerListLeftGraphic = v.findViewById(R.id.recycler_list_left_graphic)
                }
            }
        }

        override fun onClick(v: View) {
            Timber.i("onClick - in ViewHolderMotm");
        }
    }

    /**
     * return the expand / collapse state string for the search list header.
     */
    private var motmMatchesListCollapsed = true
    fun ifMotmListCollapsed(): Boolean {
        return motmMatchesListCollapsed
    }

    fun toggleMotmMatchesListState(): Boolean {
        motmMatchesListCollapsed = !motmMatchesListCollapsed
        return motmMatchesListCollapsed
    }

    private var pdbMatchesListCollapsed = true
    fun ifPdbListCollapsed(): Boolean {
        return pdbMatchesListCollapsed
    }

    fun togglePdbMatchesListState(): Boolean {
        pdbMatchesListCollapsed = !pdbMatchesListCollapsed
        return pdbMatchesListCollapsed
    }

    private fun expandStateString(): String {
        return if (pdbMatchesListCollapsed) {
            context.resources.getString(R.string.search_expand_search_results)
        } else {
            context.resources.getString(R.string.search_collapse_search_results)
        }
    }

    fun setCallback(callbackIn: Callback) {
        callback = callbackIn
    }

    interface Callback {
        fun clearKeyboard()
    }

    private var callback: Callback? = null


    companion object {
        private const val VIEW_TYPE_HEADER_MOTM = 0
        private const val VIEW_TYPE_MOTM = 1
        private const val VIEW_TYPE_HEADER_PDB = 2
        private const val VIEW_TYPE_PDB = 3
        private const val VIEW_TYPE_ERROR = 4
    }
}