package com.style.quiztrivia.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ResponseModel(
    val response_code: Int,
    val results: MutableList<ResultQuiz>
) : Parcelable

@Parcelize
data class ResultQuiz(
    var category: String = "",
    var correct_answer: String = "",
    var difficulty: String = "",
    var incorrect_answers: MutableList<String>,
    var question: String = "",
    var type: String = ""
) : Parcelable