package com.example.quizapp

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenTriviaApi {

    @GET("https://opentdb.com/api.php?amount=10&difficulty=easy&type=multiple&encode=url3986")
    fun getResponseData(@Query("category") id: Int): Call<ResponseBody>


}