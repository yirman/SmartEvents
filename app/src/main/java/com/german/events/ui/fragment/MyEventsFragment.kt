package com.german.events.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.german.events.databinding.FragmentMyEventsBinding
import com.german.events.model.Event
import com.german.events.ui.dialog.AddEventDialog
import com.german.events.ui.adapter.EventAdapter
import com.german.events.ui.dialog.EditEventDialog
import com.german.events.ui.viewmodel.EventViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MyEventsFragment : Fragment(), AddEventDialog.OnAddEventListener, EditEventDialog.OnEditEventListener, EventAdapter.OnEditClickListener{

    private lateinit var binding: FragmentMyEventsBinding

    private val viewModel: EventViewModel by viewModels()

    private lateinit var adapter : EventAdapter

    @Inject
    lateinit var addEventDialog : AddEventDialog

    @Inject
    lateinit var editEventDialog : EditEventDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        adapter = EventAdapter(context = requireContext(), onEditClickListener = this)
        binding.layoutRecyclerEvents.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.layoutRecyclerEvents.recyclerview.adapter = adapter
        viewModel.myEventsList.observe(viewLifecycleOwner){
            adapter.setItems(it)
        }
        binding.add.setOnClickListener {
            addEventDialog.onAddEventListener = this
            addEventDialog.show(childFragmentManager, "AddEventDialog")
        }
        viewModel.requestMyEvents()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyEventsFragment()
    }

    override fun onAddEvent(event: Event) {
        viewModel.addEvent(event)
    }

    override fun onEditClick(event: Event) {
        editEventDialog.onEditEventListener = this
        editEventDialog.event = event
        editEventDialog.show(childFragmentManager, "EditEventDialog")
    }

    override fun onEditEvent(event: Event) {
        viewModel.editEvent(event)
    }
}