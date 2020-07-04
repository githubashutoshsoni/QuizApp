package com.style.quiztrivia.ui.result

import androidx.lifecycle.ViewModel
import com.style.quiztrivia.ui.quiz.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class ResultViewModel(private val rpo: UserRepository) : ViewModel() {

    private lateinit var dbReference: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String

    init {
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid ?: UUID.randomUUID().toString()
        dbReference = FirebaseDatabase.getInstance()

    }

    fun submitFeedback(name: String, desc: String) {
        dbReference.getReference("feed_back").child(userId).child(name).setValue(desc)
    }


}