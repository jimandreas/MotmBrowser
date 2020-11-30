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

@file:Suppress("unused", "unused_variable", "unused_parameter")

package com.bammellab.motm.browse

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
import com.bammellab.motm.R
import com.bammellab.motm.data.Corpus
import com.bammellab.motm.data.Corpus.invertPosition
import com.bammellab.motm.data.Corpus.motmTagLinesGet
import com.bammellab.motm.data.Corpus.motmTitleGet
import com.bammellab.motm.data.URLs.PDB_MOTM_THUMB_WEB_PREFIX
import com.bammellab.motm.detail.MotmDetailActivity
import com.bumptech.glide.Glide
import timber.log.Timber
import java.util.*

class MotmListFragment : androidx.fragment.app.Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rv = inflater.inflate(
                R.layout.fragment_recyclerview, container, false) as RecyclerView
        setupRecyclerView(rv)
        return rv
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

        private val typedValue = TypedValue()
        private val background: Int

        class ViewHolder(val v: View, viewType: Int) : RecyclerView.ViewHolder(v) {
            var boundString: String? = null
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

            override fun toString(): String {
                return super.toString() + " '" + textView.text
            }
        }

        // Add a header for position 0
        override fun getItemViewType(position: Int): Int {
            val entry = motmList[position]

            return if (position == 0)
                VIEW_TYPE_HEADER
            else
                VIEW_TYPE_MOTM
        }

        fun getValueAt(position: Int): String {
            return motmList[position]
        }

        init {
            context.theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
            background = typedValue.resourceId
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            var layoutId = R.layout.list_item_motm
            if (viewType == VIEW_TYPE_HEADER) {
                layoutId = R.layout.list_item_header
            }

            val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            view.setBackgroundResource(background)
            return ViewHolder(view, viewType)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.boundString = motmList[position]
            val invertPosition = invertPosition(position-1)
            holder.boundString = (invertPosition + 1).toString() // motm tables are one based, position starts at zero

            holder.v.setOnClickListener { v ->
                val context = v.context
                // val str = holder.boundString

                Timber.i("OnClick: bound string is %s", holder.boundString)
                val intent = Intent(context, MotmDetailActivity::class.java)
                intent.putExtra(MotmDetailActivity.MOTM_EXTRA_NAME, holder.boundString)
                //                intent.putExtra(MotmDetailActivity.EXTRA_CATEGORY,
                //                        MotmSection.FRAG_ALL_MOTM);
                context.startActivity(intent)
            }

            if (position == 0) {
                val spannedString = Html.fromHtml("<strong><big>"
                        + "Molecule of the Month<br>Nov 2020 - Jan 2000"
                        + "</big></strong><br><i>"
                )

                holder.textViewHeader.text = spannedString
            }

            if (position > 0) {
                @Suppress("DEPRECATION")
                val spannedString = Html.fromHtml("<strong><big>"
                        + motmTitleGet(invertPosition + 1)
                        + "</big></strong><br><i>"
//                    + Corpus.motmDateByKey[position + 1]
                        + Corpus.motmDateByKey(invertPosition + 1)
                        + "</i><br>"
//                    + Corpus.motmDescByKey[position + 1])
                        + motmTagLinesGet(invertPosition))

                holder.textView.text = spannedString
                holder.textView2.visibility = View.GONE

                val imageString = Corpus.motmImageListGet(invertPosition)

                //val url = PDB_MOTM_THUMB_WEB_PREFIX + image
                val url = "$PDB_MOTM_THUMB_WEB_PREFIX$imageString?raw=true"
                Glide.with(holder.imageView.context)
                        .load(url)
                        .fitCenter()
                        .into(holder.imageView)
            }
        }

        override fun getItemCount(): Int {
            return motmList.size
        }
    }
    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_MOTM = 1
    }

}
