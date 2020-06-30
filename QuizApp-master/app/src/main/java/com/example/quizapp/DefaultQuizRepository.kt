package com.example.quizapp

import androidx.lifecycle.LiveData

import com.example.quizapp.database.ResponseModel
import com.example.quizapp.database.Result
import com.example.quizapp.ui.quiz.QuizRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultQuizRepository(
    private val quizLocalRepository: QuizRepository,
    private val quizRemoteRepository: QuizRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO


) : QuizRepository {
    override fun getQuizList(): LiveData<Result<ResponseModel>> {
        TODO("Not yet implemented")
    }


    override suspend fun deleteScoreList() {

    }

    override fun getMotivatingQuote() {

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