package com.example.travelme.viewmodels

import androidx.lifecycle.ViewModel
import com.example.travelme.models.Trip
import com.example.travelme.models.User
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseDBVM: ViewModel() {

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val dbTrips: CollectionReference = db.collection("Trips")

    fun addTrip(position: LatLng,
        onSuccess: (result: Trip) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val trip = Trip("", "", position, 0, listOf(""), 1.5, 1.5)
        dbTrips.add(trip)
            .addOnSuccessListener {
            // after the data addition is successful
            // we are displaying a success toast message.
            onSuccess(trip)

        }.addOnFailureListener { e ->
            onFailure(e)
        }
    }


}