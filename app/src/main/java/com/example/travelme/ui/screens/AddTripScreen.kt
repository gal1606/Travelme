package com.example.travelme.ui.screens

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.travelme.*
import com.example.travelme.R
import com.example.travelme.models.Trip
import com.example.travelme.navigation.Graph
import com.example.travelme.ui.components.ErrorDialog
import com.example.travelme.ui.components.LevelDropDown
import com.example.travelme.viewmodels.TripVM
import com.example.travelme.viewmodels.ViewState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("MutableCollectionMutableState")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AddTripScreen(
    navController: NavHostController,
    viewModel: TripVM = hiltViewModel()
) {
    val context = LocalContext.current
    val viewState by LocationViewModel.locationViewModel.viewState.collectAsStateWithLifecycle()
    var selectImages by remember { mutableStateOf(arrayListOf<Uri>()) }
    var descr by remember { mutableStateOf("") }
    var length by remember { mutableDoubleStateOf(0.0) }
    var time by remember { mutableDoubleStateOf(0.0) }

    val trip = remember {  mutableStateOf( Trip(
        tripid = "",
        description = "",
        imageUrl = "",
        coord = LatLng(0.0, 0.0),
        level = "",
        length = 0.0,
        time = 0.0
    ) ) }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            selectImages = it as ArrayList<Uri>
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

                if (ShowDialog.showDialog.value) {
                    ErrorDialog(DialogMessage.dialogMessage)
                }

                val markerState = rememberMarkerState(position = LatLng(currentLoc.latitude, currentLoc.longitude))

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
                        LevelDropDown(trip)
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

                            trip.value.imageUrl = selectImages[0].toString()

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
                        value = descr,
                        onValueChange = {
                            descr = it
                        },
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
                            value = length.toString(),
                            onValueChange = {
                                length = it.toDouble()
                            },
                            label = { Text(stringResource(id = R.string.set_length)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
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
                            value = time.toString(),
                            onValueChange = {
                                time = it.toDouble()
                            },
                            label = { Text(stringResource(id = R.string.set_time)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Button(
                        onClick = {
                            trip.value.coord = markerState.position
                            trip.value.description = descr
                            trip.value.length = length
                            trip.value.time = time

                            if (trip.value.description.isEmpty() ||
                                trip.value.level == "" ||
                                trip.value.imageUrl == "" ||
                                trip.value.length == 0.0 ||
                                trip.value.time == 0.0 )
                            {
                                DialogMessage.dialogMessage = context.getString(R.string.error_empty_fields)
                                ShowDialog.showDialog.value = true
                            }
                            else
                            {
                                saveTripInDB(trip.value, context, viewModel, navController)

                            }
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

fun saveTripInDB(trip: Trip, context: Context, viewModel: TripVM, navController: NavHostController) {
    val imageBitmap = uriToBitmap(trip.imageUrl.toUri(), context)
    trip.imageUrl = ""
    bitmapToUrl(
        bitmap = imageBitmap,
        path = "trips/",
        onSuccess = { result ->
            trip.imageUrl = result.toString()
            StoreViewModel.storeViewModel.addTrip(
                trip = trip,
                onSuccess = {
                    viewModel.createTrip(trip, context).apply {
                        navController.navigate(Graph.MYTRIPS)
                    }
                },
                onFailure = {}
            )
        },
        onFailure = {}
    )
}