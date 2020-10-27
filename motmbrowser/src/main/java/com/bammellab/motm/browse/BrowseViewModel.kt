package com.bammellab.motm.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BrowseViewModel : ViewModel() {

    private val _browseInfoStringArray
            = MutableLiveData<List<String>>().apply {
        value = listOf("")

    }
    val browseInfoStringArray: LiveData<List<String>> = _browseInfoStringArray

    fun updateSearchStringArray(devInfo: List<String>) {
        _browseInfoStringArray.value = devInfo
    }

    // TODO: copy the contents to the clipboard on a long click
    fun onClick() {

    }
}


