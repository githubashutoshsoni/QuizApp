package com.example.quizapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.quizapp.Retrofit.*

import com.example.quizapp.database.ResponseModel
import com.example.quizapp.database.Result
import com.example.quizapp.ui.quiz.QuizRepository
import com.example.quizapp.util.AbsentLiveData
import com.example.quizapp.util.AppExecutors
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class QuizRemoteDataSource internal constructor(
    private val restApi: RestApi,
    private val appExecutors: AppExecutors,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : QuizRepository {

    val responseMutableLiveDispatcher = MutableLiveData<Result<ResponseModel>>()


    override fun getQuizList(): LiveData<Result<ResponseModel>> {


        return AbsentLiveData.create()


    }

    override suspend fun deleteScoreList() {

    }

    override fun getMotivatingQuote() {
        TODO("Not yet implemented")
    }


    override fun deleteUser() {

    }

    override fun logout() {

    }

    override fun saveUserScore() {

    }

    override fun getUserScore() {

    }

    override fun loginUser() {

    }

}