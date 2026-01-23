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
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bammellab.motm.R
import androidx.core.view.isVisible


class SearchMatchesFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchResultsDisplay: FrameLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search_matches, container, false)

        searchResultsDisplay = root.findViewById(R.id.search_results_matches_frame_layout)
        recyclerView = root.findViewById(R.id.search_results_list)
        searchAdapter = SearchAdapter(root.context)

        with(recyclerView) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = searchAdapter
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (callback != null && dy > 0) {
                    callback!!.clearKeyboard()
                }
            }
        })
        return root
    }

    fun setViewModel(viewModel: SearchViewModel) {
        searchViewModel = viewModel
        //adapterInUse.setViewModel(searchViewModel)
    }

    fun show() {
        searchResultsDisplay.visibility = VISIBLE
    }

    fun hide() {
        searchResultsDisplay.visibility = GONE
    }

    fun isShowing(): Boolean {
        return searchResultsDisplay.isVisible
    }

    fun startSearch(searchTerm: String) {
        searchAdapter.doSearchOn(searchTerm)
    }

    fun setCallback(callbackIn: Callback) {
        callback = callbackIn
    }

    interface Callback {
        fun clearKeyboard()
    }

    private var callback: Callback? = null

    fun setCallbackInAdapter(callbackIn: SearchAdapter.Callback) {
        searchAdapter.setCallback(callbackIn)
    }
}