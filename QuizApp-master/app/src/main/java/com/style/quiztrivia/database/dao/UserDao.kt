package com.style.quiztrivia.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.style.quiztrivia.database.ScoreModel
import com.style.quiztrivia.database.UserModel

@Dao
interface UserDao {


    @Query("select * from usermodel")
    fun observeUser(): LiveData<UserModel>

    @Query("select * from usermodel")
    suspend fun getUser(): UserModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserModel)

    @Update
    fun update(user: UserModel)

    @Delete
    fun delete(score: UserModel)

    @Query("delete from usermodel")
    suspend fun nukeTable()

}