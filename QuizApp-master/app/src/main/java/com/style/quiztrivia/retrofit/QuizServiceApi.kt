package com.style.quiztrivia.retrofit


import androidx.lifecycle.LiveData
import com.style.quiztrivia.database.ResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizServiceApi {

    @GET("https://opentdb.com/api.php?amount=10&difficulty=easy&type=multiple&encode=url3986")
    fun getResponseData(@Query("category") id: Int): retrofit2.Call<ResponseModel>


    @GET("https://opentdb.com/api.php?amount=10&difficulty=easy&type=multiple&encode=url3986")
    fun getResponseLive(@Query("category") id: Int): LiveData<ApiResponse<ResponseModel>>

}