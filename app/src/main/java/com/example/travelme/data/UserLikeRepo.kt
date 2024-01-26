package com.example.travelme.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLikeRepo @Inject constructor(private val userLikeDao: UserLikeDao) {
    fun getAllTrips() = userLikeDao.getAllTrips()
    companion object {
        // For Singleton instantiation
        @Volatile private var instance: UserLikeRepo? = null
        fun getInstance(userLikeDao: UserLikeDao) =
            instance ?: synchronized(this) {
                instance ?: UserLikeRepo(userLikeDao).also { instance = it }
            }

    }
}