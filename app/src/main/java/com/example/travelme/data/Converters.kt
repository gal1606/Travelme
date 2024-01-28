package com.example.travelme.data

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng

class Converters {

    @TypeConverter
    fun coordToString(coord: LatLng): String =
        coord.latitude.toString() + ";" + coord.longitude.toString()

    @TypeConverter
    fun stringToCoord(value: String): LatLng {
        return LatLng(
           value.split(";")[0].toDouble(),
           value.split(";")[1].toDouble()
        )
    }
}