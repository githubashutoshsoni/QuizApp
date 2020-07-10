package com.style.quiztrivia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.style.quiztrivia.util.Result

import com.style.quiztrivia.database.UserModel

import com.style.quiztrivia.database.dao.ScoreDao
import com.style.quiztrivia.database.dao.UserDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import com.style.quiztrivia.util.Result.Success
import java.lang.Exception

class QuizLocalDataSource internal constructor(
    private val scoreDao: ScoreDao,
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    UserRepository {


    override fun deleteUser() {

    }

    override suspend fun logout() {
        nukeDetails()
    }

    override suspend fun insertUserDetails(user: UserModel) = withContext(ioDispatcher) {
        coroutineScope {

            userDao.nukeTable()
            userDao.insert(user)

        }

    }

    override fun observeUser(): LiveData<Result<UserModel>> {
        return userDao.observeUser().map {
            Success(it)
        }
    }

    override suspend fun getUser(): Result<UserModel> = withContext(ioDispatcher) {

        try {
            val usermodel = userDao.getUser()

            if (usermodel != null) {
                return@withContext Success(usermodel)
            }
            else
                return@withContext Result.Error(Exception("User not found"))

        } catch (e: Exception) {
            return@withContext Result.Error(e)

        }

    }


    override suspend fun nukeDetails() = withContext(ioDispatcher) {

        userDao.nukeTable()

    }


}