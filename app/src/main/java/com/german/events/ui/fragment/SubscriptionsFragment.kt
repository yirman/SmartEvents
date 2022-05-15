package com.german.events.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.german.events.databinding.FragmentSubscriptionsBinding
import com.german.events.ui.adapter.EventAdapter
import com.german.events.ui.viewmodel.SubscriptionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionsFragment : Fragment() {

    private lateinit var binding: FragmentSubscriptionsBinding
    private val viewModel: SubscriptionViewModel by viewModels()

    private lateinit var adapter : EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubscriptionsBinding.inflate(layoutInflater, container, false)
        adapter = EventAdapter(context = requireContext())
        binding.layoutRecyclerSubscriptions.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.layoutRecyclerSubscriptions.recyclerview.adapter = adapter
        viewModel.mySubscriptionEventsList.observe(viewLifecycleOwner){
            adapter.setItems(it)
        }

        viewModel.requestSubscribedEvents()
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = SubscriptionsFragment()
    }
}