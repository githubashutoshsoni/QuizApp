package com.style.quiztrivia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.style.quiztrivia.retrofit.RestApi
import com.style.quiztrivia.ui.category.CategoryViewModel
import com.style.quiztrivia.ui.donate.DonateViewModel
import com.style.quiztrivia.ui.onboard.LoginViewModel
import com.style.quiztrivia.ui.result.ResultViewModel
import com.style.quiztrivia.ui.quiz.UserRepository
import com.style.quiztrivia.ui.quiz.QuizViewModel
import com.style.quiztrivia.util.AppExecutors

import java.lang.IllegalArgumentException

class ViewModelFactory constructor(
    private val userRepository: UserRepository,
    private val appExecutors: AppExecutors,
    private val restApi: RestApi
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return with(modelClass) {
            when {

                isAssignableFrom(QuizViewModel::class.java) ->
                    QuizViewModel(userRepository)


                isAssignableFrom(CategoryViewModel::class.java) ->
                    CategoryViewModel(userRepository, appExecutors, restApi)

                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(userRepository)

                isAssignableFrom(ResultViewModel::class.java) ->
                    ResultViewModel(userRepository)

                isAssignableFrom(DonateViewModel::class.java) ->
                    DonateViewModel(userRepository)

                else ->
                    throw IllegalArgumentException("Unknown class exception ${modelClass.name}")

            }

        } as T

    }
}