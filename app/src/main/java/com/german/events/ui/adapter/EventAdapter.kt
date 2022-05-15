package com.german.events.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.german.events.databinding.ItemEventBinding
import com.german.events.model.Event
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class EventAdapter(context: Context, private val onSubscribeClickListener: OnSubscribeClickListener? = null, private val onEditClickListener: OnEditClickListener? = null) : RecyclerView.Adapter<EventAdapter.EventHolder>() {

    private val items = mutableListOf<Event>()

    var firebaseUser: FirebaseUser? = null

    init {
        val myEntryPoint = EntryPointAccessors.fromApplication(context, EventEntryPoint::class.java)
        firebaseUser = myEntryPoint.getFirebaseUser()
    }

    fun setItems(items: List<Event>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val binding: ItemEventBinding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventHolder(binding, onSubscribeClickListener, onEditClickListener)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class EventHolder(private val binding: ItemEventBinding, private val onSubscribeClickListener: OnSubscribeClickListener?, private val onEditClickListener: OnEditClickListener?) : RecyclerView.ViewHolder(binding.root){
        fun bind(event: Event) {
            binding.name.text = event.name
            binding.address.text = event.address
            binding.dateHour.text = event.timestamp?.toDate().toString()

            onSubscribeClickListener?.let {
                binding.subscribe.visibility = View.VISIBLE
                binding.subscribe.setOnClickListener { _ ->
                    it.onSubscribe(event)
                }
            }

            onEditClickListener?.let {
                binding.iconEdit.visibility = View.VISIBLE
                binding.iconEdit.setOnClickListener { _ ->
                    it.onEditClick(event)
                }
            }
        }
    }

    interface OnSubscribeClickListener{
        fun onSubscribe(event: Event)
    }

    interface OnEditClickListener{
        fun onEditClick(event: Event)
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface EventEntryPoint {
        fun getFirebaseUser(): FirebaseUser?
    }
}