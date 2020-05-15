package com.example.quizapp

import android.util.Log
import com.example.quizapp.model.ResponseCategoryJson
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.io.UnsupportedEncodingException
import java.net.URLDecoder


class QuizMoAdapter {


    @ToJson
    fun quizToJson(response: ResponseCategoryJson): ResponseCategoryJson {

        return response

    }

    val TAG = QuizMoAdapter::class.java.simpleName

    @FromJson
    fun jsonToQuiz(responseIs: ResponseCategoryJson): ResponseCategoryJson {


        val incorrectList = responseIs.incorrect_answers
        incorrectList?.add(responseIs.correct_answer)

        // iterate it using a mutable iterator and modify values

        val iterate = incorrectList?.listIterator()



        while (iterate?.hasNext()!!) {
            val oldValue = iterate.next()
            iterate.set(decode(oldValue))
        }





        for (element in incorrectList) {
            Log.d(TAG, "changed element $element")
        }



        return ResponseCategoryJson(
            decode(responseIs.category),
            decode(responseIs.correct_answer),
            decode(responseIs.difficulty),
            incorrectList,
            decode(responseIs.question),
            decode(responseIs.type)

        );


    }


    private fun decode(url: String): String {
        return try {
            var prevURL = ""
            var decodeURL = url
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