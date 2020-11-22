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

@file:Suppress("UNUSED_PARAMETER", "unused")

package com.bammellab.motm.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is search Fragment"
    }
    val text: LiveData<String> = _text


    private val _previousSearchStringList = MutableLiveData<List<String>>().apply {
        value = listOf("")

    }
    val previousSearchStringList: LiveData<List<String>> = _previousSearchStringList


    // if true - expand the list of PDBs in the search match results.
    // if false - just keep the header with the number of matches displayed
    private val _expandPdbListBoolean = MutableLiveData<Boolean>().apply {
        value = true
    }
    val expandPdbListBoolean: LiveData<Boolean> = _expandPdbListBoolean
    

    fun updateSearchStringList(newSearchString: String) {

    }
}