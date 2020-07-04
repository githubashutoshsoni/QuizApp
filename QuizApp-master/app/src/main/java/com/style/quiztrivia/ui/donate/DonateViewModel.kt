package com.style.quiztrivia.ui.donate

import androidx.lifecycle.*
import com.style.quiztrivia.R
import com.style.quiztrivia.database.Result
import com.style.quiztrivia.database.UserModel
import com.style.quiztrivia.ui.quiz.UserRepository
import com.style.quiztrivia.util.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DonateViewModel(private val repository: UserRepository) : ViewModel() {


    private var checkoutEvent: MutableLiveData<Event<Int>> = MutableLiveData()

    val checkoutLive: LiveData<Event<Int>> = checkoutEvent


    val userModel: LiveData<UserModel> = repository.observeUser().switchMap {

        filterUser(it)
    }



   suspend fun getUserDetails(): LiveData<UserModel> {

        val userModel: MutableLiveData<UserModel> = MutableLiveData()

        viewModelScope.launch {


            val user = repository.getUser()

            if (user is Result.Success) {
                userModel.value = user.data

            } else {

                userModel.value = UserModel(userName = "Anonymous")
            }


        }
        return userModel


    }


    fun filterUser(userResult: Result<UserModel>): LiveData<UserModel> {

        val result: MutableLiveData<UserModel> = MutableLiveData()
        if (userResult is Result.Success) {

            result.value = userResult.data
        } else {

            result.value = UserModel(userName = "Anonymous")
        }

        return result
    }


    private val _snackBarError: MutableLiveData<Event<Int>> = MutableLiveData()

    val snackBarError: LiveData<Event<Int>> = _snackBarError;

    fun proceedToCheckout(enteredAmount: String) {

        if (enteredAmount.toInt() <= 0) {
            _snackBarError.value = Event(R.string.less_than_zero)
        } else {
            checkoutEvent.value = Event(enteredAmount.toInt() * 100)
        }

    }


    fun successFullyPaid() {
        _snackBarError.value = Event(R.string.thanks_for_purchase)

    }


}