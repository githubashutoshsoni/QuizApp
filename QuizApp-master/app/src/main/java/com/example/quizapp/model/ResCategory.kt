package com.example.quizapp.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResCategory(
    val category: String = "",
    val correct_answer: String = "",
    val difficulty: String = "",
    val all_options: List<String>? = null,
    val question: String = "",
    val type: String = ""
)