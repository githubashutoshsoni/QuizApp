package com.example.quizapp.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
data class ResponseCategoryJson(
    val category: String = "",
    val correct_answer: String = "",
    val difficulty: String = "",
    val incorrect_answers: MutableList<String>? = null,
    val question: String = "",
    val type: String = ""
) : Parcelable