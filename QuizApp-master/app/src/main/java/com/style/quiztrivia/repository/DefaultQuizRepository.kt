package com.style.quiztrivia.repository

import androidx.lifecycle.LiveData

import com.style.quiztrivia.util.Result
import com.style.quiztrivia.database.UserModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultQuizRepository(
    private val userLocalRepository: UserRepository,
    private val userRemoteRepository: UserRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO


) : UserRepository {
    override suspend fun insertUserDetails(user: UserModel) {
        userLocalRepository.insertUserDetails(user)
    }

    override fun observeUser(): LiveData<Result<UserModel>> {
        return userLocalRepository.observeUser()
    }

    override suspend fun getUser(): Result<UserModel> {
        return userLocalRepository.getUser()
    }


    override fun deleteUser() {

    }

    override suspend fun logout() {
        nukeDetails()
    }

    override suspend fun nukeDetails() {
        userLocalRepository.nukeDetails()
    }


}