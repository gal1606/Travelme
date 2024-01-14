package com.example.travelme.domain

import com.example.travelme.data.LocationService
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val locationService: LocationService
) {
    operator fun invoke(): Flow<LatLng?> = locationService.requestLocationUpdates()

}