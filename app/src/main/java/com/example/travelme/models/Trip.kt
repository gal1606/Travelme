package com.example.travelme.models

import android.net.Uri
import com.google.android.gms.maps.model.LatLng

data class Trip(
    var id: String = "",
    val description: String  = "",
    val coord: LatLng = LatLng(0.0,0.0),
    var level: String = "",
    var images: ArrayList<Uri> = arrayListOf(),
    val length: Double = 0.0,
    val time: Double = 0.0
)
