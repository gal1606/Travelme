package com.example.travelme.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.travelme.data.AppDatabase
import com.example.travelme.data.TripsRepo
import com.example.travelme.data.UserDoneRepo
import com.example.travelme.data.UserLikeRepo
import com.example.travelme.models.Trip
import com.example.travelme.models.UserDone
import com.example.travelme.models.UserLike
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripVM @Inject internal constructor(
    tripsRepo: TripsRepo,
    userLikeRepo: UserLikeRepo,
    userDoneRepo: UserDoneRepo
) : ViewModel() {

   val trips: LiveData<List<Trip>> = tripsRepo.getAllTrips().asLiveData()
   val liked: LiveData<List<UserLike>> = userLikeRepo.getAllTrips().asLiveData()
   val done: LiveData<List<UserDone>> = userDoneRepo.getAllTrips().asLiveData()

    @OptIn(DelicateCoroutinesApi::class)
    fun createLike(trip: Trip, context: Context) {
        val database = AppDatabase
        GlobalScope.launch(Dispatchers.Main) {
            database.getInstance(context).userLikeDao().insert(UserLike(trip.tripid))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createDone(trip: Trip, context: Context) {
        val database = AppDatabase
        GlobalScope.launch(Dispatchers.Main) {
            database.getInstance(context).userDoneDao().insert(UserDone(trip.tripid))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createTrip(trip: Trip, context: Context) {
        val database = AppDatabase
        GlobalScope.launch(Dispatchers.Main) {
            database.getInstance(context).tripDao().insert(trip)
        }
    }
}