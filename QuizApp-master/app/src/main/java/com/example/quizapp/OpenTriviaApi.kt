package com.example.quizapp

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface OpenTriviaApi {

    @GET("https://opentdb.com/api.php?amount=10&category=10&difficulty=easy&type=multiple&encode=url3986")
    fun getResponseData(): Call<ResponseBody>



}