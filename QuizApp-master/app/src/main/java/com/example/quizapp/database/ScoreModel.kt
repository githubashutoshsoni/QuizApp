package com.example.quizapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ScoreModel(

    @PrimaryKey val uid: Int = Math.random().toInt(),
    @ColumnInfo val score: Int?

)