package com.style.quiztrivia

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.style.quiztrivia.repository.UserRepository
import com.style.quiztrivia.util.ServiceLocator

import timber.log.Timber

class QuizApplication : Application() {

    val userRepository: UserRepository
        get() = ServiceLocator.provideQuizRepository(this)

    override fun onCreate() {
        super.onCreate()


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

    }

}