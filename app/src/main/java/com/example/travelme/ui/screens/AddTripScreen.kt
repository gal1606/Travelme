package com.example.travelme.ui.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.travelme.*
import com.example.travelme.R
import com.example.travelme.ui.components.LevelDropDown
import com.example.travelme.viewmodels.ViewState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AddTripScreen() {
    val context = LocalContext.current
    val viewState by LocationViewModel.locationViewModel.viewState.collectAsStateWithLifecycle()

    var selectImages by remember { mutableStateOf(listOf<Uri>()) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            selectImages = it
        }

    with(viewState) {
        when (this) {
            is ViewState.Success -> {
                val currentLoc =
                    LatLng(
                        this.location?.latitude ?: 0.0,
                        this.location?.longitude ?: 0.0
                    )
                val cameraState = rememberCameraPositionState()

                LaunchedEffect(key1 = currentLoc) {
                    cameraState.centerOnLocation(currentLoc)
                }

                val markerState = rememberMarkerState(position = LatLng(currentLoc.latitude, currentLoc.longitude))
                var setLengthText by remember { mutableStateOf("") }
                var currentText by remember { mutableStateOf("") }
                var setTimeText by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    GoogleMap(
                        modifier = Modifier.height(400.dp),
                        cameraPositionState = cameraState,
                        properties = MapProperties(
                            isMyLocationEnabled = true,
                            mapType = MapType.HYBRID,
                            isTrafficEnabled = true
                        )
                    ) {
                        Marker(
                            state = markerState,
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
                            text = context.getString(R.string.choose_level),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Left,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        LevelDropDown()
                    }

                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(10.dp)
                    ) {
                        Text(text = stringResource(id = R.string.choose_photos))
                    }
                    LazyHorizontalGrid(
                        modifier = Modifier.height(100.dp),
                        rows = GridCells.Fixed(1)
                    ) {
                        items(selectImages) { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentScale = ContentScale.FillHeight,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(4.dp, 2.dp)
                                    .size(100.dp)
                                    .clickable {

                                    }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = currentText,
                        onValueChange = { currentText = it },
                        label = { Text(stringResource(id = R.string.trip_description)) },
                        minLines = 3,
                        maxLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(
                            value = setLengthText,
                            onValueChange = { setLengthText = it },
                            label = { Text(stringResource(id = R.string.set_length)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(
                            value = setTimeText,
                            onValueChange = { setTimeText = it },
                            label = { Text(stringResource(id = R.string.set_time)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Button(
                        onClick = {
                            saveTripInDB(markerState.position)
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    ) {
                        Text(text = stringResource(id = R.string.add_trips))
                    }
                }
            }
            else -> { }
        }
    }
}

fun saveTripInDB(position: LatLng)
{
    StoreViewModel.storeViewModel.addTrip(position, {}, {})
}