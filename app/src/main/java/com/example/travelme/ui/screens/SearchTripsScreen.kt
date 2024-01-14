package com.example.travelme.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import com.example.travelme.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.travelme.ui.components.LevelDropDown
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun SearchTripsScreen() {
    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth()
            .height(200.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val context = LocalContext.current
        //val intentScreen = Intent(context, LocationActivity::class.java)
        val extras = Bundle()
        extras.putString("screen", "searchTrips")
        //intentScreen.putExtras(extras)
        //startActivity(context, intentScreen, extras)
    }
}

@Composable
fun searchTripsScreen(currentPosition: LatLng, cameraState: CameraPositionState) {
    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)

    val markersArray: ArrayList<LatLng> = ArrayList()
    markersArray.add(LatLng(44.811058, 20.4617586))
    markersArray.add(LatLng(44.811058, 20.4627586))
    markersArray.add(LatLng(44.810058, 20.4627586))
    markersArray.add(LatLng(44.809058, 20.4627586))
    markersArray.add(LatLng(44.809058, 20.4617586))

    var searchLengthText by remember { mutableStateOf("") }
    var searchTimeText by remember { mutableStateOf("") }
    var searchDescriptionText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        GoogleMap(
            modifier = Modifier.height(200.dp),
            cameraPositionState = cameraState,
            properties = MapProperties(

                mapType = MapType.HYBRID,

                )
        ) {
            Marker(
                state = MarkerState(position = marker),
                title = "MyPosition",
                snippet = "This is a description of this Marker",
                draggable = true
            )
            Marker(
                state = MarkerState(position = LatLng(32.99656592492715, 35.69813236952394)),
                title = "MyPosition",
                snippet = "This is a description of this Marker",
                draggable = true
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.search_level),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onSurface
            )
            LevelDropDown()
        }
        TextField(
            value = searchLengthText,
            onValueChange = { searchLengthText = it },
            label = { Text(stringResource(id = R.string.search_length)) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        )

        TextField(
            value = searchTimeText,
            onValueChange = { searchTimeText = it },
            label = { Text(stringResource(id = R.string.search_time)) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        )

        TextField(
            value = searchDescriptionText,
            onValueChange = { searchDescriptionText = it },
            label = { Text(stringResource(id = R.string.search_description)) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        )

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.search_trips))
        }
    }
}

