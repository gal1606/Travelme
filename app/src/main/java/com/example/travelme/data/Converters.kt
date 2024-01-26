package com.example.travelme.data

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng

class Converters {
    @TypeConverter
    fun imagesToString(images: ArrayList<Uri>): String =
        images[0].toString().drop(1).dropLast(1)

    @TypeConverter
    fun stringToImages(value: String): ArrayList<Uri> {
        return arrayListOf(value.toUri())
    }

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