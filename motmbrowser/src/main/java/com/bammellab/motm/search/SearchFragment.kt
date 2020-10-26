@file:Suppress("UnnecessaryVariable")

package com.bammellab.motm.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bammellab.motm.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private lateinit var scanViewModel: SearchViewModel
    private lateinit var binding: FragmentSearchBinding
    private lateinit var bcontext: Context

    private lateinit var theRecyclerView: RecyclerView
    private lateinit var scanDeviceAdapter: SearchDeviceAdapter
    private lateinit var contextLocal: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        contextLocal = binding.root.context

        scanViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)
        binding.viewModel = scanViewModel
        bcontext = binding.root.context

        theRecyclerView = binding.recyclerListThing
        val layoutManager = LinearLayoutManager(binding.root.context)
        theRecyclerView.layoutManager = layoutManager
        theRecyclerView.setHasFixedSize(false)

        scanDeviceAdapter = SearchDeviceAdapter(
            binding.root.context,
            scanViewModel,
            this,
            object : SearchDeviceAdapter.DeviceAdapterOnClickHandler {
                override fun onClick() {
                    Toast.makeText(
                        activity,
                        "Recycler view - CardView click",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        theRecyclerView.adapter = scanDeviceAdapter

        return binding.root
    }

}