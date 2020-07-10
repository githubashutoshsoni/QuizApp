package com.style.quiztrivia.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.style.quiztrivia.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.style.quiztrivia.util.Event
import kotlinx.coroutines.launch
import java.util.*

class ResultViewModel(private val userRepository: UserRepository) : ViewModel() {

    private lateinit var dbReference: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String


    private val _startLogoutEvent: MutableLiveData<Event<Unit>> = MutableLiveData()

    val startLogoutEvent: LiveData<Event<Unit>> = _startLogoutEvent


    private val _startDonationEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    val startDonationEvent: LiveData<Event<Unit>> = _startDonationEvent


    fun dontate() {
        _startDonationEvent.value = Event(Unit)
    }


    init {
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid ?: UUID.randomUUID().toString()
        dbReference = FirebaseDatabase.getInstance()

    }

    fun submitFeedback(name: String, desc: String) {
        dbReference.getReference("feed_back").child(userId).child(name).setValue(desc)
    }


    fun logout() {


        viewModelScope.launch {

            FirebaseAuth.getInstance().signOut()
            userRepository.nukeDetails()
            _startLogoutEvent.value = Event(Unit)
        }
    }

}