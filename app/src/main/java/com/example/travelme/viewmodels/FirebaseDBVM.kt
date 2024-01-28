package com.example.travelme.viewmodels

import androidx.lifecycle.ViewModel
import com.example.travelme.CurrentUser
import com.example.travelme.models.Trip
import com.example.travelme.models.User
import com.example.travelme.models.UserDone
import com.example.travelme.models.UserLike
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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
                    level = document.data["level"].toString(),
                    pending = document.data["pending"].toString().toBoolean()
                )

                tripsList.add(doc)
            }
            onSuccess(tripsList)
        }
    }

    fun addUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        dbUsers.add(user)
            .addOnSuccessListener {
                CurrentUser.currentUser.id = it.id
                dbUsers.document(CurrentUser.currentUser.id).update("id", CurrentUser.currentUser.id).addOnSuccessListener {
                    onSuccess()
                }
            }.addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun updateUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        dbUsers.document(CurrentUser.currentUser.id).get()
            .addOnSuccessListener {
                val update: MutableMap<String, String> = HashMap()
                update["name"] = user.name
                update["profileImage"] = user.profileImage
                update["email"] = user.email
                update["id"] = user.id
                CurrentUser.currentUser = user
                dbUsers.document(CurrentUser.currentUser.id).set(update).addOnCompleteListener {
                    onSuccess()
                }
            }.addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun getUsers(
        onSuccess: (result: ArrayList<User>) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val usersList: ArrayList<User> = arrayListOf()
        dbUsers.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val user = User (
                    name = document.data["name"].toString(),
                    email = document.data["email"].toString(),
                    profileImage = document.data["profileImage"].toString(),
                    id = document.id
                )
                usersList.add(user)
            }
            onSuccess(usersList)
        }
    }

    fun like(
        tripId: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val data = hashMapOf("tripId" to tripId)

        dbLike.document(CurrentUser.currentUser.id).collection("like").document(tripId)
            .set(data).addOnCompleteListener{
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

    fun applyTrip(
        tripId: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        dbTrips.document(tripId).update("pending", false).addOnSuccessListener {
            onSuccess()
        }
    }

    fun declineTrip(
        tripId: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        dbTrips.document(tripId).update("pending", true).addOnSuccessListener {
            onSuccess()
        }
    }
}


