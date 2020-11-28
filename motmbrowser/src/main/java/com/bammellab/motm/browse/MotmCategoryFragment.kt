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
@file:Suppress("unused", "unused_variable", "unused_parameter", "deprecation")

package com.bammellab.motm.browse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bammellab.motm.R
import com.bammellab.motm.data.Corpus
import com.bammellab.motm.data.Corpus.motmTitleGet
import com.bammellab.motm.data.MotmByCategory
import com.bammellab.motm.data.URLs.PDB_MOTM_THUMB_WEB_PREFIX
import com.bammellab.motm.detail.MotmDetailActivity
import com.bumptech.glide.Glide
import timber.log.Timber

/**
 * This Fragment displays the list of Molecule Of The Month features.
 * Each list is organized into various subsections as indicated by
 * the lists in the MotmByCategory file.
 */
class MotmCategoryFragment : Fragment() {

    private var motmList: Array<String>? = null
    var currentCategory: Array<String>? = null

    fun fragmentCategory(category : Array<String>) {
        currentCategory = category
        motmList = category
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
       val recyclerView = inflater.inflate(
                R.layout.fragment_recyclerview, container, false) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)

        if (motmList != null) {
            val adapter = SimpleStringRecyclerViewAdapter(requireActivity(), motmList!!)
            recyclerView.adapter = adapter
        } else {
            Timber.e("CATEGORY FRAG: list is null!!")
            val adapter = SimpleStringRecyclerViewAdapter(requireActivity(), MotmByCategory.MotmCategoryHealth)
            recyclerView.adapter = adapter
        }
        return recyclerView
    }

    inner class SimpleStringRecyclerViewAdapter(context: Context, private val motmList: Array<String>)
        : RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolderMotm>() {

        // handle testing for headers or regular MOTM views in adapter
        override fun getItemViewType(position: Int): Int {
            val entry = motmList[position]

            return if (entry[0] < '0' || entry[0] > '9')
                VIEW_TYPE_HEADER
            else
                VIEW_TYPE_MOTM
        }

        inner class ViewHolderMotm(val v: View, viewType: Int)
            : RecyclerView.ViewHolder(v), View.OnClickListener {
            var motmName: String? = null
            lateinit var imageView: ImageView
            lateinit var textView: TextView
            lateinit var textView2: TextView
            lateinit var textViewHeader: TextView

            init {
                when (viewType) {
                    VIEW_TYPE_MOTM -> {
                        imageView = v.findViewById(R.id.avatar)
                        textView = v.findViewById(R.id.motmtext1)
                        textView2 = v.findViewById(R.id.motmtext2)
                    }
                    VIEW_TYPE_HEADER -> {
                        textViewHeader = v.findViewById(R.id.motmheader)
                    }
                }
            }

            override fun onClick(v: View) {
                //                Timber.i("onClick - in ViewHolderMotm");
            }
        }

        /*init {
            context.theme.resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true)
            mBackground = mTypedValue.resourceId
        }*/

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMotm {
            //            View view = LayoutInflater.from(parent.getContext())
            //                    .inflate(R.layout.list_item_motm, parent, false);
            //            view.setBackgroundResource(mBackground);
            //            return new ViewHolder(view);

            if (parent is RecyclerView) {
                var layoutId = -1
                when (viewType) {
                    VIEW_TYPE_HEADER -> {
                        layoutId = R.layout.list_item_header
                    }
                    VIEW_TYPE_MOTM -> {
                        layoutId = R.layout.list_item_motm
                    }
                }


                val view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false)
                view.isFocusable = false
                return ViewHolderMotm(view, viewType)

            } else {
                throw RuntimeException("Not bound to RecyclerView")
            }
        }

        override fun onBindViewHolder(holder: ViewHolderMotm, position: Int) {
            holder.motmName = motmList[position]

            /*
             * handle clicks on molecules.  clicks on headers are ignored
             */
            holder.v.setOnClickListener(View.OnClickListener { v ->
                val context = v.context
                val str = holder.motmName
                if (!isNumeric(str)) {
                    return@OnClickListener
                }
                val intent = Intent(context, MotmDetailActivity::class.java)
                intent.putExtra(MotmDetailActivity.MOTM_EXTRA_NAME, str)
                intent.putExtra(MotmDetailActivity.MOTM_EXTRA_CATEGORY, currentCategory)
                context.startActivity(intent)
            })

            val motm = motmList[position]

            /*
             * If the motm is numeric - then this refers to a Molecule of the Month
             *  feature.   Do a normal panel (override any changes by the else clause below)
             */
            if (isNumeric(motm)) {
                val imageIndex = Integer.valueOf(motm)
                val imageString = Corpus.motmImageListGet(imageIndex-1)
                val url = "$PDB_MOTM_THUMB_WEB_PREFIX$imageString?raw=true"
                Glide.with(holder.imageView.context)
                        .load(url)
                        .fitCenter()
                        .into(holder.imageView)

                holder.imageView.visibility = View.VISIBLE

                // note:  the "Corpus" is zero based,
                //    but the indexing scheme on motm is one based - so
                //    we need to pull back by one index for corpus indexes
                val spannedString = Html.fromHtml("<strong><big>"
                        + motmTitleGet(imageIndex)
                        + "</big></strong><br><i>"
//                        + Corpus.motmDateByKey[imageIndex]
                        + Corpus.motmDateByKey(imageIndex)
                        + "</i><br>"
//                        + Corpus.motmDescByKey[imageIndex])
                        + Corpus.motmTagLinesGet(imageIndex-1))

                holder.textView.text = spannedString
                holder.textView2.visibility = View.GONE

                //                TypedValue typedValue = new TypedValue();
                //                Resources.Theme theme = holder.mView.getContext().getTheme();
                //                theme.resolveAttribute(R.attr.background, typedValue, true);
                //                int color = typedValue.data;
                //                holder.mView.setBackgroundColor(color);
                //
                //
                //                theme.resolveAttribute(textColorPrimary, typedValue, true);
                //                color = typedValue.data;
                //                holder.textView.setTextColor(color);


            } else {
                Timber.i("not numeric: %s", motm)
                val spannedString = Html.fromHtml("<strong><big>"
                        + motmList[position]
                        + "</big></strong><br><i>"
                        //                            + Corpus.motmDateByKey.get(position + 1)
                        //                            + "</i><br>"
                        //                            + Corpus.motmDescByKey.get(position + 1)
                )

                holder.textViewHeader.text = spannedString
                //                float old_size = holder.textView.getTextSize();
                //                Timber.i("old text size in float: %f", old_size);
                //                holder.textView.setTextSize(22.0f);

            }
        }

        private fun isNumeric(str: String?): Boolean {
            if (str == null || str.isEmpty()) {
                return false
            }
            for (i in str.indices) {
                if (str[i] < '0' || str[i] > '9') {
                    return false
                }
            }
            return true
        }

        override fun getItemCount(): Int {
            return motmList.size
        }
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_MOTM = 1
        const val WHICH_CATEGORY = "category"
    }
}


