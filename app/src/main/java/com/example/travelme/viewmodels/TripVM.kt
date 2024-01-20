package com.example.travelme.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelme.models.User
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class TripVM: ViewModel() {

    private var _id: String by mutableStateOf("")
    var id: String
        get() = _id
        set(value) {
            _id = value
        }

    private var _description: String by mutableStateOf("")
    var description: String
        get() = _description
        set(value) {
            _description = value
        }

    private var _coord: LatLng by mutableStateOf(LatLng(0.0, 0.0))
    var coord: LatLng
        get() = _coord
        set(value) {
            _coord = value
        }

    private var _level: String by mutableStateOf("")
    var level: String
        get() = _level
        set(value) {
            _level = value
        }

    private var _images: ArrayList<Uri> by mutableStateOf(arrayListOf<Uri>())
    var images: ArrayList<Uri>
        get() = _images
        set(value) {
            _images = value
        }

    private var _length: Double by mutableStateOf(0.0)
    var length: Double
        get() = _length
        set(value) {
            _length = value
        }

    private var _time: Double by mutableStateOf(0.0)
    var time: Double
        get() = _time
        set(value) {
            _time = value
        }
}