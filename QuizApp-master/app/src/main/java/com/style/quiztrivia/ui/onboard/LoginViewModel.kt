package com.style.quiztrivia.ui.onboard

import androidx.lifecycle.*
import com.style.quiztrivia.database.Result
import com.style.quiztrivia.database.UserModel
import com.style.quiztrivia.ui.quiz.UserRepository
import com.style.quiztrivia.util.AbsentLiveData
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: UserRepository) : ViewModel() {


    fun setUpUserName(userModel: UserModel) {

        viewModelScope.launch {

            repo.insertUserDetails(userModel)

        }

    }

    val userModel: LiveData<UserModel> = repo.observeUser().switchMap {
        getUserDetails(it)
    }

    fun getUserDetails(userResult: Result<UserModel>): LiveData<UserModel> {


        val result = MutableLiveData<UserModel>()

        if (userResult is Result.Success) {
            result.value = userResult.data

        } else {

            result.value = UserModel(userName = "Anonymous")

        }

        return result
    }


}