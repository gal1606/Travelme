package com.example.travelme.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelme.data.AppDatabase
import com.example.travelme.data.TripsRepo
import com.example.travelme.data.UserDoneRepo
import com.example.travelme.data.UserLikeDao
import com.example.travelme.data.UserLikeRepo
import com.example.travelme.models.Trip
import com.example.travelme.models.UserDone
import com.example.travelme.models.UserLike
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    @OptIn(DelicateCoroutinesApi::class)
    fun getTrip(tripId: String, context: Context, onSuccess: (Trip) -> Unit) {
        val database = AppDatabase
        val trip: Trip
        GlobalScope.launch(Dispatchers.Main) {
            database.getInstance(context).tripDao().getTrip(tripId).collect {
                onSuccess(it)
            }
        }

    }
}