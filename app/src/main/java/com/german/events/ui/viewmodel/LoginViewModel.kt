package com.german.events.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.german.events.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth,
    firebaseFirestore: FirebaseFirestore
) : BaseViewModel(firebaseAuth, firebaseFirestore) {

    private val _loginStatus : MutableLiveData<Resource.Status> = MutableLiveData()
    val loginStatus : LiveData<Resource.Status> = _loginStatus

    fun signIn(email: String, password: String) {
        _loginStatus.postValue(Resource.Status.LOADING)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    _loginStatus.postValue(Resource.Status.SUCCESS)
                }
                else{
                    _loginStatus.postValue(Resource.Status.ERROR)
                }
            }
    }
}