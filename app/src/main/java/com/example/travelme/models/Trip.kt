package com.example.travelme.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.google.android.gms.maps.model.LatLng
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.travelme.data.Converters

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey @ColumnInfo(name = "id")
    var tripid: String,
    var description: String,
    var coord: LatLng,
    var level: String,
    var imageUrl: String = "",
    var length: Double,
    var time: Double
)
