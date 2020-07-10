package com.style.quiztrivia.retrofit

import com.style.quiztrivia.BuildConfig
import com.style.quiztrivia.util.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RestApi {

    val quizApiService: QuizServiceApi
    val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    init {

        val httpBuilder = OkHttpClient.Builder()



        if (BuildConfig.DEBUG) {
            httpBuilder.addInterceptor(logging)
        }

        val client: OkHttpClient = httpBuilder.build()

        val retrofit = Retrofit.Builder().baseUrl("http://www.google.com")


            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
        quizApiService = retrofit.create(QuizServiceApi::class.java)
    }

}
