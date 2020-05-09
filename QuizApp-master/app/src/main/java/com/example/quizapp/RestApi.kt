package com.example.quizapp

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class RestApi {

    val openTriviaApi: OpenTriviaApi
    val logging = HttpLoggingInterceptor().setLevel(BASIC)
    var client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val moshi: Moshi = Moshi.Builder().add(QuizMoAdapter()).build()


    init {
        val retrofit = Retrofit.Builder().baseUrl("http://www.google.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))

            .client(client)
            .build()
        openTriviaApi = retrofit.create(OpenTriviaApi::class.java)
    }

}
