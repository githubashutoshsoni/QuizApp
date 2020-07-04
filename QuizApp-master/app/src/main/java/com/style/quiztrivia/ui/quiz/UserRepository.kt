package com.style.quiztrivia.ui.quiz


import androidx.lifecycle.LiveData
import com.style.quiztrivia.database.Result
import com.style.quiztrivia.database.UserModel

interface UserRepository {


    suspend fun insertUserDetails(user: UserModel)

    fun observeUser(): LiveData<Result<UserModel>>


    suspend fun getUser(): Result<UserModel>

    fun deleteUser()

    suspend fun logout()

    suspend fun nukeDetails()

}