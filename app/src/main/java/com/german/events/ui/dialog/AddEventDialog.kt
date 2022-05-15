package com.german.events.ui.dialog

import com.german.events.R
import com.german.events.databinding.EventFormBinding
import com.german.events.model.Event
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddEventDialog : BaseEventDialog() {

    var onAddEventListener : OnAddEventListener? = null

    override fun getTitle(): String = resources.getString(R.string.create_event)

    override fun getActionText(): String = resources.getString(R.string.create)

    override fun onViewPrepared(binding: EventFormBinding) = Unit

    override fun handleEvent(event: Event) {
        onAddEventListener?.onAddEvent(event)
    }

    interface OnAddEventListener{
        fun onAddEvent(event: Event)
    }
}