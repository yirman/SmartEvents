package com.german.events.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.german.events.databinding.ItemEventBinding
import com.german.events.model.Event
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class EventAdapter(val listener: OnSubscribeListener) : RecyclerView.Adapter<EventAdapter.EventHolder>() {

    private val items = mutableListOf<Event>()

    var firebaseUser: FirebaseUser? = null
        @Inject set

    fun setItems(items: List<Event>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val binding: ItemEventBinding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventHolder(binding, listener, firebaseUser!!)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class EventHolder(private val binding: ItemEventBinding, private val onSubscribeListener: OnSubscribeListener, private val firebaseUser: FirebaseUser) : RecyclerView.ViewHolder(binding.root){
        fun bind(event: Event) {
            binding.name.text = event.name
            binding.address.text = event.address
            binding.dateHour.text = event.date + " " + event.time

            if(firebaseUser.uid == event.createdBy){
                binding.subscribe.visibility = View.GONE
            }
            else{
                binding.subscribe.visibility = View.VISIBLE
                binding.subscribe.setOnClickListener {
                    onSubscribeListener.onSubscribe(event.eventId!!)
                }
            }
        }
    }

    interface OnSubscribeListener{
        fun onSubscribe(id: String)
    }
}