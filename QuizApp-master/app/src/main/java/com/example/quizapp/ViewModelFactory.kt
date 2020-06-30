package com.example.quizapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.Retrofit.RestApi
import com.example.quizapp.ui.ui.category.CategoryViewModel
import com.example.quizapp.ui.ui.onboard.LoginViewModel
import com.example.quizapp.ui.result.ResultViewModel
import com.example.quizapp.ui.quiz.QuizRepository
import com.example.quizapp.ui.quiz.QuizViewModel
import com.example.quizapp.util.AppExecutors
import java.lang.IllegalArgumentException

class ViewModelFactory constructor(
    private val quizRepository: QuizRepository,
    private val appExecutors: AppExecutors,
    private val restApi: RestApi
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return with(modelClass) {
            when {

                isAssignableFrom(QuizViewModel::class.java) ->
                    QuizViewModel(quizRepository)


                isAssignableFrom(CategoryViewModel::class.java) ->
                    CategoryViewModel(quizRepository, appExecutors,restApi  )

                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(quizRepository)

                isAssignableFrom(ResultViewModel::class.java) ->
                    ResultViewModel(quizRepository)

                else ->
                    throw IllegalArgumentException("Unknown class exception ${modelClass.name}")

            }

        } as T

    }
}