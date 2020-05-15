package com.example.quizapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class QuizApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}