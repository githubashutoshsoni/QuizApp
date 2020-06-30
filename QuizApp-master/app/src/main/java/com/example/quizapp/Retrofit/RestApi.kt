package com.example.quizapp.Retrofit

import com.example.quizapp.util.LiveDataCallAdapter
import com.example.quizapp.util.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RestApi {

    val quizApiService: QuizServiceApi
    val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
//        .addInterceptor(DecodeInterceptor())
        .build()

    init {
        val retrofit = Retrofit.Builder().baseUrl("http://www.google.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
        quizApiService = retrofit.create(QuizServiceApi::class.java)
    }

}
