package com.style.quiztrivia.ui.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.style.quiztrivia.database.UserModel
import com.style.quiztrivia.ui.quiz.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: UserRepository) : ViewModel() {


    fun setUpUserName(userModel: UserModel) {

        viewModelScope.launch {

            repo.insertUserDetails(userModel)

        }

    }

}