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

@file:Suppress("unused", "unused_variable", "unused_parameter")
package com.bammellab.motm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bammellab.motm.data.Corpus
import com.bammellab.motm.data.Corpus.invertPosition
import com.bammellab.motm.data.Corpus.motmTagLinesGet
import com.bammellab.motm.data.Corpus.motmTitleGet
import timber.log.Timber
import java.util.*

class MotmListFragment : androidx.fragment.app.Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rv = inflater.inflate(
                R.layout.fragment_recyclerview, container, false) as RecyclerView
        setupRecyclerView(rv)
        setHasOptionsMenu(true)
        return rv
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.motm_all_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        val adapter = SimpleStringRecyclerViewAdapter(recyclerView.context,
                motmList())
        recyclerView.adapter = adapter
    }

    private fun motmList(): List<String> {
        val numItems = Corpus.corpus.size
        val list = ArrayList<String>(numItems)

        for (i in 0 until numItems) {
            list.add(Corpus.corpus[i])
        }
        return list
    }

    class SimpleStringRecyclerViewAdapter(context: Context, private val motmList: List<String>)
        : RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder>() {

        private val mTypedValue = TypedValue()
        private val mBackground: Int

        class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
            var mBoundString: String? = null
            val mImageView: ImageView = mView.findViewById(R.id.avatar)
            val mTextView: TextView = mView.findViewById(R.id.motmtext1)
            val mTextView2: TextView = mView.findViewById(R.id.motmtext2)

            override fun toString(): String {
                return super.toString() + " '" + mTextView.text
            }
        }

        fun getValueAt(position: Int): String {
            return motmList[position]
        }

        init {
            context.theme.resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true)
            mBackground = mTypedValue.resourceId
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_motm, parent, false)
            view.setBackgroundResource(mBackground)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.mBoundString = motmList[position]
            val invertPosition = invertPosition(position)
            holder.mBoundString = (invertPosition+1).toString() // motm tables are one based, position starts at zero

            holder.mView.setOnClickListener { v ->
                val context = v.context
                // val str = holder.mBoundString

                Timber.i("OnClick: bound string is %s", holder.mBoundString)
                val intent = Intent(context, MotmDetailActivity::class.java)
                intent.putExtra(MotmDetailActivity.EXTRA_NAME, holder.mBoundString)
                //                intent.putExtra(MotmDetailActivity.EXTRA_CATEGORY,
                //                        MotmSection.FRAG_ALL_MOTM);
                context.startActivity(intent)
            }

            @Suppress("DEPRECATION")
            val spannedString = Html.fromHtml("<strong><big>"
                    + motmTitleGet(invertPosition + 1)
                    + "</big></strong><br><i>"
//                    + Corpus.motmDateByKey[position + 1]
                    + Corpus.motmDateByKey(invertPosition + 1)
                    + "</i><br>"
//                    + Corpus.motmDescByKey[position + 1])
                    + motmTagLinesGet(invertPosition+1))

            holder.mTextView.text = spannedString
            holder.mTextView2.visibility = View.GONE

            val image = Corpus.motmImageListGet(invertPosition + 1)

            val url = MotmApplication.RCSB_MOTM_IMAGE_PREFIX + image
            Glide.with(holder.mImageView.context)
                    .load(url)
                    .fitCenter()
                    .into(holder.mImageView)
        }

        override fun getItemCount(): Int {
            return motmList.size
        }
    }
}
