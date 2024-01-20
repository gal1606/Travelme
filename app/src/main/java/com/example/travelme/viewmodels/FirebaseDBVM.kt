package com.example.travelme.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.travelme.CurrentUser
import com.example.travelme.models.Trip
import com.example.travelme.models.User
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
                trip.id = it.id
                dbTrips.document(trip.id).update("id", trip.id).addOnSuccessListener {
                    onSuccess(trip)
                }
            }.addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun getTrips(
        onSuccess: (result: ArrayList<TripVM>) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val tripsList: ArrayList<TripVM> = arrayListOf()
        dbTrips.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val doc = TripVM()
                doc.id = document.id
                doc.description = document.data["description"].toString()

                doc.images = ArrayList<Uri>()
                doc.images.add(Uri.parse(document.data["images"].toString().dropLast(1).drop(1)))

                doc.level = document.data["level"].toString()
                doc.length = document.data["length"].toString().toDouble()
                doc.time = document.data["time"].toString().toDouble()

                val coord = document.data["coord"] as Map<*, *>
                doc.coord = LatLng(coord["latitude"].toString().toDouble(), coord["longitude"].toString().toDouble())

                tripsList.add(doc)
            }
            onSuccess(tripsList)
        }
    }

   private fun getTrip(
       tripId: String,
       onSuccess: (result: TripVM) -> Unit,
       onFailure: (exception: Exception) -> Unit
    ) {
       val doc = TripVM()
       dbTrips.document(tripId).get().addOnSuccessListener { document ->

           doc.id = document.id
           doc.description = document.data?.get("description").toString()

           doc.images = ArrayList<Uri>()
           doc.images.add(Uri.parse(document.data?.get("images").toString().dropLast(1).drop(1)))

           doc.level = document.data?.get("level").toString()
           doc.length = document.data?.get("length").toString().toDouble()
           doc.time = document.data?.get("time").toString().toDouble()

           val coord = document.data?.get("coord") as Map<*, *>
           doc.coord = LatLng(
               coord["latitude"].toString().toDouble(),
               coord["longitude"].toString().toDouble()
           )
           onSuccess(doc)
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
        onSuccess: (result: ArrayList<TripVM>) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val tripIdsList: ArrayList<String> = arrayListOf()
        var tripsList: ArrayList<TripVM> = arrayListOf()
        val tripsLiked: ArrayList<TripVM> = arrayListOf()
        dbLike.document(CurrentUser.currentUser.id).collection("like").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    tripIdsList.add(document.id)
                }
                getTrips(
                    onSuccess = {
                        tripsList = it
                        for (trip in tripsList) {
                            if (trip.id in tripIdsList) {
                                tripsLiked.add(trip)
                            }
                        }
                        onSuccess(tripsLiked)
                    },
                    onFailure = {}
                )
            }
    }

    fun getDoneTrips(
        onSuccess: (result: ArrayList<TripVM>) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val tripIdsList: ArrayList<String> = arrayListOf()
        var tripsList: ArrayList<TripVM>
        val tripsDone: ArrayList<TripVM> = arrayListOf()
        dbDone.document(CurrentUser.currentUser.id).collection("done").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    tripIdsList.add(document.id)
                }
                getTrips(
                    onSuccess = {
                        tripsList = it
                        for (trip in tripsList) {
                            if (trip.id in tripIdsList) {
                                tripsDone.add(trip)
                            }
                        }
                        onSuccess(tripsDone)
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
        var likedTrips: ArrayList<TripVM>
        getLikedTrips(
            onSuccess = { result ->
                likedTrips = result
                var trip: TripVM
                getTrip(
                    tripId,
                    onSuccess = { res ->
                        trip = res
                        onSuccess(trip.id in likedTrips.map { it.id })
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
        var doneTrips: ArrayList<TripVM>
        getDoneTrips(
            onSuccess = { result ->
                doneTrips = result
                var trip: TripVM
                getTrip(
                    tripId,
                    onSuccess = { res ->
                        trip = res
                        onSuccess(trip.id in doneTrips.map { it.id })
                    },
                    onFailure = {}
                )
            },
            onFailure = {}
        )
    }
}


