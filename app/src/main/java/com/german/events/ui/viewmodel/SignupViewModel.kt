package com.german.events.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.german.events.model.User
import com.german.events.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class SignupViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth,
    firebaseFirestore: FirebaseFirestore
) : BaseViewModel(firebaseAuth, firebaseFirestore) {

    private val _signupStatus : MutableLiveData<Resource.Status> = MutableLiveData()
    val signupStatus : LiveData<Resource.Status> = _signupStatus

    fun signUpUser(name: String, lastName: String, phone: String, email: String, password: String){
        _signupStatus.postValue(Resource.Status.LOADING)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if(authResult.isSuccessful){
                    val user = User(name, lastName, phone)
                    firebaseFirestore.collection("users")
                        .document(firebaseAuth.currentUser?.uid!!)
                        .set(user)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                _signupStatus.postValue(Resource.Status.SUCCESS)
                            }
                            else{
                                _signupStatus.postValue(Resource.Status.ERROR)
                            }
                        }
                }
                else{
                    _signupStatus.postValue(Resource.Status.ERROR)
                }
            }
    }

}