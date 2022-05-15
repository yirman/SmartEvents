package com.german.events.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.german.events.databinding.FragmentPublicEventsBinding
import com.german.events.model.Event
import com.german.events.ui.adapter.EventAdapter
import com.german.events.ui.viewmodel.EventViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PublicEventsFragment : Fragment(), EventAdapter.OnSubscribeListener{

    private lateinit var binding: FragmentPublicEventsBinding
    private val viewModel: EventViewModel by viewModels()

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
        binding = FragmentPublicEventsBinding.inflate(inflater, container, false)
        adapter = EventAdapter(requireContext(), this)
        binding.layoutRecyclerEvents.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.layoutRecyclerEvents.recyclerview.adapter = adapter
        viewModel.publicEventsList.observe(viewLifecycleOwner){
            adapter.setItems(it)
        }
        viewModel.requestPublicEvents()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = PublicEventsFragment()
    }

    override fun onSubscribe(event: Event) {
        viewModel.subscribeUser(event.eventId!!)
    }
}