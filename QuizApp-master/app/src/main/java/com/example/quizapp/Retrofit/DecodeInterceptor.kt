package com.example.quizapp.Retrofit

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

class DecodeInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {


        var response = chain.proceed(chain.request())

        return if (response.body != null && response.code == 200) {
            val contentType = response.body?.contentType()
            response.newBuilder()
                .body(ResponseBody.create(contentType, DecodeUrl.decode(response.body!!.string())))
                .build()
        } else
            response

    }

    object DecodeUrl {
        fun decode(responseBody: String): String {
            return try {
                var prevURL = ""
                var decodeURL = responseBody
                while (prevURL != decodeURL) {
                    prevURL = decodeURL
                    decodeURL = URLDecoder.decode(decodeURL, "UTF-8")
                }
                decodeURL
            } catch (e: UnsupportedEncodingException) {
                "Issue while decoding" + e.stackTrace
            }
        }




    }


}