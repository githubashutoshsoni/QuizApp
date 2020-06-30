package com.example.quizapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quizapp.database.dao.ScoreDao


//@TypeConverters(Converters::class)

@Database(entities = [ScoreModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scoreDao(): ScoreDao

//    abstract fun categoryDao(): CategoryDao
}
