package com.style.quiztrivia.ui.onboard

import androidx.lifecycle.*
import com.style.quiztrivia.util.Result
import com.style.quiztrivia.database.UserModel
import com.style.quiztrivia.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: UserRepository) : ViewModel() {


    fun setUpUserName(userModel: UserModel) {

        viewModelScope.launch {

            repo.insertUserDetails(userModel)

        }

    }

    val userModel: LiveData<UserModel?> = repo.observeUser().switchMap {
        getUserDetails(it)
    }

    fun getUserDetails(userResult: Result<UserModel>): LiveData<UserModel?> {


        val result = MutableLiveData<UserModel>()

        if (userResult is Result.Success) {
            result.value = userResult.data

        } else {

            result.value = UserModel(userName = "Anonymous")

        }

        return result
    }


}