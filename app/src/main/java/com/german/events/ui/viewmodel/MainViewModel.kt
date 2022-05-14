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
class MainViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth,
    firebaseFirestore: FirebaseFirestore
) : BaseViewModel(firebaseAuth, firebaseFirestore) {

    private val _eventsStatus : MutableLiveData<Resource.Status> = MutableLiveData()
    val eventsStatus : LiveData<Resource.Status> = _eventsStatus

    private val _eventsList : MutableLiveData<List<Event>> = MutableLiveData()
    val eventsList : LiveData<List<Event>> = _eventsList


    fun queryMyEvents(uid: String){
        _eventsStatus.postValue(Resource.Status.LOADING)
        firebaseFirestore.collection("events")
            .whereEqualTo("uidCreator", uid)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    _eventsStatus.postValue(Resource.Status.SUCCESS)
                    val events = it.result.toObjects(Event::class.java)
                    _eventsList.postValue(events)
                }
                else{
                    _eventsStatus.postValue(Resource.Status.ERROR)
                }
            }
    }


}