@file:Suppress("UnnecessaryVariable")

package com.bammellab.motm.browse

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bammellab.motm.databinding.FragmentBrowseBinding


class BrowseFragment : Fragment() {

    private lateinit var scanViewModel: BrowseViewModel
    private lateinit var binding: FragmentBrowseBinding
    private lateinit var bcontext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBrowseBinding.inflate(inflater)
        binding.lifecycleOwner = this

        scanViewModel =
            ViewModelProvider(this).get(BrowseViewModel::class.java)
        binding.viewModel = scanViewModel
        bcontext = binding.root.context



        return binding.root
    }

}