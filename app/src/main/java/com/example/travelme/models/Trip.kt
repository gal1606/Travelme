package com.example.travelme.models

import com.google.android.gms.maps.model.LatLng

data class Trip(
    val id: String,
    val description: String,
    val coord: LatLng,
    var level: Int,
    val images: List<String>,
    val length: Float,
    val time: Float
)
