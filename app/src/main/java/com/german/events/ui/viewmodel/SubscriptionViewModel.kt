package com.german.events.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.german.events.model.Event
import com.german.events.model.Subscription
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth,
    firebaseFirestore: FirebaseFirestore
) : BaseViewModel(firebaseAuth, firebaseFirestore) {


    private val _mySubscriptionEventsList : MutableLiveData<List<Event>> = MutableLiveData()
    val mySubscriptionEventsList : LiveData<List<Event>> = _mySubscriptionEventsList

    init {
        firebaseFirestore.collection("subscriptions")
            .whereEqualTo("idSubscriptor", firebaseAuth.uid)
            .addSnapshotListener { value, error ->
                value?.let {
                    val subscriptions = value.toObjects(Subscription::class.java)
                    handleSubscriptions(subscriptions)
                }
            }
    }

    fun requestSubscribedEvents(){
        firebaseFirestore.collection("subscriptions")
            .whereEqualTo("idSubscriptor", firebaseAuth.uid)
            .get()
            .addOnCompleteListener { taskSubscriptions ->
                if(taskSubscriptions.isSuccessful){
                    val subscriptions = taskSubscriptions.result.toObjects(Subscription::class.java)
                    handleSubscriptions(subscriptions)
                }
            }
    }

    private fun handleSubscriptions(subscriptions: MutableList<Subscription>){

        if(subscriptions.isNotEmpty()){
            val idEvents = subscriptions.map { it.idEvent }
            firebaseFirestore.collection("events")
                .whereIn(FieldPath.documentId(), idEvents)
                .get()
                .addOnCompleteListener { taskEvents ->
                    if(taskEvents.isSuccessful){
                        val events: MutableList<Event> = taskEvents.result.toObjects(Event::class.java)
                        _mySubscriptionEventsList.postValue(events)
                    }
                }
        }
        else{
            _mySubscriptionEventsList.postValue(mutableListOf())
        }
    }

}