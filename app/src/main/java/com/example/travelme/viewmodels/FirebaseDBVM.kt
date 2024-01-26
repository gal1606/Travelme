package com.example.travelme.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.travelme.CurrentUser
import com.example.travelme.models.Trip
import com.example.travelme.models.User
import com.example.travelme.models.UserDone
import com.example.travelme.models.UserLike
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.*
import org.checkerframework.checker.units.qual.Current

class FirebaseDBVM: ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val dbTrips: CollectionReference = db.collection("Trips")
    private val dbUsers: CollectionReference = db.collection("Users")
    private val dbLike: CollectionReference = db.collection("Like")
    private val dbDone: CollectionReference = db.collection("Done")

    fun addTrip(
        trip: Trip,
        onSuccess: (result: Trip) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        dbTrips.add(trip)
            .addOnSuccessListener {
                trip.tripid = it.id
                dbTrips.document(trip.tripid).update("id", trip.tripid).addOnSuccessListener {
                    onSuccess(trip)
                }
            }.addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun getTrips(
        onSuccess: (result: ArrayList<Trip>) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val tripsList: ArrayList<Trip> = arrayListOf()
        dbTrips.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val coord = document.data["coord"] as Map<*, *>
                val doc = Trip(
                    tripid = document.id,
                    description = document.data["description"].toString(),
                    imageUrl = document.data["imageUrl"].toString(),
                    coord = LatLng(coord["latitude"].toString().toDouble(), coord["longitude"].toString().toDouble()),
                    length = document.data["length"].toString().toDouble(),
                    time = document.data["time"].toString().toDouble(),
                    level = document.data["level"].toString()
                )
               //doc.tripid = document.id
                //doc.description = document.data["description"].toString()

                //doc.images = ArrayList<Uri>()
               // doc.imageUrl = Uri.parse(document.data["images"].toString()

                //doc.level = document.data["level"].toString()
                //doc.length = document.data["length"].toString().toDouble()
                //doc.time = document.data["time"].toString().toDouble()

                //val coord = document.data["coord"] as Map<*, *>
                //doc.coord = LatLng(coord["latitude"].toString().toDouble(), coord["longitude"].toString().toDouble())

                tripsList.add(doc)
            }
            onSuccess(tripsList)
        }
    }

   private fun getTrip(
       tripId: String,
       onSuccess: (result: Trip) -> Unit,
       onFailure: (exception: Exception) -> Unit
    ) {

       dbTrips.document(tripId).get().addOnSuccessListener { document ->

           //doc.tripid = document.id
           //doc.description = document.data?.get("description").toString()

           //doc.images = ArrayList<Uri>()
           //doc.images.add(Uri.parse(document.data?.get("images").toString().dropLast(1).drop(1)))

           //doc.level = document.data?.get("level").toString()
           //doc.length = document.data?.get("length").toString().toDouble()
           val doc = Trip(
                       tripid =     tripId,
                       time = document.data?.get("time").toString().toDouble(),
                       coord = LatLng(document.data?.get("coord").toString().toDouble(), document.data?.get("coord").toString().toDouble()),
                       description = document.data?.get("description").toString(),
                       level = document.data?.get("level").toString(),
                       length = document.data?.get("length").toString().toDouble()
                   )
               }
           }



    fun addUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        dbUsers.add(user)
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun like(
        tripId: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val data = hashMapOf("tripId" to tripId)

        dbLike.document(CurrentUser.currentUser.id).collection("like").document(tripId)
            .set(data).addOnCompleteListener{ it ->
                onSuccess()
            }
    }

    fun done(
        tripId: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val data = hashMapOf("tripId" to tripId)

        dbDone.document(CurrentUser.currentUser.id).collection("done").document(tripId)
            .set(data).addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun getLikedTrips(
        onSuccess: (result: ArrayList<UserLike>) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val tripIdsList: ArrayList<UserLike> = arrayListOf()

        dbLike.document(CurrentUser.currentUser.id).collection("like").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    tripIdsList.add(UserLike(tripid=document.id))
                }
                getTrips(
                    onSuccess = {
                        onSuccess(tripIdsList)
                    },
                    onFailure = {}
                )
            }
    }

    fun getDoneTrips(
        onSuccess: (result: ArrayList<UserDone>) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val tripIdsList: ArrayList<UserDone> = arrayListOf()
        dbDone.document(CurrentUser.currentUser.id).collection("done").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    tripIdsList.add(UserDone(tripid=document.id))
                }
                getTrips(
                    onSuccess = {
                        onSuccess(tripIdsList)
                    },
                    onFailure = {}
                )
            }
    }

    fun isTripLiked(
        tripId: String,
        onSuccess: (result: Boolean) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        var likedTrips: ArrayList<UserLike>
        getLikedTrips(
            onSuccess = { result ->
                likedTrips = result
                var trip: Trip
                getTrip(
                    tripId,
                    onSuccess = { res ->
                        trip = res
                        onSuccess(trip.tripid in likedTrips.map { it.tripid })
                    },
                    onFailure = {}
                )
            },
            onFailure = {}
        )
    }

    fun isTripDone(
        tripId: String,
        onSuccess: (result: Boolean) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        var doneTrips: ArrayList<UserDone>
        getDoneTrips(
            onSuccess = { result ->
                doneTrips = result
                var trip: Trip
                getTrip(
                    tripId,
                    onSuccess = { res ->
                        trip = res
                        onSuccess(trip.tripid in doneTrips.map { it.tripid })
                    },
                    onFailure = {}
                )
            },
            onFailure = {}
        )
    }
}


