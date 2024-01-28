package com.example.travelme.ui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.travelme.*
import com.example.travelme.R
import com.example.travelme.models.Trip
import com.example.travelme.models.UserDone
import com.example.travelme.models.UserLike
import com.example.travelme.ui.components.BottomBarScreen
import com.example.travelme.ui.components.ErrorDialog
import com.example.travelme.ui.components.LevelDropDown
import com.example.travelme.ui.components.TripDetails
import com.example.travelme.viewmodels.TripVM
import com.example.travelme.viewmodels.ViewState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SearchTripsScreen(
    navController: NavHostController,
    viewModel: TripVM = hiltViewModel()
) {
    val context = LocalContext.current
    val viewState by LocationViewModel.locationViewModel.viewState.collectAsStateWithLifecycle()
    val trips by viewModel.tripsApplied.observeAsState(initial = emptyList())
    val liked by viewModel.liked.observeAsState(initial = emptyList())
    val done by viewModel.done.observeAsState(initial = emptyList())
    val markersArray: MutableState<List<LatLng>> = remember { mutableStateOf(emptyList()) }
    val showSearchPopup = remember { mutableStateOf(false) }
    var descr by remember { mutableStateOf("") }
    val trip = remember { mutableStateOf(Trip(
        tripid = "",
        description = "",
        imageUrl = "",
        coord = LatLng(0.0, 0.0),
        level = "",
        length = 0.0,
        time = 0.0,
        pending = false
    ) ) }

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
                    markersArray.value = trips.map { it.coord }
                }

                if (ShowDialog.showDialog.value) {
                    ErrorDialog(DialogMessage.dialogMessage)
                }

                if(showSearchPopup.value) {
                    SearchTripsPopup(
                        navController,
                        trip.value,
                        trips,
                        liked,
                        done,
                        viewModel,
                        context)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    GoogleMap(
                        modifier = Modifier.height(200.dp),
                        cameraPositionState = cameraState,
                        properties = MapProperties(

                            mapType = MapType.HYBRID,

                            )
                    ) {
                        for (marker in markersArray.value)
                            Marker(
                                state = MarkerState(position = marker),
                                title = "MyPosition",
                                snippet = "This is a description of this Marker",
                                draggable = true,
                                onClick = {
                                    trip.value.coord = it.position
                                    false
                                }
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
                        LevelDropDown(trip)
                    }

                    TextField(
                        value = descr,
                        onValueChange = { descr = it },
                        label = { Text(stringResource(id = R.string.search_description)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp)
                    )

                    Button(
                        onClick = {
                            trip.value.description = descr
                            showSearchPopup.value = true
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                            .padding(bottom = 60.dp)
                    ) {
                        Text(text = stringResource(id = R.string.search_trips))
                    }
                }

            }
            else -> {}
        }
    }
}

@Composable
fun SearchTripsPopup(
    navController: NavHostController,
    trip: Trip,
    trips: List<Trip>,
    liked: List<UserLike>,
    done: List<UserDone>,
    viewModel: TripVM,
    context: Context
)
{
    var filteredTrips: List<Trip> = trips

    if (!(trip.coord.latitude == 0.0 && trip.coord.longitude == 0.0))
    {
        filteredTrips = filteredTrips.filter { t -> t.coord == trip.coord }
    }
    if (trip.description != "")
    {
        filteredTrips = filteredTrips.filter { t -> t.description.contains(trip.description) }
    }
    if (trip.level != "")
    {
        filteredTrips = filteredTrips.filter { t -> t.level == trip.level }
    }

    Dialog(
        onDismissRequest = { navController.navigate(BottomBarScreen.Search.route) }
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ){
            for(i in filteredTrips.indices)
            {
                TripDetails(
                    trip = filteredTrips[i],
                    liked = liked,
                    done = done,
                    onLikeClick = {
                        StoreViewModel.storeViewModel.like(filteredTrips[i].tripid, {}, {})
                        viewModel.createLike(trips[i], context)
                    },
                    onDoneClick = {
                        StoreViewModel.storeViewModel.done(filteredTrips[i].tripid, {}, {})
                        viewModel.createDone(trips[i], context)
                    }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = { navController.navigate(BottomBarScreen.Search.route) },
                modifier = Modifier.padding(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Navigation Icon"
                )
            }
        }
    }
}

