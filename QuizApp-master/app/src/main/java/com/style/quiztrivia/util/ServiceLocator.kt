package com.style.quiztrivia.util

import android.content.Context
import androidx.room.Room
import com.style.quiztrivia.repository.DefaultQuizRepository
import com.style.quiztrivia.repository.QuizLocalDataSource
import com.style.quiztrivia.repository.QuizRemoteDataSource
import com.style.quiztrivia.retrofit.RestApi
import com.style.quiztrivia.ui.quiz.UserRepository
import com.style.quiztrivia.database.AppDatabase

object ServiceLocator {

    private val lock = Any()

    private var database: AppDatabase? = null

    @Volatile
    var userRepository: UserRepository? = null


    fun provideQuizRepository(context: Context): UserRepository {

        synchronized(this) {

            return userRepository
                ?: userRepository
                ?: createCommonRepository(
                    context
                )
        }

    }


    fun createCommonRepository(context: Context): UserRepository {


        return DefaultQuizRepository(
            createQuizLocalDataSource(
                context
            ),
            QuizRemoteDataSource(
                RestApi(),
                AppExecutors()
            )
        )

    }


    fun createQuizLocalDataSource(context: Context): UserRepository {

        val database = database
            ?: createDatabase(context)

        return QuizLocalDataSource(
            database.scoreDao(),
            database.userDao()
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
