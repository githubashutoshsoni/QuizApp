package com.example.quizapp.util

import android.content.Context
import androidx.room.Room
import com.example.quizapp.DefaultQuizRepository
import com.example.quizapp.QuizLocalDataSource
import com.example.quizapp.QuizRemoteDataSource
import com.example.quizapp.Retrofit.RestApi
import com.example.quizapp.ui.quiz.QuizRepository
import com.example.quizapp.database.AppDatabase

object ServiceLocator {

    private val lock = Any()

    private var database: AppDatabase? = null

    @Volatile
    var quizRepository: QuizRepository? = null


    fun provideQuizRepository(context: Context): QuizRepository {

        synchronized(this) {

            return quizRepository
                ?: quizRepository
                ?: createCommonRepository(
                    context
                )
        }

    }


    fun createCommonRepository(context: Context): QuizRepository {


        return DefaultQuizRepository(
            createQuizLocalDataSource(
                context
            ),
            QuizRemoteDataSource(RestApi(), AppExecutors())
        )

    }


    fun createQuizLocalDataSource(context: Context): QuizRepository {

        val database = database
            ?: createDatabase(context)

        return QuizLocalDataSource(
            database.scoreDao()
        )

    }


    fun createDatabase(context: Context): AppDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java,
            "quiz.db"
        ).build()

        database = result
        return result

    }

}
