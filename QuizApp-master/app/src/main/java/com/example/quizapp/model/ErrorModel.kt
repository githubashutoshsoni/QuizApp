package com.example.quizapp.model

data class ErrorModel(
    var show: Boolean = false,
    var errorCode: Int,
    var errorMessaageId: Int,
    var isListEmpty: Boolean = true
)


data class LoadingModel(
    var isListEmpty: Boolean,
    var isLoading: Boolean
)