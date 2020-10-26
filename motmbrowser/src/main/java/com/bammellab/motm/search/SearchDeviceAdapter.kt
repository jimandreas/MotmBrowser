@file:Suppress("UnnecessaryVariable", "JoinDeclarationAndAssignment", "unused")

package com.bammellab.motm.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bammellab.motm.R


class SearchDeviceAdapter(
    private val contextLocal: Context,
    private val scanViewModel: SearchViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val clickHandler: DeviceAdapterOnClickHandler
) : RecyclerView.Adapter<SearchDeviceAdapter.DeviceViewHolder>() {

    inner class DeviceViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        val deviceInfoText = view.findViewById<View>(R.id.recycler_search_info_text) as TextView
        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }
    }

    interface DeviceAdapterOnClickHandler {
        fun onClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val inflateStuff = LayoutInflater.from(parent.context)
        val rootView = inflateStuff.inflate(R.layout.fragment_search_list_item, parent, false)

        return DeviceViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val strings : List<String>
        if (scanViewModel.searchInfoStringArray.value == null) {
            holder.deviceInfoText.text = contextLocal.getString(R.string.funky)
            return
        }
        strings = scanViewModel.searchInfoStringArray.value as List<String>
        if (position <= scanViewModel.searchInfoStringArray.value!!.size-1) {
            holder.deviceInfoText.text = strings[position]
        } else {
            holder.deviceInfoText.text = contextLocal.getString(R.string.funky)
        }
    }

    override fun getItemCount(): Int {
        return scanViewModel.searchInfoStringArray.value!!.size
    }

}


