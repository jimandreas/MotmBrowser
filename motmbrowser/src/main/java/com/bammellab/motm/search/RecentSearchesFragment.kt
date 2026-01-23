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

package com.bammellab.motm.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bammellab.motm.R
import com.bammellab.motm.util.PrefsUtil

class RecentSearchesFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var recentSearchesContainer: FrameLayout
    private lateinit var recentSearchesList: ListView
    private lateinit var recentSearchesDeleteButton: ImageView
    private lateinit var previousSearchesList: MutableList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search_recent, container, false)
        //localContext = root!!.context

        recentSearchesContainer = root.findViewById(R.id.recent_searches_frame_layout)
        recentSearchesList = root.findViewById(R.id.recent_searches_list)
        recentSearchesDeleteButton = root.findViewById(R.id.recent_searches_delete_button)

        previousSearchesList = PrefsUtil.prefsPreviousSearchesSet.toMutableList()

        val newAdapter = ArrayAdapter(
                requireContext(), R.layout.fragment_search_item_textview, previousSearchesList)

        recentSearchesList.adapter = newAdapter
//        searchViewModel.previousSearchStringList.observe(viewLifecycleOwner, {
//            val searchList = it
//            wiredList = searchList!!.toMutableList()
//        })

        // clear the list
        recentSearchesDeleteButton.setOnClickListener { _ ->
            previousSearchesList.clear()
            newAdapter.clear()
            PrefsUtil.setPreviousSearchesList( emptySet())
            newAdapter.notifyDataSetChanged()
        }

        recentSearchesList.setOnItemClickListener { parent, v, position, id ->
            val str = (v as TextView).text
            if (callback != null) {
                callback!!.updateSearchString(str.toString())
            }
        }
        return root
    }


    fun setViewModel(viewModel: SearchViewModel) {
        searchViewModel = viewModel
    }

    fun show() {
        recentSearchesContainer.visibility = VISIBLE
    }

    fun hide() {
        recentSearchesContainer.visibility = INVISIBLE
    }

    fun setCallback(callbackIn: Callback) {
        callback = callbackIn
    }

    interface Callback {
        fun updateSearchString(recentSearchString: String)
    }

    private var callback: Callback? = null
}
