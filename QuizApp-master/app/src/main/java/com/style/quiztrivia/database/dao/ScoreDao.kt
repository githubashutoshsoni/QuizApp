package com.style.quiztrivia.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.style.quiztrivia.database.ScoreModel

@Dao
interface ScoreDao {

    @Query("select * from scoremodel")
    fun getAll(): LiveData<List<ScoreModel>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(score: ScoreModel)

    @Delete
    fun delete(score: ScoreModel)

    @Query("delete from scoremodel")
    suspend fun nukeTable()

}