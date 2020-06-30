package com.example.quizapp.Retrofit

import android.telecom.Call
import androidx.lifecycle.LiveData
import com.example.quizapp.database.ResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizServiceApi {

    @GET("https://opentdb.com/api.php?amount=10&difficulty=easy&type=multiple&encode=url3986")
    fun getResponseData(@Query("category") id: Int): retrofit2.Call<ResponseModel>


}