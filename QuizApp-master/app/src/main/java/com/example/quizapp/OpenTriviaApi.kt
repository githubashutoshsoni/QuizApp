package com.example.quizapp

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTriviaApi {

    @GET(" ")
    fun getResponseData(
        @Query("category") categoryId: Int,
        @Query("encode") encode: String = "url3986",
        @Query("type") type: String = "multiple",
        @Query("difficulty") difficulty: String = "easy",
        @Query("amount") amount: Int = 10
    ): Call<ResponseBody>


}