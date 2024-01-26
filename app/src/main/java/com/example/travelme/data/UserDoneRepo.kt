package com.example.travelme.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDoneRepo @Inject constructor(private val userDoneDao: UserDoneDao) {
    fun getAllTrips() = userDoneDao.getAllTrips()

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: UserDoneRepo? = null

        fun getInstance(userDoneDao: UserDoneDao) =
            instance ?: synchronized(this) {
                instance ?: UserDoneRepo(userDoneDao).also { instance = it }
            }

    }
}