package com.style.quiztrivia.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.style.quiztrivia.database.dao.ScoreDao
import com.style.quiztrivia.database.dao.UserDao


//@TypeConverters(Converters::class)

@Database(entities = [ScoreModel::class, UserModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scoreDao(): ScoreDao


    abstract fun userDao(): UserDao
}
