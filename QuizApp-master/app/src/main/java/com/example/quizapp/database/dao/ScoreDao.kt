package com.example.quizapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quizapp.database.ScoreModel
import retrofit2.http.GET

@Dao
interface ScoreDao {

    @Query("select * from scoremodel")
    fun getAll(): LiveData<List<ScoreModel>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)

    fun insert(score: ScoreModel)

    @Delete
    fun delete(score: ScoreModel)

    @Query("delete from scoremodel")
    fun nukeTable()

}