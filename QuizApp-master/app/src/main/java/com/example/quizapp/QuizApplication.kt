package com.example.quizapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.quizapp.ui.quiz.QuizRepository
import com.example.quizapp.util.ServiceLocator

import timber.log.Timber

class QuizApplication : Application() {

    val quizRepository: QuizRepository
        get() = ServiceLocator.provideQuizRepository(this)

    override fun onCreate() {
        super.onCreate()


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

    }

}