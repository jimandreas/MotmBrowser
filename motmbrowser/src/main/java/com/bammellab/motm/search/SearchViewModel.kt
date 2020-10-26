package com.bammellab.motm.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _searchInfoStringArray
            = MutableLiveData<List<String>>().apply {
        value = listOf("")

    }
    val searchInfoStringArray: LiveData<List<String>> = _searchInfoStringArray

    fun updateSearchStringArray(devInfo: List<String>) {
        _searchInfoStringArray.value = devInfo
    }

    // TODO: copy the contents to the clipboard on a long click
    fun onClick() {

    }
}


