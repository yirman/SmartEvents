package com.german.events.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.german.events.model.Event
import com.german.events.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth,
    firebaseFirestore: FirebaseFirestore
) : BaseViewModel(firebaseAuth, firebaseFirestore) {

    private val _publicEventStatus : MutableLiveData<Resource.Status> = MutableLiveData()
    val publicEventStatus : LiveData<Resource.Status> = _publicEventStatus


    private val _publicEventsList : MutableLiveData<List<Event>> = MutableLiveData()
    val publicEventsList : LiveData<List<Event>> = _publicEventsList

    fun requestPublicEvents() {
        _publicEventStatus.postValue(Resource.Status.LOADING)
        firebaseFirestore.collection("events")
            .whereNotEqualTo("createdBy", firebaseAuth.uid)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    _publicEventStatus.postValue(Resource.Status.SUCCESS)
                    val events = it.result.toObjects(Event::class.java)
                    _publicEventsList.postValue(events)
                }
                else{
                    _publicEventStatus.postValue(Resource.Status.ERROR)
                }
            }
    }


}