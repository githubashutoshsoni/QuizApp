package com.example.quizapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizapp.C
import com.example.quizapp.R
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class CommonErrorModel : ViewModel() {

    private var _isLoading: MutableLiveData<LoadingModel> = MutableLiveData()
    var isLoading: LiveData<LoadingModel> = _isLoading

    private var _errorModel: MutableLiveData<ErrorModel> = MutableLiveData()
    var errorModel: LiveData<ErrorModel> = _errorModel


    protected fun errorDialog(
        show: Boolean, e: Throwable?, isListEmpty: Boolean
    ) {

        val errorCode: Int

        var errorMessage: Int = R.string.something_wrong

        if (e is HttpException) {

            errorCode = when (e.code()) {

                HttpURLConnection.HTTP_BAD_REQUEST -> C.RESPONSE_UNKNOWN
                else -> C.ERROR_CODE_DEFAULT

            }


        } else if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException) {

            errorMessage = R.string.no_internet
            errorCode = C.NO_INTERNET_CONNECTION


        } else {
            errorCode = C.ERROR_CODE_DEFAULT
        }


        ErrorModel(show, errorCode, errorMessage, isListEmpty)

    }

    protected fun updateLoading(loading: Boolean, isListEmpty: Boolean=true) {

        _isLoading.value = LoadingModel(loading, isListEmpty)

    }


}