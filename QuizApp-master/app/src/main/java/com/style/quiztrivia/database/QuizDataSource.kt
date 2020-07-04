package com.style.quiztrivia.database

import androidx.lifecycle.LiveData

interface QuizDataSource {


    fun observeQuizList()

    suspend fun getQuizList()


    fun submitFeedback()

    fun saveScore()

    fun fetchScore()

    fun deleteAllScore()

    fun deleteScore(resultId: String)


}