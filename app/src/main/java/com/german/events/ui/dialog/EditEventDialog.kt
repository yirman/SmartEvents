package com.german.events.ui.dialog

import android.text.format.DateFormat
import com.german.events.R
import com.german.events.databinding.EventFormBinding
import com.german.events.model.Event
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class EditEventDialog : BaseEventDialog() {

    var event: Event? = null
    var onEditEventListener: OnEditEventListener? = null

    override fun getTitle(): String = resources.getString(R.string.edit_event)

    override fun getActionText(): String = resources.getString(R.string.edit)

    override fun onViewPrepared(binding: EventFormBinding) {
        event?.apply {
            binding.name.setText(name)
            binding.address.setText(address)
            binding.date.setText(DateFormat.format("MM/dd/yyyy", Date(timestamp?.toDate()?.time!!)).toString())
            binding.hour.setText(DateFormat.format("hh:mm aa", Date(timestamp?.toDate()?.time!!)).toString())
            hourSelected = timestamp?.toDate()?.time!!
            dateSelected = timestamp?.toDate()?.time!!
        }
    }

    override fun handleEvent(event: Event) {
        this.event?.let {
            event.eventId = it.eventId
        }
        onEditEventListener?.onEditEvent(event)
    }

    interface OnEditEventListener{
        fun onEditEvent(event: Event)
    }
}