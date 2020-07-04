package com.style.quiztrivia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.style.quiztrivia.database.ResponseModel
import com.style.quiztrivia.database.Result
import com.style.quiztrivia.database.UserModel
import com.style.quiztrivia.ui.quiz.UserRepository
import com.style.quiztrivia.util.AbsentLiveData
import com.style.quiztrivia.retrofit.RestApi
import com.style.quiztrivia.util.AppExecutors
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class QuizRemoteDataSource internal constructor(
    private val restApi: RestApi,
    private val appExecutors: AppExecutors,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    val responseMutableLiveDispatcher = MutableLiveData<Result<ResponseModel>>()
    override suspend fun insertUserDetails(user: UserModel) {
        TODO("Not yet implemented")
    }

    override fun observeUser(): LiveData<Result<UserModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): Result<UserModel> {
        TODO("Not yet implemented")
    }

    override fun deleteUser() {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun nukeDetails() {
        TODO("Not yet implemented")
    }


}