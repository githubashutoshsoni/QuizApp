package com.example.quizapp.ui.quiz

import androidx.lifecycle.LiveData
import com.example.quizapp.Retrofit.ApiResponse
import com.example.quizapp.database.ResponseModel

import com.example.quizapp.database.Result

interface QuizRepository {


    fun getQuizList(): LiveData<Result<ResponseModel>>

    suspend fun deleteScoreList()

    fun getMotivatingQuote()

    fun deleteUser()

    fun logout()

    fun saveUserScore()

    fun getUserScore()

    fun loginUser()

}